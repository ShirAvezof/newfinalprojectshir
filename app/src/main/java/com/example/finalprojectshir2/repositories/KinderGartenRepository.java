package com.example.finalprojectshir2.repositories;

import com.example.finalprojectshir2.callbacks.KinderGartenCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

public class KinderGartenRepository {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public KinderGartenRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
    }

    public void addKinderGarden(KinderGarten kinderGarten, KinderGartenCallback callback) {
        // Get the currently authenticated user's ID
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId != null) {
            // Save the kinderGarten under a collection with the user's ID (or other logic)
            DocumentReference docRef = database.collection("kinderGartens").document(userId);

            // Set the kinderGarten document data
            docRef.set(kinderGarten, SetOptions.merge()) // Use SetOptions.merge() to update if the document already exists
                    .addOnSuccessListener(aVoid -> {

                        callback.onSuccess(kinderGarten);
                    })
                    .addOnFailureListener(e -> {
                        callback.onError(e.getMessage());
                    });
        } else {
            callback.onError("User not authenticated");
        }
    }
}
