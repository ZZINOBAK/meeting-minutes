package com.task.meeting.repository;

import com.task.meeting.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> searchEmployees(String keyword);

}
