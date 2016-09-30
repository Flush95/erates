package org.flush.erates.container;

public interface UrlContainer {
	static final String PB_TODAY_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
	static final String PB_NEEDED_DATE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
	static final String NBU_TODAY_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=3";
	static final String NBU_NEEDED_DATE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
}
