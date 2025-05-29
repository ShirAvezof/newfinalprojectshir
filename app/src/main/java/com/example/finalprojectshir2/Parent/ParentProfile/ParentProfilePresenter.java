package com.example.finalprojectshir2.Parent.ParentProfile;

import android.content.Context;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.User;
import com.example.finalprojectshir2.repositories.UserRepository;

public class ParentProfilePresenter {
    private ParentProfileView view;
    private UserRepository userRepository;
    private User currentUser;

    public ParentProfilePresenter(ParentProfileView view) {
        this.view = view;
        this.userRepository = new UserRepository();
    }

    public void loadUserProfile() {
        String userId = userRepository.getCurrentUserId();
        if (userId != null) {
            view.showLoading();
            userRepository.getUserById(userId, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    currentUser = user;
                    view.hideLoading();
                    view.displayUserProfile(user);
                }

                @Override
                public void onError(String errorMessage) {
                    view.hideLoading();
                    view.showError("Failed to load profile: " + errorMessage);
                }
            });
        } else {
            view.showError("User not logged in");
        }
    }

    public void updateUserName(String newName, Context context) {
        if (currentUser != null) {
            view.showLoading();
            currentUser.setUserName(newName);

            userRepository.updateUser(currentUser, new FirebaseCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    view.hideLoading();
                    view.updateProfileSuccess("Profile updated successfully");
                    view.displayUserProfile(user);
                }

                @Override
                public void onError(String errorMessage) {
                    view.hideLoading();
                    view.showError("Failed to update profile: " + errorMessage);
                }
            });
        } else {
            view.showError("No user data available");
        }
    }

    public void logoutUser() {
        userRepository.logoutUser();
        view.navigateToLogin();
    }

    // Interface for the view
    public interface ParentProfileView {
        void showLoading();
        void hideLoading();
        void displayUserProfile(User user);
        void showError(String message);
        void updateProfileSuccess(String message);
        void navigateToLogin();
    }
}