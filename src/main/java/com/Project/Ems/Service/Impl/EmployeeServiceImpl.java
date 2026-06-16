package com.Project.Ems.Service.Impl;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.DTO.EmployeeRequest;
import com.Project.Ems.Enum.Role;
import com.Project.Ems.Exceptions.EmployeeAlreadyExistException;
import com.Project.Ems.Exceptions.EmployeeNotFoundException;
import com.Project.Ems.Repository.EmployeeRepository;
import com.Project.Ems.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee addemployee(EmployeeRequest request) {
        log.info("Received request to add employee: {}", request);
        if (employeeRepository.existsByEmail(request.getEmail())) {
            log.error("Employee with email {} already exists", request.getEmail());
            throw new EmployeeAlreadyExistException("Employee with email " + request.getEmail() + " already exists");
        }
        if(employeeRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.error("Employee with phone number {} already exists", request.getPhoneNumber());
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
        log.info("Employee added successfully: {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Getting all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long id) {
        log.info("Getting employee by ID: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(long id, EmployeeRequest employee) {
        log.info("Updating employee with ID: {}", id);
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
        log.warn("Employee Updated Successfully ");
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

    @Override
    public void importAllEmployees(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();
        Set<String> importedEmails = new HashSet<>();
        Set<String> importedPhoneNumber = new HashSet<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet =
                workbook.getSheetAt(0);

        Set<String> existingEmails =
                employeeRepository.findAll()
                        .stream()
                        .map(Employee::getEmail)
                        .collect(Collectors.toSet());

        Set<String> existingPhones =
                employeeRepository.findAll()
                        .stream()
                        .map(Employee::getPhoneNumber)
                        .collect(Collectors.toSet());
        boolean firstrow = true;
        for(Row row : sheet){

            if(firstrow){
                firstrow = false;
                continue;
            }

            String name = row.getCell(0).getStringCellValue();
            String email = row.getCell(1).getStringCellValue();
            String address = row.getCell(2).getStringCellValue();
            String phoneNumber = row.getCell(3).getStringCellValue();
            String password = row.getCell(4).getStringCellValue();
            String department = row.getCell(5).getStringCellValue();
            String designation = row.getCell(6).getStringCellValue();
            Cell joiningDateCell = row.getCell(7);
            LocalDate joiningDate;
            if(joiningDateCell.getCellType() == CellType.STRING){
                joiningDate = LocalDate.parse(joiningDateCell.getStringCellValue());
            }
            else{
                joiningDate = joiningDateCell.getLocalDateTimeCellValue().toLocalDate();
            }
            String role = row.getCell(8).getStringCellValue();
            double salary = row.getCell(9).getNumericCellValue();

            if(existingEmails.contains(email)
                    || existingPhones.contains(phoneNumber) || importedEmails.contains(email) || importedPhoneNumber.contains(phoneNumber)) {
                log.warn("Skipping duplicate employee - Email: {}, Phone: {}", email, phoneNumber);
                continue;
            }

            Employee employee = new Employee();
            employee.setName(name);
            employee.setEmail(email);
            employee.setAddress(address);
            employee.setPhoneNumber(phoneNumber);
            employee.setPassword(passwordEncoder.encode(password));
            employee.setDepartment(department);
            employee.setDesignation(designation);
            employee.setJoiningDate(joiningDate);
            employee.setRole(Role.valueOf(role.toUpperCase()));
            employee.setSalary(salary);
            employee.setActive(true);
            employee.setCreatedAt(Instant.now());
            employee.setUpdatedAt(Instant.now());
            importedEmails.add(email);
            importedPhoneNumber.add(phoneNumber);
            employees.add(employee);
        }
        employeeRepository.saveAll(employees);
        workbook.close();
        log.info("Successfully import {} Employees", employees.size());
    }
}
