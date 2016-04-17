package org.flush.erates.controllers;

public class All {
	private String bankName;
	private String codeAlpha;
	private String date;
	private Double rateBuy;
	private Double rateSale;
	
	public All(String bankName, String codeAlpha, String date, Double rateBuy, Double rateSale) {
		this.bankName = bankName;
		this.codeAlpha = codeAlpha;
		this.date = date;
		this.rateBuy = rateBuy;
		this.rateSale = rateSale;
	}
	
	public String getBankName() {
		return bankName;
	}
	public String getCodeAlpha() {
		return codeAlpha;
	}
	public String getDate() {
		return date;
	}
	public Double getRateBuy() {
		return rateBuy;
	}
	public Double getRateSale() {
		return rateSale;
	}
}
