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

    public String recordTimeStamp(Long userId){
        Attendance attendance = new Attendance(userId, LocalDateTime.now());
        attendanceRepository.save(attendance);

        int count = attendanceRepository.findByUserId(userId).size();
        return count % 2 == 0 ? "Clocked Out Successfully" : "Clocked In Successfully";

    }

    public String recordTimeStampByPin(int pin){
        Optional<Employee> employeeOpt= employeeRepository.findByPin(pin);
        if(employeeOpt.isEmpty()){
            return "Invalid Pin";
        }
        Long userId = employeeOpt.get().getId();
        List<Attendance> entries = attendanceRepository.findByUserId(userId);
        Attendance attendance = new Attendance(userId,LocalDateTime.now());
        attendanceRepository.save(attendance);
        return entries.size() % 2 == 0 ? "Clocked In Successfully" : "Clocked Out Successfully";

    }
    public List<LocalDateTime> getTimeStamps(Long userId) {
        return attendanceRepository.findByUserIdOrderByTimestampAsc(userId)
                .stream()
                .map(Attendance::getTimestamp)
                .collect(Collectors.toList());
    }
    public String getStatus(Long userId){
        int count = attendanceRepository.findByUserId(userId).size();
        return count % 2 == 0 ? "Clocked Out" : "Clocked In";
    }
    public List<Attendance> getUserLogs(Long userId){
        return attendanceRepository.findByUserId(userId);
    }

    public List<Attendance> getAllLogs(){
        return attendanceRepository.findAll();
    }
    public List<AttendanceSessions> getSessionsForUser(Long userId) {

    List<Attendance> records = attendanceRepository.findByUserId(userId);
    records.sort(Comparator.comparing(Attendance::getTimestamp));

    List<AttendanceSessions> sessions = new ArrayList<>();
    for (int i = 0; i < records.size()-1; i++) {
        LocalDateTime in = records.get(i).getTimestamp();
        LocalDateTime out = records.get(i + 1).getTimestamp();
        sessions.add(new AttendanceSessions(in, out));
    }
    if(records.size()%2!=0){
        LocalDateTime in =records.get(records.size()-1).getTimestamp();
        sessions.add(new AttendanceSessions(in,null));
    }
    return sessions;
    }

    public List<AttendanceSessionDTO> getSessionsBetweenDates(Long userId, LocalDate start, LocalDate end) {
        List<AttendanceSessionDTO> allSessions = getSessionsWithDuration(userId);
        return allSessions.stream()
                .filter(session -> {
                    LocalDate sessionDate = session.getClockIn().toLocalDate();
                    return (sessionDate.isEqual(start) || sessionDate.isAfter(start)) &&
                            (sessionDate.isEqual(end) || sessionDate.isBefore(end));
                })
                .toList();
    }

    public ByteArrayInputStream exportSessionsToExcelBetweenDates(Long userId,LocalDate from,LocalDate to){
        List<AttendanceSessionDTO> filteredSessions = getSessionsBetweenDates(userId,from,to);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sessions");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Clock In Date");
            header.createCell(1).setCellValue("Clock In Time");
            header.createCell(2).setCellValue("Clock Out Date");
            header.createCell(3).setCellValue("Clock Out Time");
            header.createCell(4).setCellValue("Duration (hrs)");

            int rowIndex= 1;
            for(AttendanceSessionDTO session :filteredSessions){
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
        }catch (IOException e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }

    }
    public List<AttendanceSessionDTO> getSessionsWithDuration(Long userId) {
        List<Attendance> records = attendanceRepository.findByUserIdOrderByTimestampAsc(userId);
        List<AttendanceSessionDTO> sessions = new ArrayList<>();

        for (int i = 0; i + 1 < records.size(); i += 2) {
            LocalDateTime clockIn = records.get(i).getTimestamp();
            LocalDateTime clockOut = records.get(i + 1).getTimestamp();
            sessions.add(new AttendanceSessionDTO(clockIn, clockOut));
        }

        // Optional: handle incomplete session (odd number of timestamps)
        if (records.size() % 2 != 0) {
            LocalDateTime clockIn = records.get(records.size() - 1).getTimestamp();
            sessions.add(new AttendanceSessionDTO(clockIn, null));
        }

        return sessions;
    }
    public ByteArrayInputStream exportSessionstoExcel(Long userId){
        List<AttendanceSessionDTO> sessions = getSessionsWithDuration(userId);
      try(Workbook workbook = new XSSFWorkbook();ByteArrayOutputStream out = new ByteArrayOutputStream()) {
          Sheet sheet = workbook.createSheet("Attendance");

          Row headerRow = sheet.createRow(0);
          String[] header = {
                  "ClockIn Date", "ClockIn Time", "ClockOut Date", "ClockOut Time",
                  "Hours", "Minutes", "Raw Total Hours", "Rounded Total Hours"
          };
          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

          for (int i = 0; i < header.length; i++) {
              Cell cell = headerRow.createCell(i);
              cell.setCellValue(header[i]);
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
      }catch (IOException e) {
          throw new RuntimeException("Failed to export Excel file: " + e.getMessage());
      }
    }


    }


