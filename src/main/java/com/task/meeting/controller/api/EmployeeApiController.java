package com.task.meeting.controller.api;

import com.task.meeting.entity.Employee;
import com.task.meeting.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeApiController {

    private final EmployeeService employeeService;

//    @GetMapping("/search")
//    public List<Employee> search(@RequestParam("keyword") String keyword) {
//        // 결과가 없으면 빈 리스트([])가 JSON으로 나갑니다.
//        return employeeService.searchEmployees(keyword);
//    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> search(@RequestParam("keyword") String keyword) {
        List<Employee> result = employeeService.searchEmployees(keyword);
        return ResponseEntity.ok(result); // 빈 배열 포함
    }
}
