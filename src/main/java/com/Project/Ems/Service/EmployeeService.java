package com.Project.Ems.Service;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.DTO.EmployeeRequest;

import java.util.List;

public interface EmployeeService {
    Employee addemployee(EmployeeRequest request);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

    Employee updateEmployee(long id, EmployeeRequest employee);

    void deleteEmployee(long id);

    List<Employee> getAllEmployees(String name, String email, String phoneNumber, String department, String address);

    List<Employee> getAllEmployees(String sortBy);
//    List<Employee> searchByName(String name);
//    List<Employee> searchByPhoneNumber(String phoneNumber);
//    List<Employee> searchByEmail(String email);
}
