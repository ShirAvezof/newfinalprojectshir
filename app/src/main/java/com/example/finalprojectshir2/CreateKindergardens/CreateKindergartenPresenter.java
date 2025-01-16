package com.example.finalprojectshir2.CreateKindergardens;

import android.content.Context;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;

public class CreateKindergartenPresenter {

    private CreateKindergartenActivity activity;
    private KinderGartenRepository repository;

    public CreateKindergartenPresenter(Context context) {
        repository = new KinderGartenRepository();
        activity = new CreateKindergartenActivity();
    }
    public void submitKinderGarten(KinderGarten garten) {
            repository.addKinderGarden(garten, new FirebaseCallback<KinderGarten>() {
                @Override
                public void onSuccess(KinderGarten kinderGarten) {
                    activity.showSuccess(kinderGarten);
                }

                @Override
                public void onError(String error) {
                    activity.showError(error);
                }
            });
    }
}
