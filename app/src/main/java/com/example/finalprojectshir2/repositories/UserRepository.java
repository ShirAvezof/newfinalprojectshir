package com.example.finalprojectshir2.repositories;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserRepository {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public UserRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
    }
    public void addUser(User user, FirebaseCallback<User> callback) {
        auth.createUserWithEmailAndPassword(user.getUserEmail(), user.getUserPass()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                user.setId(userId);

                database.collection("users").document(userId).set(user).addOnCompleteListener(task1  -> {
                    if(task1.isSuccessful()) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError(Objects.requireNonNull(task1.getException()).getMessage());
                    }
                });
            } else {
                callback.onError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }
    public void loginUser(String email, String password, FirebaseCallback<User> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                database.collection("users").document(userId).get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult().exists()) {
                        User user = task1.getResult().toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User not found in Firestore.");
                    }
                });
            } else {
                callback.onError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }



    public void getUser(User user, FirebaseCallback<User> callback) {

    }

    public void deleteUser(User user, FirebaseCallback<User> callback) {

    }
    public void updateUser(User user, FirebaseCallback<User> callback) {

    }
}
