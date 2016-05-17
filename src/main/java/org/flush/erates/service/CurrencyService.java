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
	private static List<LocalDate> daysList;
	private static List<String> jsonStringList;
	
	public static List<Rates> getCurrenciesPBDataSource(String date) {
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

	public static List<Rates> getCurrenciesNBUDataSource(String date) {
		rates = DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "NBU");

		if (rates.size() != 0) {
			return rates;
		} else if (rates.size() == 0) {
			if (!(date.equals(DateLogic.getTodayDate()))) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, date);
				String jsonStr = parser.getJSONString(connectionObj);
				rates = parser.getNBUDataSource(jsonStr, date);
				DatabaseClass.insertToRates(rates);
				return rates;
			} else if (date.equals(DateLogic.getTodayDate())) {
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, "");
				String jsonStr = parser.getJSONString(connectionObj);
				rates = parser.getNBUDataSource(jsonStr, DateLogic.getTodayDate());
				DatabaseClass.insertToRates(rates);
				return rates;
			}
		}

		return rates;
	}

	public static List<Rates> getCurrencyNBUFromDiapason(String startDate, String endDate) {
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!checkDiapason(startDate, endDate)) {
			System.out.println(checkDiapason(startDate, endDate));
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		checkDayOfWeekNBU(startDate, endDate);
		rates = parser.getNBUDataSourceDiapasonByDays(jsonStringList, daysList);
		DatabaseClass.insertToRates(rates);

		return rates;
	}
	
	public static List<Rates> getCurrencyPBFromDiapason(String startDate, String endDate) {
		if (((startDate.length() <= 0) || (startDate == null)) || ((endDate.length() <= 0) || endDate == null)) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		if (!checkDiapason(startDate, endDate)) {
			throw new WebApplicationException(ErrorMessages.diapasonDateBeforeException());
		}
		checkDayOfWeekPB(startDate, endDate);
		rates = parser.getPBDataSourceDiapasonByDays(jsonStringList, daysList);

		return rates;
	}

	public static boolean checkDiapason(String startDate, String endDate) {
		String start[] = startDate.split("-");
		String end[] = endDate.split("-");
		LocalDate startDateObj = LocalDate.of(Integer.valueOf(start[0]), Integer.valueOf(start[1]),
				Integer.valueOf(start[2]));
		LocalDate endDateObj = LocalDate.of(Integer.valueOf(end[0]), Integer.valueOf(end[1]), Integer.valueOf(end[2]));
		if (startDateObj.isBefore(endDateObj))
			return true;
		return false;
	}

	public static void checkDayOfWeekNBU(String startDate, String endDate) {
		String start[] = startDate.split("-");
		String end[] = endDate.split("-");
		LocalDate startDateObj = LocalDate.of(Integer.valueOf(start[0]), Integer.valueOf(start[1]),
				Integer.valueOf(start[2]));
		LocalDate endDateObj = LocalDate.of(Integer.valueOf(end[0]), Integer.valueOf(end[1]), Integer.valueOf(end[2]));

		daysList = new ArrayList<>();
		jsonStringList = new ArrayList<>();
		
		for (LocalDate date = startDateObj; date.isBefore(endDateObj.plusDays(1)); date = date.plusDays(1)) {
			if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				continue;
			} else {
				daysList.add(date);
				
				HttpURLConnection connectionObj = parser.openConnection(UrlContainer.NBU_NEEDED_DATE_URL, date.toString());
				jsonStringList.add(parser.getJSONString(connectionObj));
			}
		}
	}
	public static void checkDayOfWeekPB(String startDate, String endDate) {
		String start[] = startDate.split("-");
		String end[] = endDate.split("-");
		LocalDate startDateObj = LocalDate.of(Integer.valueOf(start[0]), Integer.valueOf(start[1]),
				Integer.valueOf(start[2]));
		LocalDate endDateObj = LocalDate.of(Integer.valueOf(end[0]), Integer.valueOf(end[1]), Integer.valueOf(end[2]));

		daysList = new ArrayList<>();
		jsonStringList = new ArrayList<>();
		
		for (LocalDate date = startDateObj; date.isBefore(endDateObj.plusDays(1)); date = date.plusDays(1)) {
			daysList.add(date);
				
			HttpURLConnection connectionObj = parser.openConnection(UrlContainer.PB_NEEDED_DATE_URL, DateLogic.formatPbDate(date.toString()));
			jsonStringList.add(parser.getJSONString(connectionObj));
		}
	}
	
}
