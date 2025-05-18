package com.example.finalprojectshir2.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class ReviewResponse implements Serializable {
    private String userId;
    private String userName;
    private String comment;
    @ServerTimestamp
    private Timestamp createdAt;

    // Empty constructor needed for Firestore
    public ReviewResponse() {
    }

    public ReviewResponse(String userId, String userName, String comment) {
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
    }

    // Getters and Setters
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