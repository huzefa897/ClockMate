package com.ClockMate.service;

import com.ClockMate.model.Employee;
import com.ClockMate.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ClockMate.dto.AttendanceSessionDTO;
import com.ClockMate.model.Attendance;
import com.ClockMate.model.AttendanceRecord;
import com.ClockMate.model.AttendanceSessions;
import com.ClockMate.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Utility
    private Long getUserIdFromPin(int pin) {
        return employeeRepository.findByPin(pin)
                .map(Employee::getId)
                .orElseThrow(() -> new RuntimeException("Invalid PIN"));
    }

    // --- Attendance Recording ---
    public String recordTimeStamp(Long userId) {
        attendanceRepository.save(new Attendance(userId, LocalDateTime.now()));
        int count = attendanceRepository.findByUserId(userId).size();
        return count % 2 == 0 ? "Clocked Out Successfully" : "Clocked In Successfully";
    }

    public String recordTimeStampByPin(int pin) {
        Long userId = getUserIdFromPin(pin);
        return recordTimeStamp(userId);
    }

    // --- Attendance Status ---
    public String getStatus(Long userId) {
        int count = attendanceRepository.findByUserId(userId).size();
        return count % 2 == 0 ? "Clocked Out" : "Clocked In";
    }

    public String getStatusByPin(int pin) {
        return getStatus(getUserIdFromPin(pin));
    }

    // --- Timestamps ---
    public List<Attendance> getUserLogs(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    public List<Attendance> getUserLogsByPin(int pin) {
        return getUserLogs(getUserIdFromPin(pin));
    }

    public List<Attendance> getAllLogs() {
        return attendanceRepository.findAll();
    }

    // --- Sessions ---
    public List<AttendanceSessionDTO> getSessionsWithDuration(Long userId) {
        List<Attendance> records = attendanceRepository.findByUserIdOrderByTimestampAsc(userId);
        List<AttendanceSessionDTO> sessions = new ArrayList<>();

        for (int i = 0; i + 1 < records.size(); i += 2) {
            sessions.add(new AttendanceSessionDTO(records.get(i).getTimestamp(), records.get(i + 1).getTimestamp()));
        }

        if (records.size() % 2 != 0) {
            sessions.add(new AttendanceSessionDTO(records.get(records.size() - 1).getTimestamp(), null));
        }

        return sessions;
    }

    public List<AttendanceSessionDTO> getSessionsWithDurationByPin(int pin) {
        return getSessionsWithDuration(getUserIdFromPin(pin));
    }

    public List<AttendanceSessionDTO> getSessionsBetweenDates(Long userId, LocalDate start, LocalDate end) {
        return getSessionsWithDuration(userId).stream()
                .filter(session -> {
                    LocalDate sessionDate = session.getClockIn().toLocalDate();
                    return !sessionDate.isBefore(start) && !sessionDate.isAfter(end);
                }).toList();
    }

    public List<AttendanceSessionDTO> getSessionsBetweenDatesByPin(int pin, LocalDate start, LocalDate end) {

        return getSessionsBetweenDates(getUserIdFromPin(pin), start, end);
    }

    // --- Excel Export ---
    public ByteArrayInputStream exportSessionstoExcel(Long userId) {
        return generateExcel(getSessionsWithDuration(userId));
    }

    public ByteArrayInputStream exportSessionstoExcelByPin(int pin) {
        return exportSessionstoExcel(getUserIdFromPin(pin));
    }

    public ByteArrayInputStream exportSessionsToExcelBetweenDates(Long userId, LocalDate from, LocalDate to) {
        return generateExcel(getSessionsBetweenDates(userId, from, to));
    }

    public ByteArrayInputStream exportSessionsToExcelBetweenDatesByPin(int pin, LocalDate from, LocalDate to) {
        return exportSessionsToExcelBetweenDates(getUserIdFromPin(pin), from, to);
    }

    private ByteArrayInputStream generateExcel(List<AttendanceSessionDTO> sessions) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Attendance");
            String[] headers = {"ClockIn Date", "ClockIn Time", "ClockOut Date", "ClockOut Time", "Hours", "Minutes", "Raw Hours", "Rounded Hours"};
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (AttendanceSessionDTO session : sessions) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(session.getClockIn().format(dateFormatter));
                row.createCell(1).setCellValue(session.getClockIn().format(timeFormatter));
                row.createCell(2).setCellValue(session.getClockOut() != null ? session.getClockOut().format(dateFormatter) : "N/A");
                row.createCell(3).setCellValue(session.getClockOut() != null ? session.getClockOut().format(timeFormatter) : "N/A");
                row.createCell(4).setCellValue(session.getHours());
                row.createCell(5).setCellValue(session.getMinutes());
                row.createCell(6).setCellValue(session.getTotalHours());
                row.createCell(7).setCellValue(session.getRoundedHours());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }
    }
    public ResponseEntity<byte[]> exportAllSessionsExcelByPin(int pin) throws IOException {
        Long userId = getUserIdFromPin(pin);
        Employee employee = employeeRepository.findById(userId).orElseThrow();
        ByteArrayInputStream excel = exportSessionstoExcel(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ClockMate_" + employee.getName() + "_Sessions.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel.readAllBytes());
    }

    public ResponseEntity<byte[]> exportSessionsExcelByPinAndRange(int pin, LocalDate from, LocalDate to) {
        Long userId = getUserIdFromPin(pin);
        Employee employee = employeeRepository.findById(userId).orElseThrow();

        try (ByteArrayInputStream excel = exportSessionsToExcelBetweenDates(userId, from, to)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=ClockMate_" + employee.getName() + "_Sessions_" + from + "_to_" + to + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }
    }

}



