package com.example.employeemanagementsystem.DTOs;


import com.example.employeemanagementsystem.Models.Project;
import com.example.employeemanagementsystem.Models.Employee;

import com.example.employeemanagementsystem.Models.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private List<String> teamNames; // Assuming you want just the names of team members
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.teamNames = project.getTeam().stream().map(Employee::getName).toList();
        this.status = project.getStatus();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
    }
}
