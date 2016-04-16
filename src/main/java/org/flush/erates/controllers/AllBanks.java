package org.flush.erates.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flush.erates.date.DateLogic;
import org.flush.erates.parsing.ParseDB;
import org.flush.erates.parsing.Parser;


public class AllBanks extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "http://bank-ua.com/export/exchange_rate_cash.json";
	
	private Parser parser = new Parser();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpURLConnection connection = parser.openConnection(URL, "");
		String jsonStr = parser.getJSONString(connection);
		
		parser.parseAllBanks(request, jsonStr);
		request.getRequestDispatcher("allbanks.jsp").forward(request, response);
	}

}
