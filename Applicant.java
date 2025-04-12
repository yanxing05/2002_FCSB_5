
package bto.system.models.users;


import bto.system.models.Application;
import bto.system.models.BTOProject;


public class Applicant extends User {
   private Application application;


   public Applicant(String name, String nric, String password, int age, String maritalStatus) {
       super(name, nric, password, age, maritalStatus);
   }


   public Application getApplication() {
       return application;
   }


   public void setApplication(Application application) {
       this.application = application;
   }


   @Override
   public String getUserType() {
       return "Applicant";
   }


   public boolean canApplyForProject(BTOProject project) {
       if (application != null && !application.getStatus().equals("Unsuccessful")) {
           return false; // Already has an active application
       }


       if (getMaritalStatus().equals("Single") && getAge() >= 35) {
           return project.getFlatTypes().stream().anyMatch(ft -> ft.getType().equals("2-Room"));
       } else if (getMaritalStatus().equals("Married") && getAge() >= 21) {
           return true;
       }
       return false;
   }


   public void withdrawApplication() {
       if (application != null) {
           application.setStatus("Withdrawn");
           application = null;
       }
   }
}

