package com.example.employeemanagementsystem.Services;

import com.example.employeemanagementsystem.API.APIResponse;
import com.example.employeemanagementsystem.DTOs.ProjectDTO;
import com.example.employeemanagementsystem.Models.Employee;
import com.example.employeemanagementsystem.Models.Project;
import com.example.employeemanagementsystem.Models.Status;
import com.example.employeemanagementsystem.Repository.EmployeeRepository;
import com.example.employeemanagementsystem.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;



    public APIResponse<Project> createProject(Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return new APIResponse<>(savedProject, HttpStatus.CREATED, "Project created successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), "Failed to create project");
        }
    }

    public APIResponse<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return new APIResponse<>(projects, HttpStatus.OK, "All projects retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve projects");
        }
    }

    public APIResponse<List<ProjectDTO>> getAllProjectsWithTeamMembers() {
        try {
            List<Project> projects = projectRepository.findAll();
            List<ProjectDTO> projectDTOs = projects.stream().map(ProjectDTO::new).collect(Collectors.toList());
            return new APIResponse<>(projectDTOs, HttpStatus.OK, "All projects with team members retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve projects with team members");
        }
    }

    public APIResponse<ProjectDTO> getAllDetailsById(Long projId) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(projId);
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                ProjectDTO projectDTO = new ProjectDTO(project);
                return new APIResponse<>(projectDTO, HttpStatus.OK, "Project details retrieved successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Project not found", "Project with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve project details");
        }
    }

    public APIResponse<String> updateProjectStatus(Long projectId, Status newStatus) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectId);
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                project.setStatus(newStatus);
                projectRepository.save(project);
                return new APIResponse<>(HttpStatus.OK, "Success", "Project status updated successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Error", "Project with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Failed to update project status: " + e.getMessage());
        }
    }

    public APIResponse<Double> getProjectBudget(Long projectId) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectId);
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                double totalSalary = project.getTeam().stream()
                        .mapToDouble(Employee::getSalary)
                        .sum();
                return new APIResponse<>(totalSalary, HttpStatus.OK, "Project budget calculated successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Project not found", "Project with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to calculate project budget");
        }
    }

    public APIResponse<List<Project>> getNewProjects() {
        try {
            List<Project> newProjects = projectRepository.findByStatus(Status.NEW);
            return new APIResponse<>(newProjects, HttpStatus.OK, "New projects list retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve new projects list");
        }
    }
    public APIResponse<List<Project>> getOngoingProjects() {
        try {
            List<Project> newProjects = projectRepository.findByStatus(Status.ON_GOING);
            return new APIResponse<>(newProjects, HttpStatus.OK, "Ongoing projects list retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve new projects list");
        }
    }
    public APIResponse<List<Project>> getEndProjects() {
        try {
            List<Project> newProjects = projectRepository.findByStatus(Status.ENDED);
            return new APIResponse<>(newProjects, HttpStatus.OK, "Ongoing projects list retrieved successfully");
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to retrieve new projects list");
        }
    }

    public APIResponse<String> deleteProject(Long projectId) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectId);
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                List<Employee> employees = project.getTeam();
                for (Employee employee : employees) {
                    // Reassign the employee to another project or set project to null
                    employee.getProjects().remove(project); // Remove project from employee's projects
                    employeeRepository.save(employee); // Update employee in the database
                }
                // Then delete the project itself
                projectRepository.deleteById(projectId);
                return new APIResponse<>(HttpStatus.OK, "Success", "Project deleted successfully");
            } else {
                return new APIResponse<>(HttpStatus.NOT_FOUND, "Error", "Project with the given ID not found");
            }
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Failed to delete project: " + e.getMessage());
        }
    }
}
