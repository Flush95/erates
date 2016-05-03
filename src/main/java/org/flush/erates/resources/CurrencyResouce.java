package org.flush.erates.resources;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.service.CurrencyService;

@Path("/currencies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyResouce {
	
	private CurrencyService currencyService = new CurrencyService();
	
	@GET
	public List<Rates> getListFromDate(@QueryParam("date") String date, @QueryParam("bank") String bank) {
		return currencyService.getCurrenciesDataSource(bank, DateLogic.formatPbDate(date));
	}
	
}
