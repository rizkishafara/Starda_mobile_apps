package com.example.stardaapp;

public class SemuakeahlianItem{
	private String id_keahlian;
	private String keahlian_user;

	public String getId_keahlian() {
		return id_keahlian;
	}

	public void setId_keahlian(String id_keahlian) {
		this.id_keahlian = id_keahlian;
	}

	public String getKeahlian_user() {
		return keahlian_user;
	}

	public void setKeahlian_user(String keahlian_user) {
		this.keahlian_user = keahlian_user;
	}

	@Override
	public String toString() {
		return "SemuakeahlianItem{" +
				"id_keahlian='" + id_keahlian + '\'' +
				", keahlian_user='" + keahlian_user + '\'' +
				'}';
	}
}
