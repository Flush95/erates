package org.flush.erates.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flush.erates.parsing.Parser;


public class AllBanks extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "http://bank-ua.com/export/exchange_rate_cash.json";
	
	private Parser parser = new Parser();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpURLConnection connection = parser.openConnection(URL, "");
		String jsonStr = parser.getJSONString(connection);
		
		List<All> list = parser.parseAllBanks(request, jsonStr);
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("allObjectsList", list);
		request.getRequestDispatcher("allbanks.jsp").forward(request, response);
	}

}
