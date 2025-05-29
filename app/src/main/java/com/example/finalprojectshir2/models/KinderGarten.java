package com.example.finalprojectshir2.models;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class KinderGarten {
    private String id;
    private String ganname;
    private String ownerName;
    private String address;
    private String aboutgan;
    private String hours;
    private String phone;
    private String image;
    private Float averageRating;
    private Integer reviewCount;
    private boolean hasOnlineCameras;
    private boolean hasClosedCircuitCameras;
    private String licenseImage;
    private boolean hasBusinessLicense;
    private boolean isActiveOnFriday;
    private String licenseType; // Added license type field

    // Default constructor required for Firestore
    public KinderGarten() {
    }

    // Constructor with base fields
    public KinderGarten(String ganname, String ownerName, String address, String phone) {
        this.ganname = ganname;
        this.ownerName = ownerName;
        this.address = address;
        this.phone = phone;
    }

    // Full constructor
    public KinderGarten(String id, String ganname, String ownerName, String address, String aboutgan,
                        String hours, String phone, String image, boolean hasOnlineCameras,
                        boolean hasClosedCircuitCameras, boolean isActiveOnFriday) {
        this.id = id;
        this.ganname = ganname;
        this.ownerName = ownerName;
        this.address = address;
        this.aboutgan = aboutgan;
        this.hours = hours;
        this.phone = phone;
        this.image = image;
        this.hasOnlineCameras = hasOnlineCameras;
        this.hasClosedCircuitCameras = hasClosedCircuitCameras;
        this.isActiveOnFriday = isActiveOnFriday;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGanname() {
        return ganname;
    }

    public void setGanname(String ganname) {
        this.ganname = ganname;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAboutgan() {
        return aboutgan;
    }

    public void setAboutgan(String aboutgan) {
        this.aboutgan = aboutgan;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isHasOnlineCameras() {
        return hasOnlineCameras;
    }

    public void setHasOnlineCameras(boolean hasOnlineCameras) {
        this.hasOnlineCameras = hasOnlineCameras;
    }

    public boolean isHasClosedCircuitCameras() {
        return hasClosedCircuitCameras;
    }

    public void setHasClosedCircuitCameras(boolean hasClosedCircuitCameras) {
        this.hasClosedCircuitCameras = hasClosedCircuitCameras;
    }

    public boolean isActiveOnFriday() {
        return isActiveOnFriday;
    }

    public void setActiveOnFriday(boolean activeOnFriday) {
        isActiveOnFriday = activeOnFriday;
    }

    public String getLicenseImage() {
        return licenseImage;
    }

    public void setLicenseImage(String licenseImage) {
        this.licenseImage = licenseImage;
    }

    public boolean isHasBusinessLicense() {
        return hasBusinessLicense;
    }

    public void setHasBusinessLicense(boolean hasBusinessLicense) {
        this.hasBusinessLicense = hasBusinessLicense;
    }

    // License type getter and setter
    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public String toString() {
        return "KinderGarten{" +
                "id='" + id + '\'' +
                ", ganname='" + ganname + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", address='" + address + '\'' +
                ", aboutgan='" + aboutgan + '\'' +
                ", hours='" + hours + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + (image != null ? "Has image" : "No image") + '\'' +
                ", licenseImage='" + (licenseImage != null ? "Has license image" : "No license image") + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", hasOnlineCameras=" + hasOnlineCameras +
                ", hasClosedCircuitCameras=" + hasClosedCircuitCameras +
                ", hasBusinessLicense=" + hasBusinessLicense +
                ", isActiveOnFriday=" + isActiveOnFriday +
                '}';
    }
}