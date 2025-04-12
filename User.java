package bto.system.models.users;


import bto.system.models.Enquiry;
import java.util.ArrayList;
import java.util.List;


public abstract class User {
   private String nric;
   private String password;
   private int age;
   private String maritalStatus;
   private String name;
   private List<Enquiry> enquiries;


   public User(String name, String nric, String password, int age, String maritalStatus) {
       this.name = name;
       this.nric = nric;
       this.password = password;
       this.age = age;
       this.maritalStatus = maritalStatus;
       this.enquiries = new ArrayList<>();
   }


   // Getters and setters
   public String getName() { return name; }
   public String getNric() { return nric; }
   public String getPassword() { return password; }
   public int getAge() { return age; }
   public String getMaritalStatus() { return maritalStatus; }
   public List<Enquiry> getEnquiries() { return enquiries; }


   public void setPassword(String password) { this.password = password; }


   public abstract String getUserType();


   public boolean authenticate(String password) {
       return this.password.equals(password);
   }


   public void changePassword(String oldPassword, String newPassword) {
       if (authenticate(oldPassword)) {
           this.password = newPassword;
       }
   }


   public void addEnquiry(Enquiry enquiry) {
       enquiries.add(enquiry);
   }


   public void editEnquiry(int index, String newMessage) {
       if (index >= 0 && index < enquiries.size()) {
           enquiries.get(index).setMessage(newMessage);
       }
   }


   public void deleteEnquiry(int index) {
       if (index >= 0 && index < enquiries.size()) {
           enquiries.remove(index);
       }
   }
}
