package com.ClockMate.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class AttendanceSessionDTO {
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private Long hours;
    private Long minutes;
    private double totalHours;
    private double roundedHours;

    public AttendanceSessionDTO(LocalDateTime clockIn, LocalDateTime clockOut) {
        this.clockIn = clockIn;
        this.clockOut = clockOut;

        if (clockOut != null) {
            Duration duration = Duration.between(clockIn, clockOut);
            this.hours = duration.toHours();
            this.minutes = duration.toMinutes() % 60;
            double rawHours = hours + minutes / 60.0;
            this.totalHours = Math.round(rawHours * 4) / 4.0;
        } else {
            this.hours = 0L;
            this.minutes = 0L;
            this.totalHours = 0;
        }
    }
    public double getRoundedHours() {
        return roundedHours;
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

    public Long getHours() {
        return hours;
    }

    public Long getMinutes() {
        return minutes;
    }

    public double getTotalHours() {
        return totalHours;
    }
}
