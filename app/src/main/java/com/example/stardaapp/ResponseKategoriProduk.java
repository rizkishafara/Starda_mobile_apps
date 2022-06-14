package com.example.stardaapp;

import java.util.List;

public class ResponseKategoriProduk{
	private List<com.example.stardaapp.SemuakategoriprodukItem> semuakategoriproduk;

	public void setSemuakategoriproduk(List<SemuakategoriprodukItem> semuakategoriproduk){
		this.semuakategoriproduk = semuakategoriproduk;
	}

	public List<com.example.stardaapp.SemuakategoriprodukItem> getSemuakategoriproduk(){
		return semuakategoriproduk;
	}

	@Override
 	public String toString(){
		return 
			"ResponseKategoriProduk{" + 
			"semuakategoriproduk = '" + semuakategoriproduk + '\'' + 
			"}";
		}
}