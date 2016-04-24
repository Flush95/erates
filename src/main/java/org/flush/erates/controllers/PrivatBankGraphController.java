package org.flush.erates.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flush.erates.date.DateLogic;
import org.flush.erates.parsing.ParseDB;
import org.flush.erates.parsing.Parser;
import org.json.JSONObject;


public class PrivatBankGraphController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NEEDED_DATE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

	private ParseDB parseDB = new ParseDB();
	private Parser parser = new Parser();
	private DateLogic dLogic = new DateLogic();
	
	private String startDate;
	private String finishDate;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		startDate = dLogic.formatPbDate(request.getParameter("startDate"));
		finishDate = dLogic.formatPbDate(request.getParameter("finishDate"));


		ArrayList<String> date = new ArrayList<>();
		addDateToList(date, startDate, finishDate);

		for (String dateStr : date) {
			HttpURLConnection connection = parser.openConnection(NEEDED_DATE_URL, dateStr);
			String json = parser.getJSONString(connection);

			request.setAttribute("bank", "PrivatBank");

			JSONObject rateObj = parser.parseSpecificDatePB(json, "EUR");
			parseDB.insertToRates("PB", "EUR", dateStr, rateObj.getDouble("purchaseRate"),
					rateObj.getDouble("saleRate"));
			
			rateObj = parser.parseSpecificDatePB(json, "USD");
			parseDB.insertToRates("PB", "USD", dateStr, rateObj.getDouble("purchaseRate"),
					rateObj.getDouble("saleRate"));

			rateObj = parser.parseSpecificDatePB(json, "RUB");
			parseDB.insertToRates("PB", "RUB", dateStr, rateObj.getDouble("purchaseRate"),
					rateObj.getDouble("saleRate"));

		}

	}

	private void addDateToList(ArrayList<String> list, String startDate, String finishDate) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yyyy");
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(finishDate);
			calendar.setTime(start);

			do {
				StringBuilder sb = new StringBuilder();
				sb.append(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "."
						+ calendar.get(Calendar.YEAR));
				list.add(sb.toString());
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			} while (calendar.getTime().before(end));

		} catch (ParseException exp) {
			exp.printStackTrace();
		}
	}

}
