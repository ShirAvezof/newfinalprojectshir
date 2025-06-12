package com.example.finalprojectshir2.Manager.ManagerProfile;

import android.content.Context;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Manager;
import com.example.finalprojectshir2.repositories.ManagerRepository;


public class ManagerProfilePresenter {
    private ManagerProfileActivity view;
    private ManagerRepository repository;
    private Context context;

    public ManagerProfilePresenter(ManagerProfileActivity view, Context context) {
        this.view = view;
        this.context = context;
        this.repository = new ManagerRepository();
    }


// Loads the manager's details from the repository

    public void loadManagerDetails() {
        view.showLoading();


        String managerId = repository.getCurrentManagerId();
        if (managerId == null || managerId.isEmpty()) {
            view.showError("מזהה מנהל לא נמצא");
            view.hideLoading();
            return;
        }

        // Create a Manager object with just the ID to retrieve details
        Manager manager = new Manager();
        manager.setId(managerId);

        repository.getManager(manager, new FirebaseCallback<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                view.hideLoading();
                view.displayManagerDetails(result);
            }

            @Override
            public void onError(String error) {
                view.hideLoading();
                view.showError("שגיאה בטעינת פרטי המנהל: " + error);
            }
        });
    }


    // Updates the manager's profile information

    public void updateManagerProfile(String name) {
        view.showLoading();

        String managerId = repository.getCurrentManagerId();
        if (managerId == null || managerId.isEmpty()) {
            view.showError("מזהה מנהל לא נמצא");
            view.hideLoading();
            return;
        }

        // Create a Manager object with the updated information
        Manager manager = new Manager();
        manager.setId(managerId);
        manager.setManagerName(name);

        repository.updateManagerName(manager, new FirebaseCallback<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                view.hideLoading();
                view.onProfileUpdateSuccess(result.getManagerName());
            }

            @Override
            public void onError(String error) {
                view.hideLoading();
                view.showError("שגיאה בעדכון פרופיל: " + error);
            }
        });
    }


    /**
     * Called when the presenter is no longer needed
     */
    public void onDestroy() {
        this.view = null;
    }
}