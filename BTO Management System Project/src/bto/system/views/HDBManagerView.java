// package bto.system.views;

// import bto.system.controllers.*;
// import bto.system.models.*;
// import bto.system.models.users.HDBManager;
// import java.util.List;
// import java.util.Scanner;

// public class HDBManagerView {
//     private final ProjectController projectController;
//     private final UserController userController;
//     private final ApplicationController applicationController;
//     private final Scanner scanner;

//     public HDBManagerView(UserController userController,
//                           ProjectController projectController,
//                           ApplicationController applicationController,
//                           Scanner scanner) {
//         this.userController = userController;
//         this.projectController = projectController;
//         this.applicationController = applicationController;
//         this.scanner = scanner;
//     }
//     public HDBManagerView(UserController userController, ProjectController projectController,
// 		            	  ApplicationController applicationController, OfficerController officerController,
// 		            	  EnquiryController enquiryController, Scanner scanner) {
// 		this.userController = userController;
// 		this.projectController = projectController;
// 		this.applicationController = applicationController;
// 		this.officerController = officerController;
// 		this.enquiryController = enquiryController;
// 		this.scanner = scanner;
// 	}


//     public void displayMenu(HDBManager manager) {
//         boolean logout = false;

//         while (!logout) {
//             System.out.println("\n=== HDB Manager Dashboard ===");
//             System.out.println("1. Create New Project");
//             System.out.println("2. View My Projects");
//             System.out.println("3. Toggle Project Visibility");
//             System.out.println("4. Review Applications");
//             System.out.println("5. Manage Officers");
//             System.out.println("6. View All Enquiries");
//             System.out.println("0. Logout");
//             System.out.print("Enter your choice: ");

//             int choice = scanner.nextInt();
//             scanner.nextLine(); // Consume newline

//             switch (choice) {
//                 case 1:
//                     createProject(manager);
//                     break;
//                 case 2:
//                     viewManagedProjects(manager);
//                     break;
//                 case 3:
//                     toggleProjectVisibility(manager);
//                     break;
//                 case 4:
//                     reviewApplications(manager);
//                     break;
//                 case 5:
//                     manageOfficers(manager);
//                     break;
//                 case 6:
//                     viewEnquiries(manager);
//                     break;
//                 case 0:
//                     logout = true;
//                     break;
//                 default:
//                     System.out.println("Invalid choice!");
//             }
//         }
//     }

//     private void createProject(HDBManager manager) {
//         System.out.println("\n--- Create New BTO Project ---");

//         System.out.print("Project Name: ");
//         String name = scanner.nextLine();

//         System.out.print("Neighborhood: ");
//         String neighborhood = scanner.nextLine();

//         System.out.print("Flat Types (comma separated e.g. 2-Room,3-Room): ");
//         String[] flatTypes = scanner.nextLine().split(",");

//         System.out.print("2-Room Units: ");
//         int twoRoom = scanner.nextInt();

//         System.out.print("3-Room Units: ");
//         int threeRoom = scanner.nextInt();
//         scanner.nextLine(); // Consume newline

//         System.out.print("Opening Date (yyyy-MM-dd HH:mm:ss): ");
//         String openingDate = scanner.nextLine();

//         System.out.print("Closing Date (yyyy-MM-dd HH:mm:ss): ");
//         String closingDate = scanner.nextLine();

//         BTOProject project = manager.createProject(
//                 name,
//                 neighborhood,
//                 List.of(flatTypes),
//                 twoRoom,
//                 threeRoom,
//                 openingDate,
//                 closingDate
//         );

//         System.out.println("Project created successfully! ID: " + project.getProjectId());
//     }

//     private void viewManagedProjects(HDBManager manager) {
//         System.out.println("\n--- Your Managed Projects ---");
//         manager.getManagedProjects().forEach(project -> {
//             System.out.println("\n" + project.getName() +
//                     " (" + project.getNeighborhood() + ")");
//             System.out.println("Status: " + (project.isVisible() ? "VISIBLE" : "HIDDEN"));
//             System.out.println("Applications: " + project.getApplications().size());
//         });
//     }

//     private void toggleProjectVisibility(HDBManager manager) {
//         System.out.println("\n--- Toggle Project Visibility ---");
//         manager.getManagedProjects().forEach(p ->
//                 System.out.println(p.getProjectId() + ": " + p.getName()));

//         System.out.print("Enter Project ID: ");
//         String projectId = scanner.nextLine();

//         BTOProject project = manager.getManagedProjects().stream()
//                 .filter(p -> p.getProjectId().equals(projectId))
//                 .findFirst()
//                 .orElse(null);

//         if (project != null) {
//             project.setVisible(!project.isVisible());
//             System.out.println("Visibility set to: " + project.isVisible());
//         } else {
//             System.out.println("Project not found!");
//         }
//     }

