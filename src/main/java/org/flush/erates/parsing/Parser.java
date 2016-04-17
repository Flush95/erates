package org.flush.erates.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flush.erates.controllers.All;
import org.flush.erates.utils.UnicodeConverter;
import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
	
	private URL urlObj;
	private HttpURLConnection connection;
	private int responseCode;
	private UnicodeConverter converter = new UnicodeConverter();
	
	public HttpURLConnection openConnection(String url, String date) {
		try {
			urlObj = new URL(date != ""? (url + date): url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("POST");
			responseCode = connection.getResponseCode();
		}catch(Exception e) {
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
	
	public JSONObject parseTodayPB(String jsonStr, String rate) {
		JSONArray array = new JSONArray(jsonStr);
		String ob = "";
		for (int i = 0; i < array.length(); i++) {
			if ((((JSONObject) array.get(i)).getString("ccy").toString()).equals(rate)) {
				ob = array.get(i).toString();
			}
		}
		JSONObject currencyObj = new JSONObject(ob);
		return currencyObj;
	}
	
	public JSONObject parseSpecificDatePB(String jsonStr, String rate) {
		JSONObject firstLvl = new JSONObject(jsonStr);
		JSONArray array = (JSONArray) firstLvl.get("exchangeRate");
		
		String ob = "";
		for (int i = 0; i < array.length(); i++) {
			if ((((JSONObject) array.get(i)).getString("currency").toString()).equals(rate)) {
				ob = array.get(i).toString();
			}
		}
		JSONObject currencyObj = new JSONObject(ob);
		return currencyObj;
	}
	
	public JSONObject parseSpecificDateNBU(String jsonStr, String rate) {
		JSONObject obj = new JSONObject(jsonStr);
		JSONObject nbuRate = (JSONObject) obj.get(rate);
		return nbuRate;
	}
	
	public List<All> parseAllBanks(HttpServletRequest request, String jsonStr) {
		List<All> info = new ArrayList<>();
		JSONArray jArray = new JSONArray(jsonStr);
		request.setAttribute("size", jArray.length());
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject temp = (JSONObject)jArray.get(i);
			info.add(new All(converter.convertUnicodeToString(temp.getString("bankName")),
					temp.getString("codeAlpha"),
					temp.getString("date"),
					temp.getDouble("rateBuy"),
					temp.getDouble("rateSale")));	
		}
		return info;
	}
	
	public void setJspInfo(JSONObject temp) {
		
		
	}
}
