package org.flush.erates.controllers;

import javax.servlet.http.HttpServletRequest;

public class Distribution {
	public void settingResponse(HttpServletRequest request, String rate, String date, Double buy, Double sale) {
		request.setAttribute(rate + "Rate", rate);
		request.setAttribute(rate + "Date", date);
		request.setAttribute(rate + "Buy", buy);
		request.setAttribute(rate + "Sale", sale);
	}
}
