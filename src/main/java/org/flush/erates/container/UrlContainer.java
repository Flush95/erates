package org.flush.erates.container;

public interface UrlContainer {
	static final String PB_TODAY_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
	static final String PB_NEEDED_DATE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
	static final String NBU_NEEDED_DATE_URL = "http://api.minfin.com.ua/nbu/ad533c412d1b06df8024901590184c17a5eb4927/";
}
