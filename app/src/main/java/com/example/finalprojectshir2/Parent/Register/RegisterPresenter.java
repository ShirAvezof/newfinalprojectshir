package com.example.finalprojectshir2.Parent.Register;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.repositories.UserRepository;
import com.example.finalprojectshir2.models.User;

public class RegisterPresenter {
    private UserRepository userDb;
    private RegisterActivity activity;
    public RegisterPresenter(RegisterActivity activity) {
        userDb = new UserRepository();
        this.activity = activity;
    }
    public void submitUser(User user) {
        userDb.addUser(user, new FirebaseCallback<User>() {
            @Override
            public void onError(String message) {
                activity.showError(message);
            }

            @Override
            public void onSuccess(User user) {
                activity.onUserSuccess(user);
            }
        });
    }
}
