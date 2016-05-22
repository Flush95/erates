package org.flush.erates.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.service.CurrencyService;

@Path("/currencies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyResouce {
	
	@POST
	@Path("/buildpbtable")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getListPBFromDate(@FormParam("specificDate") String date) {
		date = ((date.length() > 0) && (date != null))? 
				DateLogic.formatPbDate(date): 
					DateLogic.formatPbDate(DateLogic.getTodayDate());
		return CurrencyService.getAllCurrenciesFromPb(date);
	}
	
	@POST
	@Path("/buildnbutable")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getListNBUFromDate(@FormParam("specificDate") String date) {
		date = ((date.length() > 0) && (date != null))? date: DateLogic.getTodayDate();
		return CurrencyService.getAllCurrenciesFromNbu(date);
	}
	
	@POST
	@Path("/diapasonnbutable")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getListNBUFromDiapason(@FormParam("startDate") String startDate, 
												@FormParam("endDate") String endDate) {
		return CurrencyService.getAllDiapasonCurrenciesFromNbu(startDate, endDate);
	}
	
	@POST
	@Path("/diapasonpbtable")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getListPBFromDiapason(@FormParam("startDate") String startDate, 
												@FormParam("endDate") String endDate) {
		return CurrencyService.getCurrencyPBFromDiapason(startDate, endDate);
	}
	
	@POST
	@Path("/nbuonebank")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates>getNbuOneBankList(@FormParam("chooseRate") String selectedRate,
										@FormParam("oneBankStartDate") String startDate, 
										@FormParam("oneBankEndDate") String endDate) {
		return CurrencyService.getRateFromNbu(selectedRate, startDate, endDate);
	}
	
	@POST
	@Path("/pbonebank")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getPbOneBankList(@FormParam("chooseRate") String selectedRate,
										@FormParam("oneBankStartDate") String startDate, 
										@FormParam("oneBankEndDate") String endDate) {
		return CurrencyService.getRateFromPb(selectedRate, startDate, endDate);
	}
	
	
	@POST
	@Path("/differentbanks")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Rates> getDifferentBanksList(@FormParam("chooseRate") String selectedRate,
												@FormParam("oneBankStartDate") String startDate, 
												@FormParam("oneBankEndDate") String endDate) {
		return CurrencyService.getRatesFromDifferentBanks(selectedRate, startDate, endDate);
	}
}
