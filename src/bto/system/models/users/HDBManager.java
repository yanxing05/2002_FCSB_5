package bto.system.models.users;

import bto.system.models.BTOProject;
import bto.system.models.Application;
import java.util.ArrayList;
import java.util.List;

public class HDBManager extends User {
    private List<BTOProject> managedProjects;
    private BTOProject currentProject;

    public HDBManager(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }

    public List<BTOProject> getManagedProjects() {
        return managedProjects;
    }

    public BTOProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(BTOProject project) {
        this.currentProject = project;
    }

    @Override
    public String getUserType() {
        return "HDBManager";
    }

    public BTOProject createProject(String name, String neighborhood, List<String> flatTypes,
                                    int twoRoomCount, int threeRoomCount,
                                    String openingDate, String closingDate) {
        BTOProject project = new BTOProject(name, neighborhood, flatTypes,
                twoRoomCount, threeRoomCount,
                openingDate, closingDate, this);
        managedProjects.add(project);
        currentProject = project;
        return project;
    }

    public void editProject(BTOProject project, String name, String neighborhood,
                            List<String> flatTypes, int twoRoomCount, int threeRoomCount,
                            String openingDate, String closingDate) {
        project.setName(name);
        project.setNeighborhood(neighborhood);
        project.setFlatTypes(flatTypes);
        project.setTwoRoomCount(twoRoomCount);
        project.setThreeRoomCount(threeRoomCount);
        project.setOpeningDate(openingDate);
        project.setClosingDate(closingDate);
    }

    public void deleteProject(BTOProject project) {
        managedProjects.remove(project);
        if (currentProject != null && currentProject.equals(project)) {
            currentProject = null;
        }
    }

    public void approveOfficerRegistration(HDBOfficer officer) {
        if (currentProject != null && currentProject.getOfficers().size() < currentProject.getOfficerSlots()) {
            officer.setAssignedProject(currentProject);
            officer.setRegistrationStatus("Approved");
            currentProject.addOfficer(officer);
        }
    }

    public void rejectOfficerRegistration(HDBOfficer officer) {
        officer.setRegistrationStatus("Rejected");
    }

    public void approveApplication(Application application) {
        if (currentProject != null && currentProject.equals(application.getProject())) {
            application.setStatus("Successful");
        }
    }

    public void rejectApplication(Application application) {
        if (currentProject != null && currentProject.equals(application.getProject())) {
            application.setStatus("Unsuccessful");
        }
    }
}
