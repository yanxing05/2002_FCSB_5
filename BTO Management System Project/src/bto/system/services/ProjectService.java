package bto.system.services;

import bto.system.models.BTOProject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final List<BTOProject> projects;

    public ProjectService(List<BTOProject> projects) {
        this.projects = new ArrayList<>(projects);
    }

    public List<BTOProject> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public List<BTOProject> getVisibleProjects() {
        return projects.stream()
                .filter(BTOProject::isVisible)
                .collect(Collectors.toList());
    }

    public BTOProject getProjectById(String projectId) {
        return projects.stream()
                .filter(p -> p.getProjectId().equals(projectId))
                .findFirst()
                .orElse(null);
    }

    public boolean toggleVisibility(String projectId) {
        BTOProject project = getProjectById(projectId);
        if (project != null) {
            project.setVisible(!project.isVisible());
            return true;
        }
        return false;
    }

    public int getAvailableOfficerSlots(String projectId) {
        BTOProject project = getProjectById(projectId);
        return project != null ?
                project.getOfficerSlots() - project.getOfficers().size() :
                0;
    }

    public void addProject(BTOProject project) {
        projects.add(project);
    }
}