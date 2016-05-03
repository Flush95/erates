package org.flush.erates.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.json.JSONArray;
import org.json.JSONObject;

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
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.getTodayDate(),
						((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("USD")) {
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.getTodayDate(),
						((JSONObject) array.get(i)).getDouble("buy"),
						((JSONObject) array.get(i)).getDouble("sale")));
			} else if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals("RUR")) {
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("ccy").toString(),
						DateLogic.getTodayDate(),
						((JSONObject) array.get(i)).getDouble("buy"),
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
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("currency").toString(),
						date,
						((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("USD")) {
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("currency").toString(),
						date,
						((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			} else if ((((JSONObject) array.get(i)).getString("currency").toString()).equals("RUB")) {
				listRates.add(new Rates("PrivatBank", 
						((JSONObject) array.get(i)).getString("currency").toString(),
						date,
						((JSONObject) array.get(i)).getDouble("purchaseRate"),
						((JSONObject) array.get(i)).getDouble("saleRate")));
			}
		}
		return listRates;
	}

	public JSONObject parseSpecificDateNBU(String jsonStr, String rate) {
		JSONObject obj = new JSONObject(jsonStr);
		JSONObject nbuRate = (JSONObject) obj.get(rate);
		return nbuRate;
	}
}
