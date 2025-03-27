package com.example.finalprojectshir2.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ManagerRepository {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public ManagerRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
    }

    public void addManager(Manager manager, FirebaseCallback<Manager> callback) {
        auth.createUserWithEmailAndPassword(manager.getManagerEmail(), manager.getManagerPass())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String managerId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        manager.setId(managerId);
                        database.collection("managers").document(managerId).set(manager)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        callback.onSuccess(manager);
                                    } else {
                                        callback.onError(Objects.requireNonNull(task1.getException()).getMessage());
                                    }
                                });
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void loginManager(String email, String password, FirebaseCallback<Manager> callback, Context context) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String managerId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        Toast.makeText(context, managerId, Toast.LENGTH_SHORT).show();

                        database.collection("managers").document(managerId).get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult().exists()) {
                                        Manager manager = task1.getResult().toObject(Manager.class);
                                        callback.onSuccess(manager);
                                    } else {
                                        callback.onError("Manager not found in Firestore.");
                                    }
                                });
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void getManager(Manager manager, FirebaseCallback<Manager> callback) {
        String managerId = manager.getId();
        database.collection("managers").document(managerId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Manager fetchedManager = task.getResult().toObject(Manager.class);
                        callback.onSuccess(fetchedManager);
                    } else {
                        callback.onError("Failed to fetch manager data");
                    }
                });
    }

    public void deleteManager(Manager manager, FirebaseCallback<Manager> callback) {
        String managerId = manager.getId();
        database.collection("managers").document(managerId).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(manager);
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void updateManager(Manager manager, FirebaseCallback<Manager> callback) {
        String managerId = manager.getId();
        database.collection("managers").document(managerId).set(manager)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(manager);
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }
}