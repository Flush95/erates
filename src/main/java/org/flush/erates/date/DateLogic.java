package org.flush.erates.date;

import java.time.LocalDate;

public class DateLogic {
	
	public String getTodayDate() {
		LocalDate localDate = LocalDate.now();
		return localDate.toString();
	}
	public String convertResponseDate(String dateFromResponse) {
		String dateM[] = dateFromResponse.split(" ");
		return dateM[0];
	}
	public String formatPbDate(String date) {
		String dateY_M_D[] = date.split("-");
		return dateY_M_D[2] + "." + dateY_M_D[1] + "." + dateY_M_D[0];
	}
	
}
