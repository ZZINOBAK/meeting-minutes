package com.task.meeting.service;


import com.task.meeting.entity.Employee;
import com.task.meeting.repository.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeesMapper;

    public List<Employee> searchEmployees(String keyword) {
        return employeesMapper.searchEmployees(keyword);
    }

}
