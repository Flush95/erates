package org.flush.erates.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.parsing.ParseDB;
import org.flush.erates.parsing.Parser;
import org.json.JSONObject;

public class Pb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TODAY_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
	private static final String NEEDED_DATE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

	private Parser parser = new Parser();
	private ParseDB parseDB = new ParseDB();
	private DateLogic dLogic = new DateLogic();
	private Distribution distribution = new Distribution();

	private String date;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		date = request.getParameter("date");

		if (date == "") {
			date = dLogic.getTodayDate();

			// Retrieve data from database
			if (parseDB.checkDateAndBank(date, "PB").list().size() != 0) {
				final List<Rates> rates = new LinkedList<>();
				for (final Object obj : parseDB.checkDateAndBank(date, "PB").list()) {
					rates.add((Rates) obj);
				}
				request.setAttribute("bank", "PrivatBank");

				// For EUR
				parseDB.getFromRates(request, "EUR", rates.get(0));

				// For USD
				parseDB.getFromRates(request, "USD", rates.get(1));

				// For RUB
				parseDB.getFromRates(request, "RUB", rates.get(2));

				request.getRequestDispatcher("info.jsp").forward(request, response);

			} else {
				HttpURLConnection connection = parser.openConnection(TODAY_URL, "");
				String json = parser.getJSONString(connection);

				request.setAttribute("bank", "PrivatBank");

				// For EUR
				JSONObject rateObj = parser.parseTodayPB(json, "EUR");
				distribution.settingResponse(request, "EUR", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));
				parseDB.insertToRates("PB", "EUR", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));

				// For USD
				rateObj = parser.parseTodayPB(json, "USD");
				distribution.settingResponse(request, "USD", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));
				parseDB.insertToRates("PB", "USD", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));

				// For RUB
				rateObj = parser.parseTodayPB(json, "RUR");
				distribution.settingResponse(request, "RUB", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));
				parseDB.insertToRates("PB", "RUB", date, rateObj.getDouble("buy"), rateObj.getDouble("sale"));

				request.getRequestDispatcher("info.jsp").forward(request, response);
			}
		} else {
			if (parseDB.checkDateAndBank(date, "PB").list().size() != 0) {
				final List<Rates> rates = new LinkedList<>();
				for (final Object obj : parseDB.checkDateAndBank(date, "PB").list()) {
					rates.add((Rates) obj);
				}
				request.setAttribute("bank", "PrivatBank");

				// For EUR
				parseDB.getFromRates(request, "EUR", rates.get(0));
				// For USD
				parseDB.getFromRates(request, "USD", rates.get(1));
				// For RUB
				parseDB.getFromRates(request, "RUB", rates.get(2));

				request.getRequestDispatcher("info.jsp").forward(request, response);

			} else {

				HttpURLConnection connection = parser.openConnection(NEEDED_DATE_URL, dLogic.formatPbDate(date));
				String json = parser.getJSONString(connection);

				request.setAttribute("bank", "PrivatBank");

				JSONObject rateObj = parser.parseSpecificDatePB(json, "EUR");
				distribution.settingResponse(request, "EUR", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));
				parseDB.insertToRates("PB", "EUR", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));

				// For USD
				rateObj = parser.parseSpecificDatePB(json, "USD");
				distribution.settingResponse(request, "USD", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));
				parseDB.insertToRates("PB", "USD", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));

				// For RUB
				rateObj = parser.parseSpecificDatePB(json, "RUB");
				distribution.settingResponse(request, "RUB", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));
				parseDB.insertToRates("PB", "RUB", date, rateObj.getDouble("purchaseRate"),
						rateObj.getDouble("saleRate"));

				request.getRequestDispatcher("info.jsp").forward(request, response);
			}
		}
	}

}
