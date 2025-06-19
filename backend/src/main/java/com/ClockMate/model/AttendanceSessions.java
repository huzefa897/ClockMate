package com.ClockMate.model;

import com.ClockMate.service.AttendanceService;

import java.time.LocalDateTime;

public class AttendanceSessions {
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

    public AttendanceSessions() {}
    public AttendanceSessions(LocalDateTime clockIn,LocalDateTime clockOut){
        this.clockIn=clockIn;
        this.clockOut=clockOut;
    }

    public LocalDateTime getClockIn() {
        return clockIn;
    }

    public void setClockIn(LocalDateTime clockIn) {
        this.clockIn = clockIn;
    }

    public LocalDateTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalDateTime clockOut) {
        this.clockOut = clockOut;
    }
}
