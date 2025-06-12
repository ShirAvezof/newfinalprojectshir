package com.example.finalprojectshir2.repositories;

import android.util.Log;

import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Review;
import com.example.finalprojectshir2.models.ReviewResponse;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewRepository {
    private static final String TAG = "ReviewRepository";
    private FirebaseFirestore db;
    private CollectionReference reviewsCollection;

    public ReviewRepository() {
        db = FirebaseFirestore.getInstance();
        reviewsCollection = db.collection("reviews");
        Log.d(TAG, "ReviewRepository initialized");
    }

    public void getReviewsByKindergarten(String kindergartenId, FirebaseCallback<List<Review>> callback) {
        Log.d(TAG, "Getting reviews for kindergarten: " + kindergartenId);

        reviewsCollection.whereEqualTo("kindergartenId", kindergartenId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Review> reviews = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            review.setId(document.getId());
                            reviews.add(review);
                        }
                        Log.d(TAG, "Successfully retrieved " + reviews.size() + " reviews");
                        callback.onSuccess(reviews);
                    } else {
                        Log.e(TAG, "Error getting reviews", task.getException());
                        callback.onError(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public void addReview(Review review, FirebaseCallback<String> callback) {
        Log.d(TAG, "Adding new review for kindergarten: " + review.getKindergartenId());

        reviewsCollection.add(review)
                .addOnSuccessListener(documentReference -> {
                    String reviewId = documentReference.getId();
                    Log.d(TAG, "Review added with ID: " + reviewId);

                    // Update the review with its ID
                    documentReference.update("id", reviewId)
                            .addOnSuccessListener(aVoid -> callback.onSuccess(reviewId))
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error updating review ID", e);
                                callback.onSuccess(reviewId); // Still return success since the review was added
                            });

                    // Update the average rating for this kindergarten
                    updateKindergartenRating(review.getKindergartenId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding review", e);
                    callback.onError(e.getMessage());
                });
    }

    public void deleteReview(String reviewId, String kindergartenId, FirebaseCallback<Void> callback) {
        Log.d(TAG, "Deleting review: " + reviewId);

        reviewsCollection.document(reviewId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Review successfully deleted");

                    // Update the average rating for this kindergarten
                    updateKindergartenRating(kindergartenId);

                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting review", e);
                    callback.onError(e.getMessage());
                });
    }

    // New method for adding a manager response to a review
    public void addResponseToReview(String reviewId, ReviewResponse response, FirebaseCallback<Void> callback) {
        Log.d(TAG, "Adding response to review: " + reviewId);

        DocumentReference reviewRef = reviewsCollection.document(reviewId);

        reviewRef.update("response", response)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Response added successfully");
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding response", e);
                    callback.onError(e.getMessage());
                });
    }

    // New method for flagging a review
    public void flagReview(String reviewId, FirebaseCallback<Void> callback) {
        Log.d(TAG, "Flagging review: " + reviewId);

        DocumentReference reviewRef = reviewsCollection.document(reviewId);

        reviewRef.update("flagged", true)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Review flagged successfully");
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error flagging review", e);
                    callback.onError(e.getMessage());
                });
    }

    private void updateKindergartenRating(String kindergartenId) {
        // Calculate average rating from all reviews for this kindergarten
        reviewsCollection.whereEqualTo("kindergartenId", kindergartenId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    float totalRating = 0;
                    int count = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Review review = document.toObject(Review.class);

                        // Only include non-flagged reviews in the average
//                        if (!review.isFlagged()) {
////                            totalRating += review.getRating();
//                            count++;
//                        }
                    }

                    float averageRating = count > 0 ? totalRating / count : 0;

                    // Update the kindergarten document with the new average rating
                    FirebaseFirestore.getInstance().collection("kindergartens")
                            .document(kindergartenId)
                            .update("averageRating", averageRating,
                                    "reviewCount", count)
                            .addOnSuccessListener(aVoid ->
                                    Log.d(TAG, "Updated kindergarten rating: " + averageRating))
                            .addOnFailureListener(e ->
                                    Log.e(TAG, "Error updating kindergarten rating", e));
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error calculating average rating", e));
    }



}