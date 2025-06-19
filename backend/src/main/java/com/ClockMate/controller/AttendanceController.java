package com.ClockMate.controller;

import com.ClockMate.dto.AttendanceSessionDTO;
import com.ClockMate.model.Attendance;
import com.ClockMate.model.Employee;
import com.ClockMate.repository.EmployeeRepository;
import com.ClockMate.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    // Record attendance using user ID
    @PostMapping("/{userId}")
    public String record(@PathVariable Long userId) {
        return attendanceService.recordTimeStamp(userId);
    }

    // Record attendance using PIN
    @PostMapping("/record")
    public String recordAttendanceByPin(@RequestParam int pin) {
        return attendanceService.recordTimeStampByPin(pin);
    }

    // Get status using PIN
    @GetMapping("/{pin}/status")
    public String getStatusByPin(@PathVariable int pin) {
        Optional<Employee> employeeOpt = employeeRepository.findByPin(pin);
        return employeeOpt.map(employee -> attendanceService.getStatus(employee.getId()))
                .orElse("Invalid PIN");
    }

    // Get timestamps by user ID
    @GetMapping("/{userId}/timestamps")
    public List<Attendance> getUserLogs(@PathVariable Long userId) {
        return attendanceService.getUserLogs(userId);
    }

    // Get all logs
    @GetMapping("/all")
    public List<Attendance> getAllLogs() {
        return attendanceService.getAllLogs();
    }

    // Get sessions between dates by user ID
    @GetMapping("/{userId}/sessions")
    public List<AttendanceSessionDTO> getSessionsBetweenDates(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        return attendanceService.getSessionsBetweenDates(userId, startDate, endDate);
    }

    // Export full session log by user ID
    @GetMapping("/{userId}/export")
    public ResponseEntity<byte[]> exportAttendance(@PathVariable Long userId) throws IOException {
        ByteArrayInputStream excel = attendanceService.exportSessionstoExcel(userId);
        byte[] bytes = excel.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ClockMate_User_" + userId + "_Sessions.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    // Export full session log by PIN
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAttendanceByPin(@RequestParam int pin) throws IOException {
        Optional<Employee> employeeOpt = employeeRepository.findByPin(pin);
        if (employeeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Employee employee = employeeOpt.get();
        ByteArrayInputStream excel = attendanceService.exportSessionstoExcel(employee.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ClockMate_" + employee.getName() + "_Sessions.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel.readAllBytes());
    }

    // Export sessions between dates by user ID
    @GetMapping("/{userId}/export-range")
    public ResponseEntity<byte[]> exportAttendanceBetweenDates(
            @PathVariable Long userId,
            @RequestParam String from,
            @RequestParam String to) throws IOException {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        ByteArrayInputStream excel = attendanceService.exportSessionsToExcelBetweenDates(userId, startDate, endDate);
        byte[] bytes = excel.readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ClockMate_User_" + userId + "_Sessions_" + from + "_to_" + to + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    @GetMapping("/export-range-by-pin")
    public ResponseEntity<byte[]> exportEmployeePin(@RequestParam int pin,
                                                    @RequestParam String from,
                                                    @RequestParam String to) {
        Optional<Employee> employee = employeeRepository.findByPin(pin);
        if (employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Long userId = employee.get().getId();
        String name = employee.get().getName();
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        try (ByteArrayInputStream excel = attendanceService.exportSessionsToExcelBetweenDates(userId, startDate, endDate)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=ClockMate_" + name + "_Sessions_" + from + "_to_" + to + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }
    }

}