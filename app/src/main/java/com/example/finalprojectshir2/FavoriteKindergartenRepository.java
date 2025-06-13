package com.example.finalprojectshir2;


import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteKindergartenRepository {
    private static final String TAG = "FavoriteRepository";
    private static final String COLLECTION_NAME = "likedGans";
    private final FirebaseFirestore db;
    private static FavoriteKindergartenRepository instance;

    public static FavoriteKindergartenRepository getInstance() {
        if(instance == null) {
            instance = new FavoriteKindergartenRepository();
        }
        return instance;
    }

    public interface FavoriteCallback {
        void onResult(boolean success);
    }

    public interface FavoriteStatusCallback {
        void onResult(boolean isFavorite);
    }

    public FavoriteKindergartenRepository() {
        this.db = FirebaseFirestore.getInstance();
    }//המשתנה db הוא בעצם האובייקט שמייצג את החיבור ל־Firestore


     //Adds a kindergarten to user's favorites


    public void addToFavorites(String userId, String kindergartenId, FavoriteCallback callback) {
        Log.d(TAG, "Adding kindergarten " + kindergartenId + " to favorites for user " + userId);

        DocumentReference userFavoritesRef = db.collection(COLLECTION_NAME).document(userId);

        // Add the kindergarten ID to the favorites array
        userFavoritesRef.update("favoriteGans", FieldValue.arrayUnion(kindergartenId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully added to favorites");
                    callback.onResult(true);
                })
                .addOnFailureListener(e -> {
                    // If the document doesn't exist yet, create it
                    if (e.getMessage() != null && e.getMessage().contains("No document to update")) {
                        Log.d(TAG, "Document doesn't exist, creating new one");

                        Map<String, Object> userData = new HashMap<>();
                        ArrayList<String> favorites = new ArrayList<>();
                        favorites.add(kindergartenId);
                        userData.put("favoriteGans", favorites);
                        userFavoritesRef.set(userData, SetOptions.merge())
                                .addOnSuccessListener(aVoid2 -> {
                                    Log.d(TAG, "Successfully created favorites document and added kindergarten");
                                    callback.onResult(true);
                                })
                                .addOnFailureListener(e2 -> {
                                    Log.e(TAG, "Error creating favorites document", e2);
                                    callback.onResult(false);
                                });
                    } else {
                        Log.e(TAG, "Error adding to favorites", e);
                        callback.onResult(false);
                    }
                });
    }

    /**
     * Removes a kindergarten from user's favorites
     * @param userId User ID
     * @param kindergartenId Kindergarten ID to remove from favorites
     * @param callback Callback for operation result
     */
    public void removeFromFavorites(String userId, String kindergartenId, FavoriteCallback callback) {
        Log.d(TAG, "Removing kindergarten " + kindergartenId + " from favorites for user " + userId);

        DocumentReference userFavoritesRef = db.collection(COLLECTION_NAME).document(userId);

        userFavoritesRef.update("favoriteGans", FieldValue.arrayRemove(kindergartenId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully removed from favorites");
                    callback.onResult(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error removing from favorites", e);
                    callback.onResult(false);
                });
    }

    /**
     * Checks if a kindergarten is in user's favorites
     * @param userId User ID
     * @param kindergartenId Kindergarten ID to check
     * @param callback Callback with result (true if favorite, false if not)
     */
    public void checkIfFavorite(String userId, String kindergartenId, FavoriteStatusCallback callback) {
        Log.d(TAG, "Checking if kindergarten " + kindergartenId + " is in favorites for user " + userId);

        DocumentReference userFavoritesRef = db.collection(COLLECTION_NAME).document(userId);

        userFavoritesRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ArrayList<String> favorites = (ArrayList<String>) documentSnapshot.get("favoriteGans");
                        boolean isFavorite = favorites != null && favorites.contains(kindergartenId);
                        Log.d(TAG, "Is favorite: " + isFavorite);
                        callback.onResult(isFavorite);
                    } else {
                        Log.d(TAG, "User doesn't have favorites document yet");
                        callback.onResult(false);

                   }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking favorite status", e);
                    callback.onResult(false);
                });
    }


    /**
     * Gets all of a user's favorite kindergartens
     * @param userId User ID
     * @return List of kindergarten IDs that are favorites
     */
    public void getAllFavorites(String userId, final OnFavoritesLoadedListener listener) {
        Log.d(TAG, "Getting all favorites for user " + userId);

        DocumentReference userFavoritesRef = db.collection(COLLECTION_NAME).document(userId);

        userFavoritesRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ArrayList<String> favorites = (ArrayList<String>) documentSnapshot.get("favoriteGans");
                        Log.d(TAG, "Retrieved " + (favorites != null ? favorites.size() : 0) + " favorites");
                        listener.onFavoritesLoaded(favorites != null ? favorites : new ArrayList<>());
                    } else {
                        Log.d(TAG, "User doesn't have favorites document yet");
                        listener.onFavoritesLoaded(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting favorites", e);
                    listener.onError(e.getMessage());
                });
    }

    public interface OnFavoritesLoadedListener {
        void onFavoritesLoaded(ArrayList<String> favoriteKindergartenIds);
        void onError(String errorMessage);
    }
}
