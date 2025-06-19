package com.ClockMate.controller;

import com.ClockMate.model.Employee;
import com.ClockMate.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;



    @PostMapping("/login")
    public String loginWithPin(@RequestParam int pin){
        Optional<Employee> employee = employeeRepository.findByPin(pin);
        return employee.map(value -> "Login successful for user: " + value.getName())
                .orElse("Invalid PIN");
    }

    @PostMapping("/Register")
    public String registerAUser(@RequestParam String name , @RequestParam int pin){
        if(employeeRepository.findByPin(pin).isPresent()){
            return "Pin Already in use. Choose a different one.";
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setPin(pin);
        employeeRepository.save(employee);
        return "User registered successfully with ID: " + employee.getId();
        }
    }

