package com.example.finalprojectshir2;

public class InputValidator {

    public static String validateEmail(String email) {
        // Check if email is null or empty
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty.";
        }

        // Check if there's exactly one '@' symbol
        int atIndex = email.indexOf('@');
        if (atIndex == -1 || email.indexOf('@', atIndex + 1) != -1) {
            return "Email must contain exactly one '@' symbol.";
        }

        // Ensure there's at least one character before '@'
        if (atIndex == 0) {
            return "Email must contain characters before '@'.";
        }

        // Ensure there's at least one character after '@' and a '.' in the domain part
        String domain = email.substring(atIndex + 1);
        int dotIndex = domain.indexOf('.');
        if (dotIndex == -1 || dotIndex == domain.length() - 1) {
            return "Em..";
        }

        // Ensure the domain part is not empty
        if (domain.isEmpty()) {
            return "Email domain cannot be empty.";
        }

        // Ensure no spaces in the email
        if (email.contains(" ")) {
            return "Email cannot contain spaces.";
        }

        // If all checks pass, return an empty string (valid email)
        return "";
    }

    public static String validatePassword(String password) {
        // Check if password is null or empty

        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }

        // Check password length (example: minimum 6 characters)
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }


        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }


        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one digit.";
        }

        // Ensure password contains at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password must contain at least one special character.";
        }
        // If all checks pass, return an empty string (valid password)
        return "";
    }
    public static String validateField (String field) {
        if (field == null || field.isEmpty()) {
            return "field cannot be empty";
        }
        return "";
    }
}


