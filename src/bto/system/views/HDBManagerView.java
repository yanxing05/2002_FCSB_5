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
            System.out.println("7. Edit Project");
            System.out.println("8. Delete Project");
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
                case 7:
                    editProject(manager);
                    break;
                case 8:
                    deleteProject(manager);
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
                    System.out.print("Enter new opening date (yyyy-MM-dd HH:mm:ss): ");
                    String openingDate = scanner.nextLine();
                    project.setOpeningDate(openingDate);
                    break;
                case 7:
                    System.out.print("Enter new closing date (yyyy-MM-dd HH:mm:ss): ");
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
            manager.deleteProject(project);
            System.out.println("Project deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
