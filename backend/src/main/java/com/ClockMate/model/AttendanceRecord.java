package com.ClockMate.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRecord {
    private Long userId;
    private List<LocalDateTime> timestamps= new ArrayList<>();
    public AttendanceRecord(){}
    public AttendanceRecord(Long userId){
        this.userId=userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<LocalDateTime> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<LocalDateTime> timestamps) {
        this.timestamps = timestamps;
    }
    public String getStatus() {
        return timestamps.size() % 2 == 0 ? "Clocked Out" : "Clocked In";
    }
    public void addTimestamp(LocalDateTime timestamp) {
        this.timestamps.add(timestamp);
    }
}
