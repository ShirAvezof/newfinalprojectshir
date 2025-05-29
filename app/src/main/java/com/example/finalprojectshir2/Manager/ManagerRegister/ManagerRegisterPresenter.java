package com.example.finalprojectshir2.Manager.ManagerRegister;

import com.example.finalprojectshir2.InputValidator;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Manager;
import com.example.finalprojectshir2.repositories.ManagerRepository;

public class ManagerRegisterPresenter {

    private ManagerRegisterView view;
    private ManagerRepository managerRepository;

    public ManagerRegisterPresenter(ManagerRegisterView view) {
        this.view = view;
        this.managerRepository = new ManagerRepository();
    }

    public void registerManager(String firstName, String lastName, String email, String password) {
        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty()) {
            view.showError("Please fill in both name fields");
            return;
        }

        // Validate email
        String emailError = InputValidator.validateEmail(email);
        if (!emailError.isEmpty()) {
            view.showEmailError(emailError);
            return;
        }

        // Validate password
        String passwordError = InputValidator.validatePassword(password);
        if (!passwordError.isEmpty()) {
            view.showPasswordError(passwordError);
            return;
        }

        // Show loading state
        view.setLoadingState(true);

        // Create manager object
        Manager manager = new Manager(firstName + " " + lastName, password, email);

        // Use repository to add manager
        managerRepository.addManager(manager, new FirebaseCallback<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                view.setLoadingState(false);
                view.onRegistrationSuccess(email, firstName + " " + lastName);
            }

            @Override
            public void onError(String error) {
                view.setLoadingState(false);
                view.showError("Registration failed: " + error);
            }
        });
    }

    // Interface for the View
    public interface ManagerRegisterView {
        void showError(String message);
        void showEmailError(String error);
        void showPasswordError(String error);
        void setLoadingState(boolean isLoading);
        void onRegistrationSuccess(String email, String fullName);
    }
}