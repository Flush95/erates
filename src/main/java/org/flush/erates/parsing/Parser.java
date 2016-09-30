package org.flush.erates.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.flush.erates.database.DatabaseClass;
import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.flush.erates.errormessages.ErrorMessages;
import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {

	private URL urlObj;
	private HttpURLConnection connection;
	private int responseCode;
	private List<Rates> listRates;
	private Rates rateObj = null;

	public HttpURLConnection openConnection(String url, String date) {
		try {
			urlObj = new URL(date != "" ? (url + date) : url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("POST");
			responseCode = connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseCode == HttpURLConnection.HTTP_OK ? connection : null;
	}

	public String getJSONString(HttpURLConnection connection) {
		StringBuffer responseBuffer = new StringBuffer();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBuffer.toString();
	}

	public List<Rates> parseTodayPB(String jsonStr, String bank) {
		JSONArray array = new JSONArray(jsonStr);
		listRates = new ArrayList<>();
		
		if (array.equals("[]") || array.equals("{}") || array.length() <= 0) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		todayPbService(array, bank);

		return listRates;
	}

	public void todayPbService(JSONArray array, String bank) {
		for (int i = 0; i < array.length(); i++) {
			if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("EUR")) {
				listRates.add(new Rates(bank, ((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.formatPbDate(DateLogic.getTodayDate()), ((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("USD")) {
				listRates.add(new Rates(bank, ((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.formatPbDate(DateLogic.getTodayDate()), ((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("RUR")) {
				listRates.add(new Rates(bank, "RUB", DateLogic.formatPbDate(DateLogic.getTodayDate()),
						((JSONObject) array.get(i)).getDouble("buy"), ((JSONObject) array.get(i)).getDouble("sale")));
			}
		}
	}

	public List<Rates> parseSpecificDatePB(String jsonStr, String date, String bank) {
		listRates = new ArrayList<>();
		
		JSONObject firstLvl = new JSONObject(jsonStr);
		JSONArray array = (JSONArray) firstLvl.get("exchangeRate");

		if (array.equals("[]") || array.equals("{}") || array.length() <= 0) {
			throw new WebApplicationException(ErrorMessages.diapasonBadDatesException());
		}
		
		if (bank.equals("PrivatBank"))
			specificDatePbService(array, "PrivatBank", "purchaseRate", "saleRate", date);
		else if (bank.equals("NBU")) 
			specificDatePbService(array, "NBU", "purchaseRateNB", "saleRateNB", date);
		
		return listRates;
	}

	public void specificDatePbService(JSONArray array, String bank, String getBuy, String getSale, String date) {
		
		for (int i = 0; i < array.length(); i++) {
			
			if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("EUR")) {
				rateObj = new Rates(bank, ((JSONObject) array.get(i)).getString("currency").toString(), date,
						((JSONObject) array.get(i)).getDouble(getBuy),
						((JSONObject) array.get(i)).getDouble(getSale));
				
				/*if (DatabaseClass.getRate(rateObj).size() > 0)
					listRates.addAll(DatabaseClass.getRate(rateObj));
				else
					listRates.add(rateObj);*/
				
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("USD")) {
				
				rateObj = new Rates(bank, ((JSONObject) array.get(i)).getString("currency").toString(), date,
						((JSONObject) array.get(i)).getDouble(getBuy),
						((JSONObject) array.get(i)).getDouble(getSale));
				
				/*if (DatabaseClass.getRate(rateObj).size() > 0) 
					listRates.addAll(DatabaseClass.getRate(rateObj));
				else 
					listRates.add(rateObj);*/
				
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("RUB")) {
				
				rateObj = new Rates(bank, ((JSONObject) array.get(i)).getString("currency").toString(), date,
						((JSONObject) array.get(i)).getDouble(getBuy),
						((JSONObject) array.get(i)).getDouble(getSale));
				
				/*if (DatabaseClass.getRate(rateObj).size() > 0)
					listRates.addAll(DatabaseClass.getRate(rateObj));
				else
					listRates.add(rateObj);*/
			}
		}
	}
	
	
	public List<Rates> getNBUDataSource(String jsonStr, String date) {
		JSONObject obj = new JSONObject(jsonStr);
		JSONObject eurObj = (JSONObject) obj.get("eur");
		JSONObject usdObj = (JSONObject) obj.get("usd");
		JSONObject rubObj = (JSONObject) obj.get("rub");

		listRates = new ArrayList<>();

		listRates.add(new Rates("NBU", "EUR", DateLogic.formatPbDate(date), ((JSONObject) eurObj).getDouble("ask"),
				((JSONObject) eurObj).getDouble("bid")));
		listRates.add(new Rates("NBU", "USD", DateLogic.formatPbDate(date), ((JSONObject) usdObj).getDouble("ask"),
				((JSONObject) usdObj).getDouble("bid")));
		listRates.add(new Rates("NBU", "RUB", DateLogic.formatPbDate(date), ((JSONObject) rubObj).getDouble("ask"),
				((JSONObject) rubObj).getDouble("bid")));

		return listRates;
	}


	public List<Rates> getNbuDiapasonByDays(List<String> jsonStrList, List<String> dateList) {
		listRates = new ArrayList<>();
		diapasonService(jsonStrList, dateList, "NBU", "purchaseRateNB", "saleRateNB");
				
		return listRates;
	}

	public List<Rates> getPbDiapasonByDays(List<String> jsonStrList, List<String> dateList) {
		listRates = new ArrayList<>();
		diapasonService(jsonStrList, dateList, "PrivatBank", "purchaseRate", "saleRate");
		
		return listRates;
	}

	public void diapasonService(List<String> jsonStrList, List<String> dateList, String bank, String getBuy, String getSale) {
		for (int i = 0; i < jsonStrList.size(); i++) {
			if (jsonStrList.get(i).equals("[]") || jsonStrList.get(i).equals("{}") || jsonStrList.get(i).length() <= 0)
				continue;

			String date = dateList.get(i);

			JSONObject firstLvl = new JSONObject(jsonStrList.get(i));
			JSONArray array = (JSONArray) firstLvl.get("exchangeRate");

			for (int j = 0; j < array.length(); j++) {
				if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("EUR")) {
					rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
							DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
							((JSONObject) array.get(j)).getDouble(getSale));
					listRates.add(rateObj);
					/*if (DatabaseClass.getRate(rateObj).size() > 0) {
						listRates.addAll(DatabaseClass.getRate(rateObj));
					} else {
						listRates.add(rateObj);
						DatabaseClass.insertSingleToRates(rateObj);
					}*/

				} else if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("USD")) {
					rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
							DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
							((JSONObject) array.get(j)).getDouble(getSale));
					listRates.add(rateObj);
					/*if (DatabaseClass.getRate(rateObj).size() > 0) {
						listRates.addAll(DatabaseClass.getRate(rateObj));
					} else {
						listRates.add(rateObj);
						DatabaseClass.insertSingleToRates(rateObj);
					}*/

				} else if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("RUB")) {
					rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
							DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
							((JSONObject) array.get(j)).getDouble(getSale));
					listRates.add(rateObj);
					/*if (DatabaseClass.getRate(rateObj).size() > 0) {
						listRates.addAll(DatabaseClass.getRate(rateObj));
					} else {
						listRates.add(rateObj);
						DatabaseClass.insertSingleToRates(rateObj);
					}*/

				}
			}
		}
	}
	
	
	public List<Rates> getNbuByRate(String rate, List<String> jsonStrList, List<String> dateList) {
		listRates = new ArrayList<>();
		oneRateChartService(rate, jsonStrList, dateList, "NBU", "purchaseRateNB", "saleRateNB");
		
		return listRates;
	}

	public List<Rates> getPbByRate(String rate, List<String> jsonStrList, List<String> dateList) {
		listRates = new ArrayList<>();
		oneRateChartService(rate, jsonStrList, dateList, "PrivatBank", "purchaseRate", "saleRate");
		
		return listRates;
	}
	
	public void oneRateChartService(String rate, List<String> jsonStrList, List<String> dateList, String bank, 
			String getBuy, String getSale) {
		
		for (int i = 0; i < jsonStrList.size(); i++) {

			if (jsonStrList.get(i).equals("[]") || jsonStrList.get(i).equals("{}") || jsonStrList.get(i).length() <= 0)
				continue;

			String date = dateList.get(i);

			JSONObject firstLvl = new JSONObject(jsonStrList.get(i));

			JSONArray array = (JSONArray) firstLvl.get("exchangeRate");
			for (int j = 0; j < array.length(); j++) {
				if (rate.equals("EUR")) {
					if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("EUR")) {
						
						rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
								((JSONObject) array.get(j)).getDouble(getSale));
						listRates.add(rateObj);
						/*if (DatabaseClass.getRate(rateObj).size() > 0) {
							listRates.addAll(DatabaseClass.getRate(rateObj));
						} else {
							listRates.add(rateObj);
							DatabaseClass.insertSingleToRates(rateObj);
						}*/
					}
				} else if (rate.equals("USD")) {
					if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("USD")) {
						rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
								((JSONObject) array.get(j)).getDouble(getSale));
						listRates.add(rateObj);
						/*if (DatabaseClass.getRate(rateObj).size() > 0) {
							listRates.addAll(DatabaseClass.getRate(rateObj));
						} else {
							listRates.add(rateObj);
							DatabaseClass.insertSingleToRates(rateObj);
						}*/
					}
				} else if (rate.equals("RUB")) {
					if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("RUB")) {
						rateObj = new Rates(bank, ((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble(getBuy),
								((JSONObject) array.get(j)).getDouble(getSale));
						listRates.add(rateObj);
						/*if (DatabaseClass.getRate(rateObj).size() > 0) {
							listRates.addAll(DatabaseClass.getRate(rateObj));
						} else {
							listRates.add(rateObj);
							DatabaseClass.insertSingleToRates(rateObj);
						}*/
					}
				}
			}
		}
	}
	
}
