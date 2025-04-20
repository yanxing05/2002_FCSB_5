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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//2nd
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// package bto.system.views;

// import bto.system.controllers.*;
// import bto.system.models.*;
// import bto.system.models.users.HDBManager;
// import java.util.List;
// import java.util.Scanner;
// import bto.system.models.users.HDBOfficer;
// import bto.system.controllers.OfficerController;
// import bto.system.controllers.EnquiryController;

// public class HDBManagerView {
//     private final ProjectController projectController;
//     private final UserController userController;
//     private final ApplicationController applicationController;
//     private final OfficerController officerController;
//     private final EnquiryController enquiryController;
//     private final Scanner scanner;

// //    public HDBManagerView(UserController userController,
// //                          ProjectController projectController,
// //                          ApplicationController applicationController,
// //                          Scanner scanner) {
// //        this.userController = userController;
// //        this.projectController = projectController;
// //        this.applicationController = applicationController;
// //        this.scanner = scanner;
// //    }
//     public HDBManagerView(UserController userController, ProjectController projectController,
//                           ApplicationController applicationController, OfficerController officerController,
//                           EnquiryController enquiryController, Scanner scanner) {
//         this.userController = userController;
//         this.projectController = projectController;
//         this.applicationController = applicationController;
//         this.officerController = officerController;
//         this.enquiryController = enquiryController;
//         this.scanner = scanner;
//     }


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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//3rd
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package bto.system.views;

