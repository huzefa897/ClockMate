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

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Clock In/Out using PIN
    @PostMapping("/record")
    public String recordAttendanceByPin(@RequestParam int pin) {
        return attendanceService.recordTimeStampByPin(pin);
    }

    // Get status using PIN
    @GetMapping("/{pin}/status")
    public String getStatusByPin(@PathVariable int pin) {
        return attendanceService.getStatusByPin(pin);
    }

    // Get raw timestamps using PIN
    @GetMapping("/{pin}/timestamps")
    public List<Attendance> getTimestamps(@PathVariable int pin) {
        return attendanceService.getUserLogsByPin(pin);
    }

    // Get sessions between dates using PIN
    @GetMapping("/{pin}/sessions")
    public List<AttendanceSessionDTO> getSessionsBetweenDatesByPin(
            @PathVariable int pin,
            @RequestParam String from,
            @RequestParam String to) {
        return attendanceService.getSessionsBetweenDatesByPin(pin, LocalDate.parse(from), LocalDate.parse(to));
    }

    // Export full session log using PIN
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAttendanceByPin(@RequestParam int pin) throws IOException {
        return attendanceService.exportAllSessionsExcelByPin(pin);
    }

    // Export session log between date range using PIN
    @GetMapping("/export-range")
    public ResponseEntity<byte[]> exportAttendanceRangeByPin(
            @RequestParam int pin,
            @RequestParam String from,
            @RequestParam String to) {
        return attendanceService.exportSessionsExcelByPinAndRange(pin, LocalDate.parse(from), LocalDate.parse(to));
    }
}
