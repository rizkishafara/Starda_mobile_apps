package com.example.stardaapp;

import java.io.Serializable;

public class KaryaResponse implements Serializable {
    String id_produk;
    String title_produk;
    String name_produk;
    String desc_produk;
    String kategori_file;
    String kegiatan;
    String cat_file;
    String status_produk;
    String upload_date;

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    String fullname;

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getTitle_produk() {
        return title_produk;
    }

    public void setTitle_produk(String title_produk) {
        this.title_produk = title_produk;
    }

    public String getName_produk() {
        return name_produk;
    }

    public void setName_produk(String name_produk) {
        this.name_produk = name_produk;
    }

    public String getDesc_produk() {
        return desc_produk;
    }

    public void setDesc_produk(String desc_produk) {
        this.desc_produk = desc_produk;
    }

    public String getKategori_file() {
        return kategori_file;
    }

    public void setKategori_file(String kategori_file) {
        this.kategori_file = kategori_file;
    }

    public String getCat_file() {
        return cat_file;
    }

    public void setCat_file(String cat_file) {
        this.cat_file = cat_file;
    }

    public String getStatus_produk() {
        return status_produk;
    }

    public void setStatus_produk(String status_produk) {
        this.status_produk = status_produk;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    @Override
    public String toString() {
        return "KaryaResponse{" +
                "id_produk='" + id_produk + '\'' +
                ", title_produk='" + title_produk + '\'' +
                ", name_produk='" + name_produk + '\'' +
                ", desc_produk='" + desc_produk + '\'' +
                ", kategori_file='" + kategori_file + '\'' +
                ", kegiatan='" + kegiatan + '\'' +
                ", cat_file='" + cat_file + '\'' +
                ", status_produk='" + status_produk + '\'' +
                ", upload_date='" + upload_date + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }
}
