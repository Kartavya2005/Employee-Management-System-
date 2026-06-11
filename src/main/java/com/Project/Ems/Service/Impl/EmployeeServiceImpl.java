package com.Project.Ems.Service.Impl;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.DTO.EmployeeRequest;
import com.Project.Ems.Exceptions.EmployeeAlreadyExistException;
import com.Project.Ems.Exceptions.EmployeeNotFoundException;
import com.Project.Ems.Repository.EmployeeRepository;
import com.Project.Ems.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee addemployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeAlreadyExistException("Employee with email " + request.getEmail() + " already exists");
        }
        if(employeeRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new EmployeeAlreadyExistException("Employee with phone number " + request.getPhoneNumber() + " already exists");
        }
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setAddress(request.getAddress());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setRole(request.getRole());
        employee.setSalary(request.getSalary());
        employee.setActive(true);
        employee.setCreatedAt(Instant.now());
        employee.setUpdatedAt(Instant.now());

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(long id, EmployeeRequest employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

//        if(employee.getName() != null && !employee.getName().isBlank()) {
        existingEmployee.setName(employee.getName()==null?existingEmployee.getName():employee.getName());
//        }
        if(employee.getEmail() != null && !employee.getEmail().isBlank()) {
            existingEmployee.setEmail(employee.getEmail());
        }
        if(employee.getAddress() != null && !employee.getAddress().isBlank()) {
            existingEmployee.setAddress(employee.getAddress());
        }
        if(employee.getPhoneNumber() != null && !employee.getPhoneNumber().isBlank()) {
            existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        }
        if (employee.getPassword() != null && !employee.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        if(employee.getDepartment() != null && !employee.getDepartment().isBlank()) {
            existingEmployee.setDepartment(employee.getDepartment());
        }
        if(employee.getDesignation() != null && !employee.getDesignation().isBlank()) {
            existingEmployee.setDesignation(employee.getDesignation());
        }
        if(employee.getJoiningDate() != null) {
            existingEmployee.setJoiningDate(employee.getJoiningDate());
        }
        if(employee.getRole() != null) {
            existingEmployee.setRole(employee.getRole());
        }
        if(employee.getSalary() > 0) {
            existingEmployee.setSalary(employee.getSalary());
        }
//            existingEmployee.setUpdatedAt(employee.getUpdatedAt());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);

    }

    @Override
    public List<Employee> getAllEmployees(String name,String email,String phoneNumber,String department,String address) {

//        if(keyword == null || keyword.isBlank()) {
//            return employeeRepository.findAll();
//        }
        return employeeRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingOrDepartmentIgnoreCaseOrAddressContainingIgnoreCase(
                name,email,phoneNumber,department,address);
    }

    @Override
    public List<Employee> getAllEmployees(String sortBy) {
        return employeeRepository.findAll(org.springframework.data.domain.Sort.by(sortBy));
    }
}
