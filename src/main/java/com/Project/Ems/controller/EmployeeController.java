package com.Project.Ems.controller;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.DTO.EmployeeRequest;
import com.Project.Ems.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeRequest employee){
        log.info("Received request to add employee: {}", employee);
        Employee savedEmployee = employeeService.addemployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);

        }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public List<Employee> getAllEmployees(){
        log.info("Received request to get all employees");
        return  employeeService.getAllEmployees();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        log.info("Received request to get employee by ID: {}", id);
        return employeeService.getEmployeeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable ("id") Long id, @Valid @RequestBody EmployeeRequest employee) {
        log.info("Received request to update employee with ID: {}", id);
        return employeeService.updateEmployee(id, employee);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        log.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return "Employee Deleted Succesfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> getAllEmployees( @RequestParam(required = false) String name,@RequestParam(required = false) String email,
                                                           @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String department,
                                                           @RequestParam(required = false) String address) {
        log.info("Received request to search employees with name: {}, email: {}, phoneNumber: {}, department: {}, address: {}", name, email, phoneNumber, department, address);
        List<Employee> employees = employeeService.getAllEmployees(name, email, phoneNumber, department, address);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/sort")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam String sortBy) {
        log.info("Received request to sort employees by: {}", sortBy);
        List<Employee> employees = employeeService.getAllEmployees(sortBy);
        return new ResponseEntity<>(employees, HttpStatus.OK);
        
    }


}
