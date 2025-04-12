package bto.system.views;


import bto.system.controllers.UserController;
import bto.system.controllers.ProjectController;
import bto.system.models.users.*;
import java.util.Scanner;


public class MainView {
   private final UserController userController;
   private final ProjectController projectController;
   private final Scanner scanner;
   private final LoginView loginView;
   private ApplicantView applicantView;
   private HDBOfficerView officerView;
   private HDBManagerView managerView;


   public MainView(UserController userController, ProjectController projectController) {
       this.userController = userController;
       this.projectController = projectController;
       this.scanner = new Scanner(System.in);
       this.loginView = new LoginView(userController, scanner);
   }


   public void display() {
       boolean exit = false;


       while (!exit) {
           System.out.println("\n=== BTO Management System ===");
           System.out.println("1. Login");
           System.out.println("2. Exit");
           System.out.print("Enter your choice: ");


           int choice = scanner.nextInt();
           scanner.nextLine(); // Consume newline


           switch (choice) {
               case 1:
                   handleLogin();
                   break;
               case 2:
                   exit = true;
                   System.out.println("Exiting system...");
                   break;
               default:
                   System.out.println("Invalid choice. Please try again.");
           }
       }
   }


   private void handleLogin() {
       try {
           User user = loginView.displayLogin();


           if (user != null) {
               switch (user.getUserType()) {
                   case "Applicant":
                       applicantView = new ApplicantView(userController, projectController, scanner);
                       applicantView.displayMenu((Applicant) user);
                       break;
                   case "HDBOfficer":
                       officerView = new HDBOfficerView(userController, projectController, scanner);
                       officerView.displayMenu((HDBOfficer) user);
                       break;
                   case "HDBManager":
                       managerView = new HDBManagerView(userController, projectController, scanner);
                       managerView.displayMenu((HDBManager) user);
                       break;
               }
           }
       } catch (Exception e) {
           System.out.println("Error: " + e.getMessage());
       }
   }
}
