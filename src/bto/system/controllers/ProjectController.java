package bto.system.controllers;

import bto.system.models.BTOProject;
import bto.system.services.ProjectService;
import java.util.List;

public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<BTOProject> getAllProjects() {
        return projectService.getAllProjects();
    }

    public List<BTOProject> getVisibleProjects() {
        return projectService.getVisibleProjects();
    }

    public BTOProject getProjectById(String projectId) {
        return projectService.getProjectById(projectId);
    }

    public boolean toggleProjectVisibility(String projectId) {
        return projectService.toggleVisibility(projectId);
    }

    public int getAvailableSlots(String projectId) {
        return projectService.getAvailableOfficerSlots(projectId);
    }
}