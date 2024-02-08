package com.example.employeemanagementsystem.Repository;

import com.example.employeemanagementsystem.Models.Project;
import com.example.employeemanagementsystem.Models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByStatus(Status status);
}
