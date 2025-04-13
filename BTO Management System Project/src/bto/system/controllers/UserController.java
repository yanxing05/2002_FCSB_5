package bto.system.controllers;

import bto.system.models.users.User;
import bto.system.services.AuthenticationService;
import bto.system.services.UserService;
import bto.system.exceptions.AuthenticationException;
import java.util.List;
import java.util.ArrayList;

public class UserController {
    private final AuthenticationService authService;
    private final UserService userService;

    public UserController(UserService userService) {
        this.authService = new AuthenticationService();
        this.userService = userService;
    }

    public User login(String nric, String password) throws AuthenticationException {
        User user = userService.getUserByNric(nric);
        if (user == null || !authService.authenticate(user, password)) {
            throw new AuthenticationException("Invalid NRIC or password");
        }
        return user;
    }

    public void changePassword(User user, String oldPassword, String newPassword)
            throws AuthenticationException {

        if (!authService.authenticate(user, oldPassword)) {
            throw new AuthenticationException("Current password is incorrect");
        }

        authService.validatePassword(newPassword);
        userService.updateUserPassword(user, newPassword);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<User> getUsersByType(String userType) {
        switch (userType.toLowerCase()) {
            case "applicant":
                return new ArrayList<>(userService.getAllApplicants());
            case "hdbofficer":
                return new ArrayList<>(userService.getAllOfficers());
            case "hdbmanager":
                return new ArrayList<>(userService.getAllManagers());
            default:
//                return userService.getAllUsers();
                return new ArrayList<>(userService.getAllUsers());
        }
    }
}