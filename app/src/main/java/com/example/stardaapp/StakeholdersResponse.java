package com.example.stardaapp;

import java.io.Serializable;

public class StakeholdersResponse implements Serializable {
    String id_user;
    String fullname;
    String email;
    String photo_user;
    String address_user;
    String keahlian_user;
    String category_user;
    String instansi;
    String gender;
    String about;
    String phone_user;


    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
    }

    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
    }

    public String getKeahlian_user() {
        return keahlian_user;
    }

    public void setKeahlian_user(String keahlian_user) {
        this.keahlian_user = keahlian_user;
    }

    public String getCategory_user() {
        return category_user;
    }

    public void setCategory_user(String category_user) {
        this.category_user = category_user;
    }

    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "StakeholdersResponse{" +
                "id_user='" + id_user + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", photo_user='" + photo_user + '\'' +
                ", address_user='" + address_user + '\'' +
                ", keahlian_user='" + keahlian_user + '\'' +
                ", category_user='" + category_user + '\'' +
                ", instansi='" + instansi + '\'' +
                ", gender='" + gender + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
