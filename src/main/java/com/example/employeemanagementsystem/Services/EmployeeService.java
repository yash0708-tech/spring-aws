package com.example.employeemanagementsystem.Services;

import com.example.employeemanagementsystem.API.APIResponse;
import com.example.employeemanagementsystem.Models.Department;
import com.example.employeemanagementsystem.Models.Employee;
import com.example.employeemanagementsystem.Models.Project;
import com.example.employeemanagementsystem.Repository.DepartmentRepository;
import com.example.employeemanagementsystem.Repository.EmployeeRepository;
import com.example.employeemanagementsystem.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeService(ProjectRepository projectRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public APIResponse<Employee> createEmployee(Employee employee) {
        try {
            Employee savedEmployee = employeeRepository.save(employee);
            return new APIResponse<>(savedEmployee, HttpStatus.CREATED, "Employee created successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), "Failed to create employee");
        }
    }

    public APIResponse<Employee> assignDepToEmployee(Long emp_id, Long dep_id) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(emp_id);
            Optional<Department> departmentOptional = departmentRepository.findById(dep_id);

            if (employeeOptional.isPresent() && departmentOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                Department department = departmentOptional.get();
                employee.setDepartment(department);
                Employee savedEmployee = employeeRepository.save(employee);
                return new APIResponse<>(savedEmployee, HttpStatus.OK, "Department assigned to employee successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee or department not found", "Employee or department with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to assign department to employee");
        }
    }

    public APIResponse<Employee> assignProjectToEmployee(Long empId, Long projectId) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(empId);
            Optional<Project> projectOptional = projectRepository.findById(projectId);

            if (employeeOptional.isPresent() && projectOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                Project project = projectOptional.get();
                employee.getProjects().add(project);
                Employee savedEmployee = employeeRepository.save(employee);
                return new APIResponse<>(savedEmployee, HttpStatus.OK, "Project assigned to employee successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee or project not found", "Employee or project with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to assign project to employee");
        }
    }

    public APIResponse<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return new APIResponse<>(employees, HttpStatus.OK, "All employees retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve employees");
        }
    }

    public APIResponse<Employee> getAnEmployee(Long id) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                return new APIResponse<>(employee, HttpStatus.OK, "Employee retrieved successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee not found", "Employee with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve employee");
        }
    }

    public APIResponse<Department> getDeptEmployee(Long empId) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(empId);
            if (employeeOptional.isPresent()) {
                Department department = employeeOptional.get().getDepartment();
                return new APIResponse<>(department, HttpStatus.OK, "Department retrieved successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee not found", "Employee with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve department");
        }
    }

    public APIResponse<List<Employee>> getEmployeesWithHighestSalary() {
        try {
            List<Employee> highestPaidEmployees = employeeRepository.findTop3ByOrderBySalaryDesc(); // Change to findTopN to get N highest salary holders
            return new APIResponse<>(highestPaidEmployees, HttpStatus.OK, "Highest salary holders retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve highest salary holders");
        }

    }

    public APIResponse<Map<String, Employee>> getSecondHighestSalaryHoldersByDepartment() {
        try {
            Map<String, Employee> secondHighestSalaryHoldersByDepartment = new HashMap<>();
            List<Department> departments = departmentRepository.findAll();

            for (Department department : departments) {
                List<Employee> employeesInDepartment = employeeRepository.findByDepartmentOrderBySalaryDesc(department);
                if (employeesInDepartment.size() > 1) {
                    secondHighestSalaryHoldersByDepartment.put(department.getName(), employeesInDepartment.get(1));
                }
            }
            return new APIResponse<>(secondHighestSalaryHoldersByDepartment, HttpStatus.OK, "Second highest salary holder by department retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve second highest salary holders by department");
        }
    }

    public APIResponse<Employee> updateEmployee(Long employeeId, Employee updatedEmployeeData) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                // Update employee data with the new values
                employee.setName(updatedEmployeeData.getName());
                employee.setSalary(updatedEmployeeData.getSalary());
                employee.setDepartment(updatedEmployeeData.getDepartment());
                employee.setAddress(updatedEmployeeData.getAddress());
                // Save the updated employee object
                Employee updatedEmployee = employeeRepository.save(employee);
                return new APIResponse<>(updatedEmployee, HttpStatus.OK, "Employee data updated successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee not found", "Employee with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to update employee data");
        }
    }

    public APIResponse<String> deleteEmployee(Long employeeId) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            if (optionalEmployee.isPresent()) {
                employeeRepository.deleteById(employeeId);
                return new APIResponse<>(HttpStatus.OK,"null" ,"Employee deleted successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Employee not found", "Employee with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to delete employee");
        }
    }

}
