package com.example.finalprojectshir2.models;

public class Manager {
    private String id;
    private String managerName;
    private String managerPass;
    private String managerEmail;
    private String createdAt;


    // Default constructor for Firebase
    public Manager() {
    }

    public Manager(String managerName, String managerPass, String managerEmail) {
        this.managerName = managerName;
        this.managerPass = managerPass;
        this.managerEmail = managerEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPass() {
        return managerPass;
    }

    public void setManagerPass(String managerPass) {
        this.managerPass = managerPass;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerPass='" + managerPass + '\'' +
                ", managerEmail='" + managerEmail + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}