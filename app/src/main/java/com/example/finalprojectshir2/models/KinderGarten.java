package com.example.finalprojectshir2.models;


public class KinderGarten {


    private String id;
    private String ganname, ownerName, address, aboutgan,hours;
    private String phone,imgg;


    public KinderGarten( String ganname, String ownerName,String address,String aboutgan,String hours,String phone, String id,String imgg) {
        this.id=id;
        this.ganname = ganname;
        this.ownerName = ownerName;
        this.address = address;
        this.aboutgan = aboutgan;
        this.hours = hours;
        this.phone = phone;
        this.imgg = imgg;
    }
    public KinderGarten() {

    }

    public String getImgg() {
        return imgg;
    }

    public void setImgg(String imgg) {
        this.imgg = imgg;
    }

    public KinderGarten(String ganname, String ownerName, String address, String phone) {
        this.ganname = ganname;
        this.ownerName = ownerName;
        this.address = address;
        this.phone = phone;
    }
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
                ", imgg='" + imgg + '\'' +
                '}';
    }
}
