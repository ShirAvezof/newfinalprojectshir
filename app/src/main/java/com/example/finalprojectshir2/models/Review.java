package com.example.finalprojectshir2.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String id;
    private String kindergartenId;
    private String userId;
    private String userName;
    private String comment;
//    private float rating;

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    private boolean flagged;
    public ReviewResponse getResponse() {
        return response;
    }
    public boolean hasResponse() {
        return response != null;
    }

    public void setResponse(ReviewResponse response) {
        this.response = response;
    }

    private ReviewResponse response;
    @ServerTimestamp
    private Timestamp createdAt;

    // Empty constructor needed for Firestore
    public Review() {
    }

    public Review(String kindergartenId, String userId, String userName, String comment) {
        this.kindergartenId = kindergartenId;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
//        this.rating = rating;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKindergartenId() {
        return kindergartenId;
    }

    public void setKindergartenId(String kindergartenId) {
        this.kindergartenId = kindergartenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public float getRating() {
//        return rating;
//    }
//
//    public void setRating(float rating) {
//        this.rating = rating;
//    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAtDate() {
        return createdAt != null ? createdAt.toDate() : new Date();
    }
}