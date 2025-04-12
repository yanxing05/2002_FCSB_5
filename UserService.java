package bto.system.services;


import bto.system.models.users.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserService {
   private List<User> users;


   public UserService() {
       this.users = new ArrayList<>();
   }


   public void addUser(User user) {
       users.add(user);
   }


   public void addAllUsers(List<? extends User> users) {
       this.users.addAll(users);
   }


   public User getUserByNric(String nric) {
       return users.stream()
               .filter(u -> u.getNric().equalsIgnoreCase(nric))
               .findFirst()
               .orElse(null);
   }


   public List<User> getAllUsers() {
       return new ArrayList<>(users);
   }


   public List<Applicant> getAllApplicants() {
       return users.stream()
               .filter(u -> u instanceof Applicant)
               .map(u -> (Applicant) u)
               .collect(Collectors.toList());
   }


   public List<HDBOfficer> getAllOfficers() {
       return users.stream()
               .filter(u -> u instanceof HDBOfficer)
               .map(u -> (HDBOfficer) u)
               .collect(Collectors.toList());
   }


   public List<HDBManager> getAllManagers() {
       return users.stream()
               .filter(u -> u instanceof HDBManager)
               .map(u -> (HDBManager) u)
               .collect(Collectors.toList());
   }


   public void updateUserPassword(User user, String newPassword) {
       user.setPassword(newPassword);
   }
}
