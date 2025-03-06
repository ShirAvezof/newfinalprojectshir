package com.example.finalprojectshir2.repositories;

import android.util.Log;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class KinderGartenRepository {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    private static final String TAG = "KGRepository";
    private static final String COLLECTION_NAME = "kinderGartens";

    public KinderGartenRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
    }

    public void getKinderGartenById(String kindergartenId, FirebaseCallback<KinderGarten> callback) {
        Log.d(TAG, "Getting kindergarten with ID: " + kindergartenId);

        if (kindergartenId != null && !kindergartenId.trim().isEmpty()) {
            database.collection(COLLECTION_NAME)
                    .document(kindergartenId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document != null && document.exists()) {
                                KinderGarten kg = document.toObject(KinderGarten.class);
                                if (kg != null) {
                                    kg.setId(document.getId());
                                    Log.d(TAG, "Retrieved kindergarten: " + kg.getGanname());
                                    callback.onSuccess(kg);
                                } else {
                                    Log.e(TAG, "Failed to convert document to KinderGarten object");
                                    callback.onError("Error parsing kindergarten data");
                                }
                            } else {
                                Log.e(TAG, "Kindergarten document does not exist");
                                callback.onError("Kindergarten not found");
                            }
                        } else {
                            Log.e(TAG, "Error getting kindergarten: " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                            callback.onError(task.getException() != null ?
                                    task.getException().getMessage() : "Failed to retrieve kindergarten");
                        }
                    });
        } else {
            Log.e(TAG, "Invalid kindergarten ID provided");
            callback.onError("Invalid kindergarten ID");
        }
    }


    public void addKinderGarden(KinderGarten kinderGarten, FirebaseCallback<KinderGarten> callback) {
        String kinderGartenID = auth.getCurrentUser().getUid();

        if (kinderGartenID != null) {
            DocumentReference docRef = database.collection("kinderGartens").document(kinderGartenID);
            docRef.set(kinderGarten).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    callback.onSuccess(kinderGarten);
                } else {
                    callback.onError(task.getException().getMessage());
                }
            });
        } else {
            callback.onError("id is not valid");
        }
    }
    public void getKinderGarten(KinderGarten kinderGarten, FirebaseCallback<KinderGarten> callback) {
        String kinderGartenID = kinderGarten.getId();
        if(kinderGartenID != null) {
            database.collection("kinderGartens").document(kinderGartenID).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    callback.onSuccess(task.getResult().toObject(KinderGarten.class));
                } else {
                    callback.onError(task.getException().getMessage());
                }
            });
        } else {
            callback.onError("id is not valid");
        }
    }
    public void updateKinderGarden(KinderGarten kinderGarten, FirebaseCallback<KinderGarten> callback) {
        String kinderGartenID = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (kinderGartenID != null) {
            DocumentReference docRef = database.collection("kinderGartens").document(kinderGartenID);
            docRef.set(kinderGarten).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    callback.onSuccess(kinderGarten);
                } else {
                    callback.onError(task.getException().getMessage());
                }
            });
        } else {
            callback.onError("id is not valid");
        }
    }
    public void deleteKinderGarden(KinderGarten kinderGarten, FirebaseCallback<KinderGarten> callback) {
        String kinderGartenID = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (kinderGartenID != null) {
            database.collection("kinderGartens").document(kinderGartenID).delete().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    callback.onSuccess(kinderGarten);
                } else {
                    callback.onError(task.getException().getMessage());
                }
            });
        } else {
            callback.onError("id is not valid");
        }
    }
    public void searchKinderGartensByCity(String city, FirebaseCallback<List<KinderGarten>> callback) {
        if (city == null || city.trim().isEmpty()) {
            database.collection("kinderGartens")
                    .get()
                    .addOnCompleteListener(task -> handleQueryResult(task, callback));
        } else {
            database.collection("kinderGartens")
                    .whereGreaterThanOrEqualTo("address", city.trim())
                    .whereLessThanOrEqualTo("address", city.trim() + "\uf8ff")
                    .get()
                    .addOnCompleteListener(task -> handleQueryResult(task, callback));
        }
    }

    private void handleQueryResult(Task<QuerySnapshot> task, FirebaseCallback<List<KinderGarten>> callback) {
        if (task.isSuccessful()) {
            List<KinderGarten> kindergartens = new ArrayList<>();
            QuerySnapshot querySnapshot = task.getResult();

            if (querySnapshot != null) {
                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    KinderGarten kg = doc.toObject(KinderGarten.class);
                    if (kg != null) {
                        kg.setId(doc.getId());
                        kindergartens.add(kg);
                    }
                }
            }
            callback.onSuccess(kindergartens);
        } else {
            callback.onError(task.getException() != null ?
                    task.getException().getMessage() :
                    "Unknown error occurred");
        }
    }
}
