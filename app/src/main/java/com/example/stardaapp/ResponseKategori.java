package com.example.stardaapp;

import java.util.List;

public class ResponseKategori{
	private List<com.example.stardaapp.SemuakategoriItem> semuakategori;

	public void setSemuakategori(List<com.example.stardaapp.SemuakategoriItem> semuakategori){
		this.semuakategori = semuakategori;
	}

	public List<com.example.stardaapp.SemuakategoriItem> getSemuakategori(){
		return semuakategori;
	}

	@Override
 	public String toString(){
		return 
			"ResponseKategori{" + 
			"semuakategori = '" + semuakategori + '\'' + 
			"}";
		}
}