//     private void reviewApplications(HDBManager manager) {
//         System.out.println("\n--- Application Review ---");
//         manager.getManagedProjects().forEach(project -> {
//             System.out.println("\nProject: " + project.getName());
//             project.getApplications().forEach(app -> {
//                 System.out.println("\nApplication ID: " + app.getApplicationId());
//                 System.out.println("Applicant: " + app.getApplicant().getName());
//                 System.out.println("Status: " + app.getStatus());

//                 if (app.getStatus().equals("Pending")) {
//                     System.out.print("Approve? (Y/N): ");
//                     String decision = scanner.nextLine();

//                     if (decision.equalsIgnoreCase("Y")) {
//                         manager.approveApplication(app);
//                         System.out.println("Application approved!");
//                     } else {
//                         manager.rejectApplication(app);
//                         System.out.println("Application rejected.");
//                     }
//                 }
//             });
//         });
//     }

//     private void manageOfficers(HDBManager manager) {
//         System.out.println("\n--- Officer Management ---");
//         System.out.println("1. View Registered Officers");
//         System.out.println("2. Approve/Reject Officers");
//         System.out.print("Choice: ");
//         int choice = scanner.nextInt();
//         scanner.nextLine();

//         if (choice == 1) {
//             manager.getManagedProjects().forEach(project -> {
//                 System.out.println("\nProject: " + project.getName());
//                 project.getOfficers().forEach(officer -> {
//                     System.out.println("- " + officer.getName() +
//                             " (" + officer.getRegistrationStatus() + ")");
//                 });
//             });
//         } else if (choice == 2) {
//             System.out.print("Enter Officer NRIC: ");
//             String nric = scanner.nextLine();

//             HDBOfficer officer = (HDBOfficer) userController.getUserByNric(nric);
//             if (officer != null) {
//                 System.out.print("Approve? (Y/N): ");
//                 String decision = scanner.nextLine();

//                 if (decision.equalsIgnoreCase("Y")) {
//                     manager.approveOfficerRegistration(officer);
//                     System.out.println("Officer approved!");
//                 } else {
//                     manager.rejectOfficerRegistration(officer);
//                     System.out.println("Officer rejected.");
//                 }
//             } else {
//                 System.out.println("Officer not found!");
//             }
//         }
//     }

//     private void viewEnquiries(HDBManager manager) {
//         System.out.println("\n--- Project Enquiries ---");
//         manager.getManagedProjects().forEach(project -> {
//             System.out.println("\nProject: " + project.getName());
//             project.getEnquiries().forEach(enquiry -> {
//                 System.out.println("\nEnquiry ID: " + enquiry.getEnquiryId());
//                 System.out.println("From: " + enquiry.getUser().getName());
//                 System.out.println("Message: " + enquiry.getMessage());
//                 System.out.println("Status: " +
//                         (enquiry.isResolved() ? "Resolved" : "Pending"));

//                 if (!enquiry.isResolved()) {
//                     System.out.print("Reply (leave blank to skip): ");
//                     String reply = scanner.nextLine();

//                     if (!reply.isBlank()) {
//                         enquiry.setReply(reply);
//                         enquiry.markAsResolved();
//                         System.out.println("Reply sent!");
//                     }
//                 }
//             });
//         });
//     }
// }

package bto.system.views;

import bto.system.controllers.*;
import bto.system.models.*;
import bto.system.models.users.HDBManager;
import java.util.List;
import java.util.Scanner;
import bto.system.models.users.HDBOfficer;
import bto.system.controllers.OfficerController;
import bto.system.controllers.EnquiryController;

public class HDBManagerView {
    private final ProjectController projectController;
    private final UserController userController;
    private final ApplicationController applicationController;
    private final OfficerController officerController;
    private final EnquiryController enquiryController;
    private final Scanner scanner;

//    public HDBManagerView(UserController userController,
//                          ProjectController projectController,
//                          ApplicationController applicationController,
//                          Scanner scanner) {
//        this.userController = userController;
//        this.projectController = projectController;
//        this.applicationController = applicationController;
//        this.scanner = scanner;
//    }
    public HDBManagerView(UserController userController, ProjectController projectController,
                          ApplicationController applicationController, OfficerController officerController,
                          EnquiryController enquiryController, Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.officerController = officerController;
        this.enquiryController = enquiryController;
        this.scanner = scanner;
    }


