package com.Project.Ems.controller;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.DTO.EmployeeRequest;
import com.Project.Ems.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeRequest employee){
        Employee savedEmployee = employeeService.addemployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return  employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable ("id") Long id, @Valid @RequestBody EmployeeRequest employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        employeeService.deleteEmployee(id);
        return "Employee Deleted Succesfully";
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> getAllEmployees( @RequestParam(required = false) String name,@RequestParam(required = false) String email,
                                                           @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String department,
                                                           @RequestParam(required = false) String address) {
        List<Employee> employees = employeeService.getAllEmployees(name, email, phoneNumber, department, address);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam String sortBy) {
        List<Employee> employees = employeeService.getAllEmployees(sortBy);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


}
