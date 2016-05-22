package org.flush.erates.service;

import java.net.HttpURLConnection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.flush.erates.database.DatabaseClass;
import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.errormessages.ErrorMessages;
import org.flush.erates.parsing.Parser;
import org.flush.erates.container.UrlContainer;

public class CurrencyService {

	private static Parser parser = new Parser();
	
	private static List<Rates> rates;
	private static List<String> daysList;
	private static List<String> jsonStringList;
	
	
	/** Getting All Currencies List From PrivatBank **/
	public static List<Rates> getAllCurrenciesFromPb(String date) {
		rates = new ArrayList<>();
		rates = DatabaseClass.getNeededDateList(date, "PrivatBank");

		if (rates.size() != 0) {
			return rates;
		} else if (rates.size() == 0) {
			if (!(date.equals(DateLogic.formatPbDate(DateLogic.getTodayDate())))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_NEEDED_DATE_URL, date);
				String jsonStr = parser.getJSONString(connectionObj);
				rates = parser.parseSpecificDatePB(jsonStr, date);
				DatabaseClass.insertToRates(rates);
				return rates;
			} else if (date.equals(DateLogic.formatPbDate(DateLogic.getTodayDate()))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_TODAY_URL, "");
				String jsonStr = parser.getJSONString(connectionObj);
				rates = parser.parseTodayPB(jsonStr);
				DatabaseClass.insertToRates(rates);
				return rates;
			}
		}
		return rates;
	}

	
	/** Getting All Currencies List From NBU **/
	public static List<Rates> getAllCurrenciesFromNbu(String date) {
		rates = new ArrayList<>();
		rates = DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "NBU");

		if (rates.size() != 0) {
			return rates;
		} else if (rates.size() == 0) {
			if (!(date.equals(DateLogic.getTodayDate()))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, date);
				String jsonStr = parser.getJSONString(connectionObj);
				if (jsonStr.equals("[]") || jsonStr.length() <= 0) {
					throw new WebApplicationException(ErrorMessages.rateForDateException());
				} else {
					rates = parser.getNBUDataSource(jsonStr, date);
					DatabaseClass.insertToRates(rates);
				}
				return rates;
			} else if (date.equals(DateLogic.getTodayDate())) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, "");
				String jsonStr = parser.getJSONString(connectionObj);
				if (jsonStr.equals("[]") || jsonStr.length() <= 0) {
					throw new WebApplicationException(ErrorMessages.rateForDateException());
				} else {
					rates = parser.getNBUDataSource(jsonStr, DateLogic.getTodayDate());
					DatabaseClass.insertToRates(rates);
				}
				return rates;
			}
		}
		return rates;
	}

	
	/** Getting All DIAPASON Currencies List From NBU **/
	public static List<Rates> getAllDiapasonCurrenciesFromNbu(String startDate, String endDate) {
		rates = new ArrayList<>();
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!Services.checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		checkDayOfWeek(startDate, endDate, "NBU");
		rates = parser.getNbuDiapasonByDays(jsonStringList, daysList);

		return rates;
	}
	
	
	/** Geting All DIAPASON Currecies List from PrivatBank  **/
	public static List<Rates> getCurrencyPBFromDiapason(String startDate, String endDate) {
		rates = new ArrayList<>();
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!Services.checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		checkDayOfWeek(startDate, endDate, "PB");
		rates.addAll(parser.getPbDiapasonByDays(jsonStringList, daysList));

		return rates;
	}
	
	
	/** Getting One Rate From Nbu **/
	public static List<Rates> getRateFromNbu(String selectedRate,
			String startDate, String endDate) {
		rates = new ArrayList<>();
		
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!Services.checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		
		checkDayOfWeek(startDate, endDate, "NBU");
		rates.addAll(parser.getNbuByRate(selectedRate, jsonStringList, daysList));

		return rates;
	}
	
	
	/** Getting One Rate From PrivatBank **/
	public static List<Rates> getRateFromPb(String selectedRate,
			String startDate, String endDate) {
		rates = new ArrayList<>();
		
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!Services.checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}

		checkDayOfWeek(startDate, endDate, "PB");
		rates.addAll(parser.getPbByRate(selectedRate, jsonStringList, daysList));
		
		return rates;
	}
	
	
	/** Getting Rates from Different Banks **/
	public static List<Rates> getRatesFromDifferentBanks(String selectedRate,
			String startDate, String endDate) {
		rates = new ArrayList<>();
		
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!Services.checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		
		checkDayOfWeek(startDate, endDate, "NBU");
		rates.addAll(parser.getNbuDiapasonByDays(jsonStringList, daysList));
				
		checkDayOfWeek(startDate, endDate, "PB");
		rates.addAll(parser.getPbDiapasonByDays(jsonStringList, daysList));
		
		
		
		return rates;
	}
	
	
	/** Converting and Parsing Days of Week **/
	public static void checkDayOfWeek(String startDate, String endDate, String bank) {
		String start[] = startDate.split("-");
		String end[] = endDate.split("-");
		LocalDate startDateObj = LocalDate.of(Integer.valueOf(start[0]), Integer.valueOf(start[1]),
				Integer.valueOf(start[2]));
		LocalDate endDateObj = LocalDate.of(Integer.valueOf(end[0]), Integer.valueOf(end[1]), Integer.valueOf(end[2]));

		daysList = new ArrayList<>();
		jsonStringList = new ArrayList<>();
		
		for (LocalDate date = startDateObj; date.isBefore(endDateObj.plusDays(1)); date = date.plusDays(1)) {
			if (bank.equals("NBU")) {
				if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
					continue;
				} else {
					daysList.add(date.toString());
					
					HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, date.toString());
					jsonStringList.add(parser.getJSONString(connectionObj));
				}
			} else if (bank.equals("PB")) {
				daysList.add(date.toString());	
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_NEEDED_DATE_URL, DateLogic.formatPbDate(date.toString()));
				jsonStringList.add(parser.getJSONString(connectionObj));
			}
		}
	}
}
