package com.example.stardaapp;

public class SemuakategoriprodukItem{
	private String kategori_file;
	private String id_kategori_file;

	public void setKategoriFile(String kategoriFile){
		this.kategori_file = kategoriFile;
	}

	public String getKategoriFile(){
		return kategori_file;
	}

	public void setIdKategoriFile(String idKategoriFile){
		this.id_kategori_file = idKategoriFile;
	}

	public String getIdKategoriFile(){
		return id_kategori_file;
	}

	@Override
 	public String toString(){
		return 
			"SemuakategoriprodukItem{" + 
			"kategori_file = '" + kategori_file + '\'' +
			",id_kategori_file = '" + id_kategori_file + '\'' +
			"}";
		}
}
