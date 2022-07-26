package com.example.stardaapp;

import java.io.Serializable;

public class UnduhanResponse implements Serializable
{
    String id_unduhan,id_produk_unduh,id_user_unduh,tanggal_unduh,title_produk,fullname,photo_user;

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
    }

    public String getId_unduhan() {
        return id_unduhan;
    }

    public void setId_unduhan(String id_unduhan) {
        this.id_unduhan = id_unduhan;
    }

    public String getId_produk_unduh() {
        return id_produk_unduh;
    }

    public void setId_produk_unduh(String id_produk_unduh) {
        this.id_produk_unduh = id_produk_unduh;
    }

    public String getId_user_unduh() {
        return id_user_unduh;
    }

    public void setId_user_unduh(String id_user_unduh) {
        this.id_user_unduh = id_user_unduh;
    }

    public String getTanggal_unduh() {
        return tanggal_unduh;
    }

    public void setTanggal_unduh(String tanggal_unduh) {
        this.tanggal_unduh = tanggal_unduh;
    }

    public String getTitle_produk() {
        return title_produk;
    }

    public void setTitle_produk(String title_produk) {
        this.title_produk = title_produk;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "UnduhanResponse{" +
                "id_unduhan='" + id_unduhan + '\'' +
                ", id_produk_unduh='" + id_produk_unduh + '\'' +
                ", id_user_unduh='" + id_user_unduh + '\'' +
                ", tanggal_unduh='" + tanggal_unduh + '\'' +
                ", title_produk='" + title_produk + '\'' +
                ", fullname='" + fullname + '\'' +
                ", photo_user='" + photo_user + '\'' +
                '}';
    }
}
