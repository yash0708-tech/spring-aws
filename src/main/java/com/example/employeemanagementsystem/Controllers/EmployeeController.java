package com.example.employeemanagementsystem.Controllers;

import com.example.employeemanagementsystem.API.APIResponse;
import com.example.employeemanagementsystem.Models.Department;
import com.example.employeemanagementsystem.Models.Employee;
import com.example.employeemanagementsystem.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public APIResponse<Employee> createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("{emp_id}/department/{dep_id}")
    public APIResponse<Employee> assignDepToEmployee(@PathVariable Long emp_id, @PathVariable Long dep_id) {
        return employeeService.assignDepToEmployee(emp_id, dep_id);
    }

    @PutMapping("{emp_id}/project/{project_id}")
    public APIResponse<Employee> assignProjectToEmployee(@PathVariable Long emp_id, @PathVariable Long project_id) {
        return employeeService.assignProjectToEmployee(emp_id, project_id);
    }

    @PutMapping("/update/{employeeId}")
    public APIResponse<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee updatedEmployeeData) {
        return employeeService.updateEmployee(employeeId,updatedEmployeeData);
    }

    @GetMapping("/getall")
    public APIResponse<List<Employee>> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/get/{emp_id}")
    public APIResponse<Employee> getAnEmployee(@PathVariable Long emp_id) {
        return employeeService.getAnEmployee(emp_id);
    }

    @GetMapping("/get/department/{emp_id}")
    public APIResponse<Department> getDeptEmployee(@PathVariable Long emp_id) {
        return employeeService.getDeptEmployee(emp_id);
    }

    @GetMapping("/highest-salary")
    public APIResponse<List<Employee>> getEmployeesWithHighestSalary() {
        return employeeService.getEmployeesWithHighestSalary();
    }

    @GetMapping("/second-highest-salary-by-department")
    public APIResponse<Map<String, Employee>> getSecondHighestSalaryHoldersByDepartment() {
        return employeeService.getSecondHighestSalaryHoldersByDepartment();
    }

    @DeleteMapping("/delete/{employeeId}")
    public APIResponse<String> deleteEmployee(@PathVariable Long employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }
}
