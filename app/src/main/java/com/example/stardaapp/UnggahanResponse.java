package com.example.stardaapp;

import java.io.Serializable;

public class UnggahanResponse implements Serializable {
    String id_user,id_produk, title_produk, name_produk,desc_produk,kategori_file,cat_file,status,upload_date,fullname,kegiatan,alasan;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    @Override
    public String toString() {
        return "UnggahanResponse{" +
                "id_user='" + id_user + '\'' +
                ", id_produk='" + id_produk + '\'' +
                ", title_produk='" + title_produk + '\'' +
                ", name_produk='" + name_produk + '\'' +
                ", desc_produk='" + desc_produk + '\'' +
                ", kategori_file='" + kategori_file + '\'' +
                ", cat_file='" + cat_file + '\'' +
                ", status='" + status + '\'' +
                ", upload_date='" + upload_date + '\'' +
                ", fullname='" + fullname + '\'' +
                ", kegiatan='" + kegiatan + '\'' +
                ", alasan='" + alasan + '\'' +
                '}';
    }
}
