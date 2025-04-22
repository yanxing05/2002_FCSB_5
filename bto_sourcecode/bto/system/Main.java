

package bto.system;


import bto.system.services.*;
        import bto.system.controllers.*;
        import bto.system.views.*;
        import bto.system.models.*;
        import bto.system.models.users.*;
        import bto.system.exceptions.*;
        import java.util.*;

public class Main {


    private static final String APPLICANT_FILE = "bt/src/bto/system/data/ApplicantList.csv";
    private static final String OFFICER_FILE = "bt/src/bto/system/data/OfficerList.csv";
    private static final String MANAGER_FILE = "bt/src/bto/system/data/ManagerList.csv";
    private static final String PROJECT_FILE = "bt/src/bto/system/data/ProjectList.csv";

    public static void main(String[] args) {
        try {
            // Initialize services
        	CSVFileService excelFileService = new CSVFileService();
            UserDataLoader userDataLoader = new UserDataLoader(excelFileService);
            UserService userService = new UserService();
            OfficerService officerService = new OfficerService();
            ApplicationService applicationService = new ApplicationService();

            // Load users from Excel files
            List<Applicant> applicants = userDataLoader.loadApplicants(APPLICANT_FILE);
            List<HDBOfficer> officers = userDataLoader.loadOfficers(OFFICER_FILE);
            List<HDBManager> managers = userDataLoader.loadManagers(MANAGER_FILE);

            // Add all users to UserService
            userService.addAllUsers(applicants);
            userService.addAllUsers(officers);
            userService.addAllUsers(managers);

            // Load projects
            ProjectDataLoader projectDataLoader = new ProjectDataLoader(excelFileService, userService, officerService, applicationService);
            List<BTOProject> projects = projectDataLoader.loadProjects(PROJECT_FILE);
            String APPLICATION_FILE = "bt/src/bto/system/data/ApplicationList.csv";
            projectDataLoader.loadApplications(APPLICATION_FILE, projects);

            // Initialize all services
            ProjectService projectService = new ProjectService(projects);
            EnquiryService enquiryService = new EnquiryService();
            //OfficerService officerService = new OfficerService();
            ReportService reportService = new ReportService();
            //NotificationService notificationService = new NotificationService();

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
                    excelFileService,
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
