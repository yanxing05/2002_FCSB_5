package bto.system.services;

import bto.system.models.users.User;
import bto.system.exceptions.AuthenticationException;
import bto.system.utils.NRICValidator;
import bto.system.utils.InputValidator;

public class AuthenticationService {
    public boolean authenticate(User user, String password) throws AuthenticationException {
        // Validate NRIC format
        if (!NRICValidator.isValidNRIC(user.getNric())) {
            throw new AuthenticationException("Invalid NRIC format");
        }

        // Validate password
        if (!InputValidator.isValidPassword(password)) {
            throw new AuthenticationException("Password must be at least 8 characters");
        }
        if (!user.authenticate(password)) {
            throw new AuthenticationException("Incorrect password");
        }

        return true; // Actual authentication happens in UserController
    }

    public void validatePassword(String password) throws AuthenticationException {
        if (password == null || password.length() < 8) {
            throw new AuthenticationException("Password must be at least 8 characters");
        }
    }
}
