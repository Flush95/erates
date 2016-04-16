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

public class Nbu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String NEEDED_DATE_URL = "http://api.minfin.com.ua/nbu/ad533c412d1b06df8024901590184c17a5eb4927/";

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
			if (parseDB.checkDateAndBank(date, "NBU").list().size() != 0) {
				final List<Rates> rates = new LinkedList<>();
				for (final Object obj : parseDB.checkDateAndBank(date, "NBU").list()) {
					rates.add((Rates) obj);
				}
				request.setAttribute("bank", "National Bank Of Ukraine");
				// For EUR
				parseDB.getFromRates(request, "EUR", rates.get(0));
				// For USD
				parseDB.getFromRates(request, "USD", rates.get(1));
				// For RUB
				parseDB.getFromRates(request, "RUB", rates.get(2));

				request.getRequestDispatcher("info.jsp").forward(request, response);

			} else {

				HttpURLConnection connection = parser.openConnection(NEEDED_DATE_URL, "");
				String json = parser.getJSONString(connection);

				request.setAttribute("bank", "National Bank Of Ukraine");

				// For EUR
				JSONObject rateObj = parser.parseSpecificDateNBU(json, "eur");
				distribution.settingResponse(request, "EUR", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "EUR", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				// For USD
				rateObj = parser.parseSpecificDateNBU(json, "usd");
				distribution.settingResponse(request, "USD", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "USD", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				// For RUB
				rateObj = parser.parseSpecificDateNBU(json, "rub");
				distribution.settingResponse(request, "RUB", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "RUB", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				request.getRequestDispatcher("info.jsp").forward(request, response);
			}

		} else {

			if (parseDB.checkDateAndBank(date, "NBU").list().size() != 0) {

				final List<Rates> rates = new LinkedList<>();
				for (final Object obj : parseDB.checkDateAndBank(date, "NBU").list()) {
					rates.add((Rates) obj);
				}
				request.setAttribute("bank", "National Bank Of Ukraine");

				// For EUR
				parseDB.getFromRates(request, "EUR", rates.get(0));
				// For USD
				parseDB.getFromRates(request, "USD", rates.get(1));
				// For RUB
				parseDB.getFromRates(request, "RUB", rates.get(2));

				request.getRequestDispatcher("info.jsp").forward(request, response);

			} else {

				HttpURLConnection connection = parser.openConnection(NEEDED_DATE_URL, date);
				String json = parser.getJSONString(connection);
				request.setAttribute("bank", "National Bank Of Ukraine");

				// For EUR
				JSONObject rateObj = parser.parseSpecificDateNBU(json, "eur");
				distribution.settingResponse(request, "EUR", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "EUR", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				// For USD
				rateObj = parser.parseSpecificDateNBU(json, "usd");
				distribution.settingResponse(request, "USD", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "USD", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				// For RUB
				rateObj = parser.parseSpecificDateNBU(json, "rub");
				distribution.settingResponse(request, "RUB", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));
				parseDB.insertToRates("NBU", "RUB", date, rateObj.getDouble("ask"), rateObj.getDouble("bid"));

				request.getRequestDispatcher("info.jsp").forward(request, response);

			}
		}
	}

}
