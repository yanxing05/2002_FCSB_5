package bto.system.views;

import bto.system.models.users.Applicant;
import bto.system.controllers.ProjectController;
import bto.system.controllers.UserController;
import java.util.Scanner;

public class ApplicantView {
    private final UserController userController;
    private final ProjectController projectController;
    private final Scanner scanner;

    public ApplicantView(UserController userController,
                         ProjectController projectController,
                         Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.scanner = scanner;
    }

    public void displayMenu(Applicant applicant) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== Applicant Menu ===");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for Project");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewProjects(applicant);
                    break;
                case 0:
                    logout = true;
                    break;
            }
        }
    }

    private void viewProjects(Applicant applicant) {
        projectController.getAllProjects().stream()
                .filter(p -> p.isVisible() && applicant.canApplyForProject(p))
                .forEach(p -> System.out.println(p.getName()));
    }
}
