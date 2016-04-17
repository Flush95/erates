package org.flush.erates.controllers;

public class All {
	public String bankName;
	public String codeAlpha;
	public String date;
	public Double rateBuy;
	public Double rateSale;
	
	public All(String bankName, String codeAlpha, String date, Double rateBuy, Double rateSale) {
		this.bankName = bankName;
		this.codeAlpha = codeAlpha;
		this.date = date;
		this.rateBuy = rateBuy;
		this.rateSale = rateSale;
	}
}
