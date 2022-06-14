package com.example.stardaapp;

public class SemuakategoriItem{
	private String id_cat;
	private String category_user;

	public String getId_cat() {
		return id_cat;
	}

	public void setId_cat(String id_cat) {
		this.id_cat = id_cat;
	}

	public String getCategory_user() {
		return category_user;
	}

	public void setCategory_user(String category_user) {
		this.category_user = category_user;
	}

	@Override
	public String toString() {
		return "SemuakategoriItem{" +
				"id_cat='" + id_cat + '\'' +
				", category_user='" + category_user + '\'' +
				'}';
	}
}
