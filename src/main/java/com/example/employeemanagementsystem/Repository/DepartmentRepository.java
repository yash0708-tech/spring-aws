package com.example.employeemanagementsystem.Repository;

import com.example.employeemanagementsystem.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

