package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetTallySalesData {

	public static JSONArray gettallysalesdata(String where)
			throws ParseException, IOException, org.json.simple.parser.ParseException {

		try {
			// Don't Check SSL Validation
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted1(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public void checkServerTrusted1(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc;

			sc = SSLContext.getInstance("SSL");

			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify1(String hostname, SSLSession session) {
					return true;
				}

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {
			// TODO: handle exception
		}

		JSONArray a = new JSONArray();
		String whr = "";
		String bearer_token = GenerateBearerToken.generatebearer(whr);
		StringTokenizer stk = new StringTokenizer(where, ";");
		String dealer_code = stk.nextToken();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String to_date = format.format(cal.getTime());
		cal.add(Calendar.DATE, -30);
		String from_date = format.format(cal.getTime());
		System.out.println("From Date : " + from_date);
		System.out.println("To Date : " + to_date);
		System.out.println("Dealer Code : " + dealer_code);

		URL url = new URL("https://tslappsqa.tatasteel.co.in/eca/api/TFPTallySalesDlrCode/GetTallySalesData");
		JSONObject payload = new JSONObject();
		payload.put("FromDate", from_date);
		payload.put("ToDate", to_date);
		payload.put("dlrCode",dealer_code);
		System.out.println(payload);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("jwt-token", bearer_token);
		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(payload.toString());
		writer.flush();

		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(in);
		String api_output = "";
		String api_result = "";
		while ((api_output = br.readLine()) != null) {
			api_result += api_output;
		}

		JSONParser parser = new JSONParser();
		JSONArray arr = new JSONArray();
		arr = (JSONArray) parser.parse(api_result);

		return arr;
		//JSONArray final_response = new JSONArray();
		//final_response = addpkey(arr);
		//System.out.println(final_response);
		//return final_response;
	}

	public static JSONArray addpkey(JSONArray in) {
		JSONArray out = new JSONArray();
		for (int i = 0; i < in.size(); i++)
		{
			JSONObject obj = new JSONObject();
			obj = (JSONObject) in.get(i);
			String pkey = String.valueOf(i + 1);
			obj.put("tid", pkey);
			out.add(obj);
			obj = null;
		}
		return out;
	}
}
