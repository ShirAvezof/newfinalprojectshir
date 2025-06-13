package com.example.finalprojectshir2.repositories;

import android.content.Context;
import android.widget.Toast;

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

    public void loginUser(String email, String password, FirebaseCallback<User> callback, Context context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
//                Toast.makeText(context, userId, Toast.LENGTH_SHORT).show();


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

    public String getCurrentUserId() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        }
        return null;
    }

    public void getUserById(String userId, FirebaseCallback<User> callback) {
        database.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        User user = task.getResult().toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "User not found";
                        callback.onError(errorMsg);
                    }
                });
    }

    public void updateUser(User user, FirebaseCallback<User> callback) {
        if (user == null || user.getId() == null) {
            callback.onError("Invalid user data");
            return;
        }

        database.collection("users").document(user.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(user);
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Update failed";
                        callback.onError(errorMsg);
                    }
                });
    }



    public void logoutUser() {
        auth.signOut();
    }
}