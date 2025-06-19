package com.ClockMate.repository;

import com.ClockMate.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Optional<Employee> findByPin(int pin); // Used for login with 4-digit PIN

}
