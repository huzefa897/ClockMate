package com.ClockMate.repository;

import com.ClockMate.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByUserId(Long userId);

    List<Attendance> findByUserIdOrderByTimestampAsc(Long userId);
}
