package com.example.stardaapp;

import java.util.List;

public class ResponseKeahlian{
	private List<com.example.stardaapp.SemuakeahlianItem> semuakeahlian;

	public void setSemuakeahlian(List<com.example.stardaapp.SemuakeahlianItem> semuakeahlian){
		this.semuakeahlian = semuakeahlian;
	}

	public List<com.example.stardaapp.SemuakeahlianItem> getSemuakeahlian(){
		return semuakeahlian;
	}

	@Override
 	public String toString(){
		return 
			"ResponseKeahlian{" + 
			"semuakeahlian = '" + semuakeahlian + '\'' + 
			"}";
		}
}