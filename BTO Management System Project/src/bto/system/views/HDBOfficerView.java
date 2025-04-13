package bto.system.views;

import bto.system.models.users.HDBOfficer;
import java.util.Scanner;
import bto.system.controllers.ProjectController;
import bto.system.controllers.UserController;

public class HDBOfficerView {
    private final UserController userController;
    private final ProjectController projectController;
    private final Scanner scanner;

    public HDBOfficerView(UserController userController,
                          ProjectController projectController,
                          Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.scanner = scanner;
    }

    public void displayMenu(HDBOfficer officer) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== HDB Officer Menu ===");
            System.out.println("1. View Assigned Project");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAssignedProject(officer);
                    break;
                case 0:
                    logout = true;
                    break;
            }
        }
    }

    private void viewAssignedProject(HDBOfficer officer) {
        if (officer.getAssignedProject() != null) {
            System.out.println(officer.getAssignedProject().getName());
        }
    }
}
