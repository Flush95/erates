package org.flush.erates.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.flush.erates.database.DatabaseClass;
import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author User
 *
 */
public class Parser {

	private URL urlObj;
	private HttpURLConnection connection;
	private int responseCode;
	private List<Rates> listRates;

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

	public List<Rates> parseTodayPB(String jsonStr) {
		JSONArray array = new JSONArray(jsonStr);
		listRates = new ArrayList<>();

		for (int i = 0; i < array.length(); i++) {
			if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("EUR")) {
				listRates.add(new Rates("PrivatBank", ((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.formatPbDate(DateLogic.getTodayDate()), ((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("USD")) {
				listRates.add(new Rates("PrivatBank", ((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.formatPbDate(DateLogic.getTodayDate()), ((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("RUR")) {
				listRates.add(new Rates("PrivatBank", "RUB",
						DateLogic.formatPbDate(DateLogic.getTodayDate()), ((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			}
		}
		return listRates;
	}

	public List<Rates> parseSpecificDatePB(String jsonStr, String date) {
		JSONObject firstLvl = new JSONObject(jsonStr);
		JSONArray array = (JSONArray) firstLvl.get("exchangeRate");

		listRates = new ArrayList<>();

		for (int i = 0; i < array.length(); i++) {
			if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("EUR")) {
				listRates.add(new Rates("PrivatBank", ((JSONObject) array.get(i)).getString("currency").toString(),
						date, ((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("USD")) {
				listRates.add(new Rates("PrivatBank", ((JSONObject) array.get(i)).getString("currency").toString(),
						date, ((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("RUB")) {
				listRates.add(new Rates("PrivatBank", ((JSONObject) array.get(i)).getString("currency").toString(),
						date, ((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			}
		}
		return listRates;
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
	
	
	
	
	/*
	 * Diapason DataSources By Days in PrivatBank and National Bank Of Ukraine
	 */
	public List<Rates> getNBUDataSourceDiapasonByDays(List<String> jsonStrList, List<LocalDate> dateList) {

		listRates = new ArrayList<>();

		for (int i = 0; i < jsonStrList.size(); i++) {
			String date = dateList.get(i).toString();
			
			if (DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "NBU").size() > 0) {
				listRates.addAll(DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "PrivatBank"));
			}else {
			
				JSONObject obj = new JSONObject(jsonStrList.get(i));
				JSONObject eurObj = (JSONObject) obj.get("eur");
				JSONObject usdObj = (JSONObject) obj.get("usd");
				JSONObject rubObj = (JSONObject) obj.get("rub");
	
				listRates.add(new Rates("NBU", "EUR", DateLogic.formatPbDate(date),
						((JSONObject) eurObj).getDouble("ask"), ((JSONObject) eurObj).getDouble("bid")));
				listRates.add(new Rates("NBU", "USD", DateLogic.formatPbDate(date),
						((JSONObject) usdObj).getDouble("ask"), ((JSONObject) usdObj).getDouble("bid")));
				listRates.add(new Rates("NBU", "RUB", DateLogic.formatPbDate(date),
						((JSONObject) rubObj).getDouble("ask"), ((JSONObject) rubObj).getDouble("bid")));
			}
		}
		return listRates;
	}
	
	public List<Rates> getPBDataSourceDiapasonByDays(List<String> jsonStrList, List<LocalDate> dateList) {
		listRates = new ArrayList<>();

		for (int i = 0; i < jsonStrList.size(); i++) {
			String date = dateList.get(i).toString();

			if (DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "PrivatBank").size() > 0) {
				listRates.addAll(DatabaseClass.getNeededDateList(DateLogic.formatPbDate(date), "PrivatBank"));
			} else {
				JSONObject firstLvl = new JSONObject(jsonStrList.get(i));
				JSONArray array = (JSONArray) firstLvl.get("exchangeRate");

				for (int j = 0; j < array.length(); j++) {
					if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("EUR")) {
						Rates rate = new Rates("PrivatBank",
								((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble("purchaseRate"),
								((JSONObject) array.get(j)).getDouble("saleRate"));

						listRates.add(rate);
						DatabaseClass.insertSingleToRates(rate);

					} else if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("USD")) {
						Rates rate = new Rates("PrivatBank",
								((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble("purchaseRate"),
								((JSONObject) array.get(j)).getDouble("saleRate"));

						listRates.add(rate);
						DatabaseClass.insertSingleToRates(rate);
					} else if ((((JSONObject) array.get(j)).getString("currency").toString()).equals("RUB")) {
						Rates rate = new Rates("PrivatBank",
								((JSONObject) array.get(j)).getString("currency").toString(),
								DateLogic.formatPbDate(date), ((JSONObject) array.get(j)).getDouble("purchaseRate"),
								((JSONObject) array.get(j)).getDouble("saleRate"));
						listRates.add(rate);
						DatabaseClass.insertSingleToRates(rate);
					}
				}
			}
		}
		return listRates;
	}
	/*
	 * 
	 */
	
}