import bto.system.controllers.*;
import bto.system.models.*;
import bto.system.models.users.Applicant;
import bto.system.models.users.HDBManager;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            System.out.println("2. View All Projects");
            System.out.println("3. Filter Projects");
            System.out.println("4. View My Projects");
            System.out.println("5. Toggle Project Visibility");
            System.out.println("6. Review Applications");
            System.out.println("7. Manage Officers");
            System.out.println("8. View All Enquiries");
            System.out.println("9. Edit Project");
            System.out.println("10. Delete Project");
            System.out.println("11. Change Password");
            System.out.println("12. Process Withdrawal Request");
            System.out.println("13. Handle Report");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createProject(manager);
                    break;
                case 2:
                	viewProjects(manager);
                	break;
                case 3:
                	filterProjects(manager);
                	break;
                case 4:
                    viewManagedProjects(manager);
                    break;
                case 5:
                    toggleProjectVisibility(manager);
                    break;
                case 6:
                    reviewApplications(manager);
                    break;
                case 7:
                    manageOfficers(manager);
                    break;
                case 8:
                    viewEnquiries(manager);
                    break;
                case 9:
                    editProject(manager);
                    break;
                case 10:
                    deleteProject(manager);
                    break;
                case 11:
                	if (changePassword(manager)) {
                        System.out.println("Password changed successfully. Please login again.");
                        logout = true;
                    } else {
                        System.out.println("Password change failed.");
                    }
                    break;
                case 12:
                	handleWithdrawalRequests(manager);
                	break;
                case 13:
                	reportGenerationMenu(manager);
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

        System.out.print("Opening Date (DD/MM/YYYY): ");
        String openingDate = scanner.nextLine();

        System.out.print("Closing Date (DD/MM/YYYY): ");
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
        projectController.addProject(project);

        System.out.println("Project created successfully! ID: " + project.getProjectId());
    }
    private void viewProjects(HDBManager manager) {
        System.out.println("\n--- All Projects ---");
        List<BTOProject> available = projectController.getAllProjects();

        if (available.isEmpty()) {
            System.out.println("No projects found.");
        } else {
            available.forEach(p -> System.out.println("- " + p.getName()));
        }
    }
    private void filterProjects(HDBManager manager) {
        System.out.println("\n=== Filter Projects ===");
        System.out.println("1. By Neighborhood");
        System.out.println("2. By Flat Type");
        System.out.println("3. By Visibility");
        System.out.println("4. Currently Open for Applications");
        System.out.println("5. My Projects Currently Open");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        List<BTOProject> projectsToFilter = projectController.getAllProjects();
        List<BTOProject> filtered = new ArrayList<>();
        
        switch(choice) {
            case 1:
                System.out.print("Enter neighborhood: ");
                String area = scanner.nextLine();
                filtered = projectsToFilter.stream()
                        .filter(p -> p.getNeighborhood().equalsIgnoreCase(area))
                        .collect(Collectors.toList());
                break;
            case 2:
                System.out.print("Enter flat type (2-Room/3-Room): ");
                String type = scanner.nextLine();
                filtered = projectsToFilter.stream()
                        .filter(p -> p.getFlatTypes().stream()
                                .anyMatch(ft -> ft.getType().equalsIgnoreCase(type)))
                        .collect(Collectors.toList());
                break;
            case 3:
                System.out.print("Show visible only? (Y/N): ");
                String input = scanner.nextLine().trim();

                boolean visible;
                if (input.equalsIgnoreCase("Y")) {
                    visible = true;
                } else if (input.equalsIgnoreCase("N")) {
                    visible = false;
                } else {
                    System.out.println("Invalid input. Showing all projects by default.");
                    visible = true; // or false, depending on your default behavior
                }

                filtered = projectsToFilter.stream()
                        .filter(p -> p.isVisible() == visible)
                        .collect(Collectors.toList());
                break;

            case 4:
                LocalDate today = LocalDate.now();
                filtered = projectsToFilter.stream()
                        .filter(p -> !today.isBefore(p.getOpeningDate()) && 
                                   !today.isAfter(p.getClosingDate()))
                        .collect(Collectors.toList());
                break;
            case 5:
                filtered = manager.getProjectsInApplicationPeriod();
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }
        
        if (filtered.isEmpty()) {
            System.out.println("No projects match the criteria.");
        } else {
            printProjectTable(filtered);
        }
    }
    private void viewManagedProjects(HDBManager manager) {
        System.out.println("\n--- Your Managed Projects ---");
        manager.getManagedProjects().forEach(project -> {
            System.out.println("\n" + project.getName() +
                    " (" + project.getNeighborhood() + ")");
            System.out.println("Status: " + (project.isVisible() ? "VISIBLE" : "HIDDEN"));
            System.out.println("Applications: " + project.getApplications().size());
            System.out.println("Flat Types: " + project.getFlatTypes());
            System.out.println("2-Room Units: " + project.getTwoRoomCount());
            System.out.println("3-Room Units: " + project.getThreeRoomCount());
            System.out.println("Opening Date: " + project.getOpeningDate());
            System.out.println("Closing Date: " + project.getClosingDate());
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

                if (app.getStatus().equals("Pending") && app.getProject().equals(manager.getCurrentProject())) {
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
            // Gather pending officers
        	List<HDBOfficer> pendingOfficers = userController.getAllUsers().stream()
                    .filter(user -> user instanceof HDBOfficer)
                    .map(user -> (HDBOfficer) user)
                    .filter(officer -> 
                        "Pending".equalsIgnoreCase(officer.getRegistrationStatus()) &&
                        manager.getCurrentProject().equals(officer.getAssignedProject()))
                    .toList();

            if (pendingOfficers.isEmpty()) {
                System.out.println("No officers with pending registration.");
                return;
            }

            // Display officers
            System.out.println("\nPending Officer Registrations of Current Project" + "(" + manager.getCurrentProject().getName() + "): ");
            for (int i = 0; i < pendingOfficers.size(); i++) {
                HDBOfficer officer = pendingOfficers.get(i);
                System.out.println((i + 1) + ". " + officer.getName() + " - NRIC: " + officer.getNric());
            }

            System.out.print("Select officer to review (0 to cancel): ");
            int index = scanner.nextInt();
            scanner.nextLine();

            if (index <= 0 || index > pendingOfficers.size()) {
                System.out.println("Cancelled or invalid selection.");
                return;
            }

            HDBOfficer selectedOfficer = pendingOfficers.get(index - 1);
            System.out.print("Approve officer '" + selectedOfficer.getName() + "'? (Y/N): ");
            String decision = scanner.nextLine();

            if (decision.equalsIgnoreCase("Y")) {
                manager.approveOfficerRegistration(selectedOfficer);
                System.out.println("Officer approved!");
            } else {
                manager.rejectOfficerRegistration(selectedOfficer);
                System.out.println("Officer rejected.");
            }
        }
    }


    private void viewEnquiries(HDBManager manager) {
    	List<Enquiry> enquiries = enquiryController.getAllEnquiries().stream()
                .filter(e -> !e.isResolved())
                .toList();

        if (enquiries.isEmpty()) {
            System.out.println("No unresolved enquiries found in the system.");
            return;
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== All Unresolved Enquiries in the System ===");
            List<Enquiry> active_enquiries = enquiryController.getAllEnquiries().stream()
                    .filter(e -> !e.isResolved())
                    .toList();
            for (int i = 0; i < active_enquiries.size(); i++) {
                Enquiry enquiry = active_enquiries.get(i);
                System.out.println((i + 1) + ". [" + enquiry.getEnquiryId() + "] Project: "
                    + enquiry.getProject().getName() + " | " + enquiry.getMessage());
            }

            System.out.println("\nChoose an action:");
            System.out.println("1. Reply to an enquiry");
            System.out.println("2. Exit");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter the number of the enquiry you want to reply to: ");
                    int enquiryIndex;
                    try {
                        enquiryIndex = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    if (enquiryIndex < 1 || enquiryIndex > enquiries.size()) {
                        System.out.println("Invalid number. Please select a valid enquiry.");
                        continue;
                    }

                    Enquiry selectedEnquiry = active_enquiries.get(enquiryIndex - 1);
                    System.out.print("Enter your reply: ");
                    String reply = scanner.nextLine();
                    enquiryController.replyToEnquiry(selectedEnquiry, reply);
                    System.out.println("Reply sent.");
                    break;

                case 2:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void editProject(HDBManager manager) {
        System.out.println("\n--- Edit Project ---");
        List<BTOProject> projects = manager.getManagedProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects available to edit.");
            return;
        }

        // Display project list
        for (int i = 0; i < projects.size(); i++) {
            BTOProject p = projects.get(i);
            System.out.printf("%d. %s (%s)%n", i+1, p.getName(), p.getNeighborhood());
        }

        System.out.print("Select project to edit (number): ");
        int projectIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (projectIndex < 0 || projectIndex >= projects.size()) {
            System.out.println("Invalid project selection.");
            return;
        }

        BTOProject project = projects.get(projectIndex);

        // Display current values and prompt for new ones
        System.out.println("\nCurrent Project Details:");
        System.out.println("1. Name: " + project.getName());
        System.out.println("2. Neighborhood: " + project.getNeighborhood());
        System.out.println("3. Flat Types: " + project.getFlatTypes());
        System.out.println("4. 2-Room Units: " + project.getTwoRoomCount());
        System.out.println("5. 3-Room Units: " + project.getThreeRoomCount());
        System.out.println("6. Opening Date: " + project.getOpeningDate());
        System.out.println("7. Closing Date: " + project.getClosingDate());

        System.out.print("\nEnter field number to edit (1-7) or 0 to save: ");
        int fieldChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        while (fieldChoice != 0) {
            switch (fieldChoice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    project.setName(name);
                    break;
                case 2:
                    System.out.print("Enter new neighborhood: ");
                    String neighborhood = scanner.nextLine();
                    project.setNeighborhood(neighborhood);
                    break;
                case 3:
                    System.out.print("Enter new flat types (comma separated): ");
                    String[] flatTypes = scanner.nextLine().split(",");
                    project.setFlatTypes(List.of(flatTypes));
                    break;
                case 4:
                    System.out.print("Enter new 2-room count: ");
                    int twoRoom = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    project.setTwoRoomCount(twoRoom);
                    break;
                case 5:
                    System.out.print("Enter new 3-room count: ");
                    int threeRoom = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    project.setThreeRoomCount(threeRoom);
                    break;
                case 6:
                    System.out.print("Enter new opening date (DD/MM/YYYY): ");
                    String openingDate = scanner.nextLine();
                    project.setOpeningDate(openingDate);
                    break;
                case 7:
                    System.out.print("Enter new closing date (DD/MM/YYYY): ");
                    String closingDate = scanner.nextLine();
                    project.setClosingDate(closingDate);
                    break;
                default:
                    System.out.println("Invalid field choice!");
            }

            System.out.print("\nEnter another field number to edit (1-7) or 0 to save: ");
            fieldChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }

        System.out.println("Project updated successfully!");
    }

    private void deleteProject(HDBManager manager) {
        System.out.println("\n--- Delete Project ---");
        List<BTOProject> projects = manager.getManagedProjects();

        if (projects.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }

        // Display project list
        for (int i = 0; i < projects.size(); i++) {
            BTOProject p = projects.get(i);
            System.out.printf("%d. %s (%s)%n", i+1, p.getName(), p.getNeighborhood());
        }

        System.out.print("Select project to delete (number): ");
        int projectIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (projectIndex < 0 || projectIndex >= projects.size()) {
            System.out.println("Invalid project selection.");
            return;
        }

        BTOProject project = projects.get(projectIndex);

        // Confirmation
        System.out.print("Are you sure you want to delete " + project.getName() + "? (Y/N): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            // Step 3: Remove the project from all officers who are assigned to it
            List<HDBOfficer> officers = officerController.getRegisteredOfficers();
            for (HDBOfficer officer : officers) {
                if (officer.getAssignedProject() != null && officer.getAssignedProject().equals(project)) {
                    officer.setAssignedProject(null); 
                    officer.setRegistrationStatus("None");// Unassign the project from the officer
                    System.out.println("Officer " + officer.getName() + " has been unassigned from the project.");
                }
            }

            // Now, delete the project
            manager.deleteProject(project);
            projectController.removeProject(project);
            System.out.println("Project deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private boolean changePassword(HDBManager manager) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter new password (min 8 characters): ");
        String newPassword = scanner.nextLine();

        try {
            manager.changePassword(oldPassword, newPassword);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    private void printProjectTable(List<BTOProject> projects) {
        System.out.printf("%-15s %-20s %-15s %-12s %-10s %-15s %-10s%n",
                "ID", "Name", "Neighborhood", "Flat Types", "Units", "Status", "Visible");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        
        projects.forEach(p -> {
            String flatTypes = p.getFlatTypes().stream()
                    .map(ft -> ft.getType())
                    .collect(Collectors.joining("/"));
            
            String units = p.getFlatTypes().stream()
                    .map(ft -> String.valueOf(ft.getAvailableUnits()))
                    .collect(Collectors.joining("/"));
            
            String status = p.isAcceptingApplications() ? "OPEN" : "CLOSED";
            String visible = p.isVisible() ? "Yes" : "No";
            
            System.out.printf("%-15s %-20s %-15s %-12s %-10s %-15s %-10s%n",
                    p.getProjectId().substring(0, 7), // Show abbreviated ID
                    p.getName(),
                    p.getNeighborhood(),
                    flatTypes,
                    units,
                    status,
                    visible);
        });
    }
    private void handleWithdrawalRequests(HDBManager manager) {
    	List<Application> withdrawalRequests = manager.getCurrentProject().getApplications().stream()
                .filter(a -> "Pending Withdrawal".equals(a.getStatus()))
                .collect(Collectors.toList());

        
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        
        System.out.println("\n=== Pending Withdrawal Requests ===");
        withdrawalRequests.forEach(req -> {
            System.out.printf("%s - %s (%s) %s%n",
                    req.getApplicant().getName(),
                    req.getProject().getName(),
                    req.getApplicationDate(),
                    req.getApplicantNric());
        });
        
        System.out.print("Enter applicant NRIC to process: ");
        String nric = scanner.nextLine();
        
        Application request = withdrawalRequests.stream()
                .filter(r -> r.getApplicant().getNric().equals(nric))
                .findFirst()
                .orElse(null);
        
        if (request != null) {
            System.out.print("Approve withdrawal? (Y/N): ");
            String decision = scanner.nextLine();
            
            if (decision.equalsIgnoreCase("Y")) {
                manager.processWithdrawalRequest(request, true);
                System.out.println("Withdrawal approved.");
            } else {
                manager.processWithdrawalRequest(request, false);
                System.out.println("Withdrawal rejected.");
            }
        } else {
            System.out.println("Request not found.");
        }
    }
    private void reportGenerationMenu(HDBManager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Report Generation ===");
            System.out.println("1. All Booked Applications");
            System.out.println("2. Married Applicants Report");
            System.out.println("3. Flat Type Preference Report");
            System.out.println("4. Project Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    generateAllBookingsReport(manager);
                    break;
                case 2:
                    generateMarriedApplicantsReport(manager);
                    break;
                case 3:
                    generateFlatTypeReport(manager);
                    break;
                case 4:
                    generateProjectStatistics(manager);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void generateAllBookingsReport(HDBManager manager) {
        System.out.println("\n=== All Booked Applications Report ===");
        List<Application> bookedApps = manager.getBookedApplications();
        
        if (bookedApps.isEmpty()) {
            System.out.println("No booked applications found.");
            return;
        }
        
        bookedApps.forEach(app -> {
            System.out.println("\nApplication ID: " + app.getApplicationId());
            System.out.println("Applicant: " + app.getApplicant().getName());
            System.out.println("Project: " + app.getProject().getName());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Booking Date: " + app.getApprovalDate());
        });
    }

    private void generateMarriedApplicantsReport(HDBManager manager) {
        System.out.println("\n=== Married Applicants Report ===");
        List<Application> marriedApps = manager.getApplicationsByMaritalStatus("Married");
        
        if (marriedApps.isEmpty()) {
            System.out.println("No applications from married applicants.");
            return;
        }
        
        marriedApps.forEach(app -> {
            System.out.println("\nApplicant: " + app.getApplicant().getName());
            System.out.println("Age: " + app.getApplicant().getAge());
            System.out.println("Project: " + app.getProject().getName());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Status: " + app.getStatus());
        });
    }

    private void generateFlatTypeReport(HDBManager manager) {
        System.out.println("\n=== Flat Type Preference Report ===");
        System.out.println("1. 2-Room Preferences");
        System.out.println("2. 3-Room Preferences");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        String flatType = (choice == 1) ? "2-Room" : "3-Room";
        List<Application> typeApps = manager.getApplicationsByFlatType(flatType);
        
        if (typeApps.isEmpty()) {
            System.out.println("No applications for " + flatType + " flats.");
            return;
        }
        
        System.out.println("\n=== " + flatType + " Applications ===");
        typeApps.forEach(app -> {
            System.out.println("\nApplicant: " + app.getApplicant().getName());
            System.out.println("Marital Status: " + app.getApplicant().getMaritalStatus());
            System.out.println("Project: " + app.getProject().getName());
            System.out.println("Status: " + app.getStatus());
        });
    }
    private void generateProjectStatistics(HDBManager manager) {
        System.out.println("\n=== Project Statistics ===");
        manager.getManagedProjects().forEach(project -> {
            System.out.println("\nProject: " + project.getName());
            System.out.println("Total Applications: " + project.getApplications().size());

            long booked = project.getApplications().stream()
                    .filter(a -> "Booked".equals(a.getStatus()))
                    .count();
            System.out.println("Booked Units: " + booked);

            project.getFlatTypes().forEach(ft -> {
                System.out.println(ft.getType() + " - Available: " + 
                        ft.getAvailableUnits() + "/" + ft.getTotalUnits());
            });
        });
    }

}
