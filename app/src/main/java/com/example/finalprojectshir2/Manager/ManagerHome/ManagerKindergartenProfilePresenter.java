package com.example.finalprojectshir2.Manager.ManagerHome;

import android.util.Log;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;

public class ManagerKindergartenProfilePresenter {
    private static final String TAG = "MgrKGProfilePresenter";
    private ManagerKindergartenProfile view;
    private KinderGartenRepository repository;
    private String kindergartenId;

    public ManagerKindergartenProfilePresenter(ManagerKindergartenProfile view) {
        this.view = view;
        this.repository = new KinderGartenRepository();
        Log.d(TAG, "Manager Profile Presenter initialized");
    }

    public void loadKindergartenDetails(String kindergartenId) {
        if (kindergartenId == null || kindergartenId.isEmpty()) {
            Log.e(TAG, "Invalid kindergarten ID");
            view.showError("Invalid kindergarten ID");
            return;
        }

        this.kindergartenId = kindergartenId;
        Log.d(TAG, "Loading details for kindergarten ID: " + kindergartenId);

        view.showLoading();
        repository.getKinderGartenById(kindergartenId, new FirebaseCallback<KinderGarten>() {
            @Override
            public void onSuccess(KinderGarten result) {
                Log.d(TAG, "Load successful: " + (result != null ? result.getGanname() : "null"));
                if (view != null) {
                    view.hideLoading();
                    if (result == null) {
                        view.showError("Kindergarten not found");
                    } else {
                        view.displayKindergartenDetails(result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Load error: " + error);
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }

    public void saveKindergartenDetails(KinderGarten kindergarten) {
        if (kindergarten == null || kindergarten.getId() == null || kindergarten.getId().isEmpty()) {
            Log.e(TAG, "Invalid kindergarten data for saving");
            if (view != null) {
                view.onSaveError("Invalid kindergarten data");
            }
            return;
        }

        Log.d(TAG, "Saving details for kindergarten: " + kindergarten.getGanname());

        repository.updateKinderGarten(kindergarten, new FirebaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.d(TAG, "Save successful");
                if (view != null) {
                    view.onSaveSuccess();
                    loadKindergartenDetails(kindergarten.getId());
                }
            }
            @Override
            public void onError(String error) {
                Log.e(TAG, "Save error: " + error);
                if (view != null) {
                    view.onSaveError(error);
                }
            }
        });
    }

    public void onDestroy() {
        this.view = null;
    }
}