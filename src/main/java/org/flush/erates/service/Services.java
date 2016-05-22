package org.flush.erates.service;

import java.time.LocalDate;

public class Services {

	/** Check Diapason Exception **/
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
	
	
}
