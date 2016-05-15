package org.flush.erates.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity
public class Rates {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String bank;
	private String rate;
	private String date;
	private Double buy;
	private Double sale;
	
	public Rates() {}
	
	public Rates(String bank, String rate, String date, Double buy, Double sale) {
		this.bank = bank;
		this.rate = rate;
		this.date = date;
		this.buy = buy;
		this.sale = sale;
	}
	@XmlTransient
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getBuy() {
		return buy;
	}
	public void setBuy(Double buy) {
		this.buy = buy;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	
}
