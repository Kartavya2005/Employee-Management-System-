package com.Project.Ems.Repository;

import com.Project.Ems.Entity.Employee;
import com.Project.Ems.Enum.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Employee> findByEmail(String email);

    List<Employee> findByNameContainingIgnoreCase(String name);
    List<Employee> findByPhoneNumberContaining(String phoneNumber);
    List<Employee> findByEmailContainingIgnoreCase(String email);

    List<Employee> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingOrDepartmentIgnoreCaseOrAddressContainingIgnoreCase(
            String name,
            String email,
            String phoneNumber,
            String department,


            String address);
    //sort by name , salary, creadedAt, updatedAt,joiningDate
    List<Employee> findAll(Sort sort);

}