    public void displayMenu(HDBManager manager) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n=== HDB Manager Dashboard ===");
            System.out.println("1. Create New Project");
            System.out.println("2. View My Projects");
            System.out.println("3. Toggle Project Visibility");
            System.out.println("4. Review Applications");
            System.out.println("5. Manage Officers");
            System.out.println("6. View All Enquiries");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createProject(manager);
                    break;
                case 2:
                    viewManagedProjects(manager);
                    break;
                case 3:
                    toggleProjectVisibility(manager);
                    break;
                case 4:
                    reviewApplications(manager);
                    break;
                case 5:
                    manageOfficers(manager);
                    break;
                case 6:
                    viewEnquiries(manager);
                    break;
                case 0:
                    logout = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void createProject(HDBManager manager) {
        System.out.println("\n--- Create New BTO Project ---");

        System.out.print("Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Neighborhood: ");
        String neighborhood = scanner.nextLine();

        System.out.print("Flat Types (comma separated e.g. 2-Room,3-Room): ");
        String[] flatTypes = scanner.nextLine().split(",");

        System.out.print("2-Room Units: ");
        int twoRoom = scanner.nextInt();

        System.out.print("3-Room Units: ");
        int threeRoom = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Opening Date (yyyy-MM-dd HH:mm:ss): ");
        String openingDate = scanner.nextLine();

        System.out.print("Closing Date (yyyy-MM-dd HH:mm:ss): ");
        String closingDate = scanner.nextLine();

        BTOProject project = manager.createProject(
                name,
                neighborhood,
                List.of(flatTypes),
                twoRoom,
                threeRoom,
                openingDate,
                closingDate
        );

        System.out.println("Project created successfully! ID: " + project.getProjectId());
    }

    private void viewManagedProjects(HDBManager manager) {
        System.out.println("\n--- Your Managed Projects ---");
        manager.getManagedProjects().forEach(project -> {
            System.out.println("\n" + project.getName() +
                    " (" + project.getNeighborhood() + ")");
            System.out.println("Status: " + (project.isVisible() ? "VISIBLE" : "HIDDEN"));
            System.out.println("Applications: " + project.getApplications().size());
        });
    }

    private void toggleProjectVisibility(HDBManager manager) {
        System.out.println("\n--- Toggle Project Visibility ---");
        manager.getManagedProjects().forEach(p ->
                System.out.println(p.getProjectId() + ": " + p.getName()));

        System.out.print("Enter Project ID: ");
        String projectId = scanner.nextLine();

        BTOProject project = manager.getManagedProjects().stream()
                .filter(p -> p.getProjectId().equals(projectId))
                .findFirst()
                .orElse(null);

        if (project != null) {
            project.setVisible(!project.isVisible());
            System.out.println("Visibility set to: " + project.isVisible());
        } else {
            System.out.println("Project not found!");
        }
    }

    private void reviewApplications(HDBManager manager) {
        System.out.println("\n--- Application Review ---");
        manager.getManagedProjects().forEach(project -> {
            System.out.println("\nProject: " + project.getName());
            project.getApplications().forEach(app -> {
                System.out.println("\nApplication ID: " + app.getApplicationId());
                System.out.println("Applicant: " + app.getApplicant().getName());
                System.out.println("Status: " + app.getStatus());

                if (app.getStatus().equals("Pending")) {
                    System.out.print("Approve? (Y/N): ");
                    String decision = scanner.nextLine();

                    if (decision.equalsIgnoreCase("Y")) {
                        manager.approveApplication(app);
                        System.out.println("Application approved!");
                    } else {
                        manager.rejectApplication(app);
                        System.out.println("Application rejected.");
                    }
                }
            });
        });
    }

    private void manageOfficers(HDBManager manager) {
        System.out.println("\n--- Officer Management ---");
        System.out.println("1. View Registered Officers");
        System.out.println("2. Approve/Reject Officers");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            manager.getManagedProjects().forEach(project -> {
                System.out.println("\nProject: " + project.getName());
                project.getOfficers().forEach(officer -> {
                    System.out.println("- " + officer.getName() +
                            " (" + officer.getRegistrationStatus() + ")");
                });
            });
        } else if (choice == 2) {
            System.out.print("Enter Officer NRIC: ");
            String nric = scanner.nextLine();

            HDBOfficer officer = (HDBOfficer) userController.getUserByNric(nric);
            if (officer != null) {
                System.out.print("Approve? (Y/N): ");
                String decision = scanner.nextLine();

                if (decision.equalsIgnoreCase("Y")) {
                    manager.approveOfficerRegistration(officer);
                    System.out.println("Officer approved!");
                } else {
                    manager.rejectOfficerRegistration(officer);
                    System.out.println("Officer rejected.");
                }
            } else {
                System.out.println("Officer not found!");
            }
        }
    }

    private void viewEnquiries(HDBManager manager) {
        System.out.println("\n--- Project Enquiries ---");
        manager.getManagedProjects().forEach(project -> {
            System.out.println("\nProject: " + project.getName());
            project.getEnquiries().forEach(enquiry -> {
                System.out.println("\nEnquiry ID: " + enquiry.getEnquiryId());
                System.out.println("From: " + enquiry.getUser().getName());
                System.out.println("Message: " + enquiry.getMessage());
                System.out.println("Status: " +
                        (enquiry.isResolved() ? "Resolved" : "Pending"));

                if (!enquiry.isResolved()) {
                    System.out.print("Reply (leave blank to skip): ");
                    String reply = scanner.nextLine();

                    if (!reply.isBlank()) {
                        enquiry.setReply(reply);
                        enquiry.markAsResolved();
                        System.out.println("Reply sent!");
                    }
                }
            });
        });
    }
}
