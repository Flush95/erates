package org.flush.erates.service;

import java.net.HttpURLConnection;
import java.util.List;

import org.flush.erates.database.DatabaseClass;
import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.parsing.Parser;
import org.flush.erates.container.UrlContainer;

public class CurrencyService {

	private Parser parser = new Parser();

	private List<Rates> rates;

	public List<Rates> getCurrenciesDataSource(String bank, String date) {
		rates = DatabaseClass.getNeededDateList(date, bank);
		
		if (rates.size() != 0) {
			return rates;
		}
		else if (rates.size() == 0) {
			if ((bank.equals("PrivatBank")) && !(date.equals(DateLogic.getTodayDate()))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_NEEDED_DATE_URL, date);
				String jsonStr = parser.getJSONString(connectionObj);
				return rates = parser.parseSpecificDatePB(jsonStr, date);
			} 
			else if ((bank.equals("PrivatBank")) && (date.equals(DateLogic.getTodayDate()))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_TODAY_URL, "");
				String jsonStr = parser.getJSONString(connectionObj);
				rates = parser.parseTodayPB(jsonStr);
				DatabaseClass.insertToRates(rates);
				return rates;
			}
		}
		
		return rates;
	}
	
}
