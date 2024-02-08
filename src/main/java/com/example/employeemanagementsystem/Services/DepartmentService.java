package com.example.employeemanagementsystem.Services;

import com.example.employeemanagementsystem.API.APIResponse;
import com.example.employeemanagementsystem.DTOs.DepartmentDTO;
import com.example.employeemanagementsystem.Models.Department;
import com.example.employeemanagementsystem.Models.Employee;
import com.example.employeemanagementsystem.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public APIResponse<Department> createDepartment(Department department) {
        try {
            Department savedDepartment = departmentRepository.save(department);
            return new APIResponse<>(savedDepartment, HttpStatus.CREATED, "Department created successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), "Failed to create department");
        }
    }

    public APIResponse<List<Department>> getAllDepartments() {
        try {
            List<Department> departments = departmentRepository.findAll();
            return new APIResponse<>(departments, HttpStatus.OK, "All departments retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve departments");
        }
    }

    public APIResponse<List<DepartmentDTO>> getAllDepartmentsWithEmployees() {
        try {
            List<Department> departments = departmentRepository.findAll();
            List<DepartmentDTO> departmentDTOs = departments.stream().map(DepartmentDTO::new).collect(Collectors.toList());
            return new APIResponse<>(departmentDTOs, HttpStatus.OK, "All departments retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve departments");
        }
    }

    public APIResponse<Map<String, Double>> getTotalSalaryByDepartment() {
        try {
            Map<String, Double> totalSalaryByDepartment = new HashMap<>();
            List<Department> departments = departmentRepository.findAll();

            for (Department department : departments) {
                String departmentName = department.getName();
                List<Employee> employeesInDepartment = department.getEmployees();
                double totalSalary = employeesInDepartment.stream()
                        .mapToDouble(Employee::getSalary)
                        .sum();
                totalSalaryByDepartment.put(departmentName, totalSalary);
            }
            return new APIResponse<>(totalSalaryByDepartment, HttpStatus.OK, "Total salary by department retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve total salary by department");
        }
    }

    public APIResponse<Department> updateDepartment(Long departmentId, Department updatedDepartmentData) {
        try {
            Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                // Update department data with the new values
                department.setName(updatedDepartmentData.getName());
                // Save the updated department object
                Department updatedDepartment = departmentRepository.save(department);
                return new APIResponse<>(updatedDepartment, HttpStatus.OK, "Department data updated successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Department not found", "Department with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to update department data");
        }
    }

    public APIResponse<String> deleteDepartment(Long departmentId) {
        try {
            Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                List<Employee> employeesInDepartment = department.getEmployees();
                if (!employeesInDepartment.isEmpty()) {
                    return new APIResponse<>(HttpStatus.BAD_REQUEST, "Error", "Cannot delete department because there are employees associated with it");
                }
                departmentRepository.deleteById(departmentId);
                return new APIResponse<>(HttpStatus.OK, "Success", "Department deleted successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Error", "Department with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Failed to delete department: " + e.getMessage());
        }
    }
}
