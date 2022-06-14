package com.example.stardaapp;

import java.io.Serializable;

public class DokumenKaryaResponse implements Serializable {
    String id_document,produk_id,name_document,cat_file;

    public String getId_document() {
        return id_document;
    }

    public void setId_document(String id_document) {
        this.id_document = id_document;
    }

    public String getProduk_id() {
        return produk_id;
    }

    public void setProduk_id(String produk_id) {
        this.produk_id = produk_id;
    }

    public String getName_document() {
        return name_document;
    }

    public void setName_document(String name_document) {
        this.name_document = name_document;
    }

    public String getCat_file() {
        return cat_file;
    }

    public void setCat_file(String cat_file) {
        this.cat_file = cat_file;
    }

    @Override
    public String toString() {
        return "DokumenKaryaResponse{" +
                "id_document='" + id_document + '\'' +
                ", produk_id='" + produk_id + '\'' +
                ", name_document='" + name_document + '\'' +
                ", cat_file='" + cat_file + '\'' +
                '}';
    }
}
