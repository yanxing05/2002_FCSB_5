//package bto.system;
//
//import bto.system.services.*;
//import bto.system.controllers.UserController;
//import bto.system.controllers.ProjectController;
//import bto.system.views.MainView;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            // Initialize services
//            ExcelFileService excelFileService = new ExcelFileService();
//            UserDataLoader userDataLoader = new UserDataLoader(excelFileService);
//            UserService userService = new UserService();
//
//            // Load users from Excel files
//            List<User> applicants = userDataLoader.loadApplicants("data/ApplicantList.xlsx");
//            List<HDBOfficer> officers = userDataLoader.loadOfficers("data/OfficerList.xlsx");
//            List<HDBManager> managers = userDataLoader.loadManagers("data/ManagerList.xlsx");
//
//            // Add all users to UserService
//            userService.addAllUsers(applicants);
//            userService.addAllUsers(officers);
//            userService.addAllUsers(managers);
//
//            // Load projects
//            ProjectDataLoader projectDataLoader = new ProjectDataLoader(excelFileService, userService);
//            List<BTOProject> projects = projectDataLoader.loadProjects("data/ProjectList.xlsx");
//
//            // Initialize controllers with loaded data
//            UserController userController = new UserController(userService);
//            ProjectController projectController = new ProjectController(new ProjectService(projects));
//
//            // Start the application
//            MainView mainView = new MainView(userController, projectController);
//            mainView.display();
//
//        } catch (Exception e) {
//            System.err.println("Failed to initialize application: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}


package bto.system;


import bto.system.services.*;
        import bto.system.controllers.*;
        import bto.system.views.*;
        import bto.system.models.*;
        import bto.system.models.users.*;
        import bto.system.exceptions.*;
        import java.util.*;

public class Main {
    private static final String APPLICANT_FILE = "data/ApplicantList.xlsx";
    private static final String OFFICER_FILE = "data/OfficerList.xlsx";
    private static final String MANAGER_FILE = "data/ManagerList.xlsx";
    private static final String PROJECT_FILE = "data/ProjectList.xlsx";

    public static void main(String[] args) {
        try {
            // Initialize services
            ExcelFileService excelFileService = new ExcelFileService();
            UserDataLoader userDataLoader = new UserDataLoader(excelFileService);
            UserService userService = new UserService();

            // Load users from Excel files
            List<Applicant> applicants = userDataLoader.loadApplicants(APPLICANT_FILE);
            List<HDBOfficer> officers = userDataLoader.loadOfficers(OFFICER_FILE);
            List<HDBManager> managers = userDataLoader.loadManagers(MANAGER_FILE);

            // Add all users to UserService
            userService.addAllUsers(applicants);
            userService.addAllUsers(officers);
            userService.addAllUsers(managers);

            // Load projects
            ProjectDataLoader projectDataLoader = new ProjectDataLoader(excelFileService, userService);
            List<BTOProject> projects = projectDataLoader.loadProjects(PROJECT_FILE);

            // Initialize all services
            ProjectService projectService = new ProjectService(projects);
            ApplicationService applicationService = new ApplicationService();
            EnquiryService enquiryService = new EnquiryService();
            OfficerService officerService = new OfficerService();
            ReportService reportService = new ReportService();
            NotificationService notificationService = new NotificationService();

            // Initialize controllers
            UserController userController = new UserController(userService);
            ProjectController projectController = new ProjectController(projectService);
            ApplicationController applicationController = new ApplicationController(applicationService);
            EnquiryController enquiryController = new EnquiryController(enquiryService);
            OfficerController officerController = new OfficerController(officerService);

            // Start the application
            Scanner scanner = new Scanner(System.in);
            MainView mainView = new MainView(
                    userController,
                    projectController,
                    applicationController,
                    enquiryController,
                    officerController,
                    scanner
            );

            mainView.display();

        } catch (FileException e) {
            System.err.println("File error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
