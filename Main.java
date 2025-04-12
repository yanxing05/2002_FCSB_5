package bto.system;


import bto.system.services.*;
import bto.system.controllers.UserController;
import bto.system.controllers.ProjectController;
import bto.system.views.MainView;


public class Main {
   public static void main(String[] args) {
       try {
           // Initialize services
           ExcelFileService excelFileService = new ExcelFileService();
           UserDataLoader userDataLoader = new UserDataLoader(excelFileService);
           UserService userService = new UserService();


           // Load users from Excel files
           List<User> applicants = userDataLoader.loadApplicants("data/ApplicantList.xlsx");
           List<HDBOfficer> officers = userDataLoader.loadOfficers("data/OfficerList.xlsx");
           List<HDBManager> managers = userDataLoader.loadManagers("data/ManagerList.xlsx");


           // Add all users to UserService
           userService.addAllUsers(applicants);
           userService.addAllUsers(officers);
           userService.addAllUsers(managers);


           // Load projects
           ProjectDataLoader projectDataLoader = new ProjectDataLoader(excelFileService, userService);
           List<BTOProject> projects = projectDataLoader.loadProjects("data/ProjectList.xlsx");


           // Initialize controllers with loaded data
           UserController userController = new UserController(userService);
           ProjectController projectController = new ProjectController(new ProjectService(projects));


           // Start the application
           MainView mainView = new MainView(userController, projectController);
           mainView.display();


       } catch (Exception e) {
           System.err.println("Failed to initialize application: " + e.getMessage());
           e.printStackTrace();
       }
   }
}
