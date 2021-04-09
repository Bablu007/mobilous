package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.util.StringTokenizer;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ActivateAndDeactivateDealer {

	public JSONArray deactivateDealer(String where) throws Exception {
		
		try {
			//Don't Check SSL Validation
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
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
			}
			};

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
			}catch (Exception e) {
			// TODO: handle exception
			}
		
		
//		String path_name = "https://tslappsqa.tatasteel.co.in/eca/api/ActivateAgricoDealer/DeActivateDealers";
//		String path_name = "https://tslapps.tatasteel.co.in/eca/api/ActivateAgricoDealer/DeActivateDealers";
		String path_name = "https://tslapps.tatasteel.co.in/eca/api/ActivateAgricoDealer/DeActivateDealers";
		
		URL url1 = new URL(path_name);		
		JSONArray jarray1 = new JSONArray();
		
		
		StringTokenizer stk = new StringTokenizer(where, ";");
        String dealerCode = stk.nextToken();
        String distCode = stk.nextToken();
        String status = stk.nextToken();
		
		HttpURLConnection conn = (HttpURLConnection)url1.openConnection(); 
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
//		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);		

        JSONObject jo = new JSONObject();
        jo.put("dealerCode", dealerCode);
        jo.put("distCode", distCode);
        jo.put("status", status);
        
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jo.toString().getBytes("utf-8");
            os.write(input, 0, input.length);	
            os.flush();
            os.close();
        }
        
        conn.connect();
	
		System.out.println(conn.getRequestProperty("Authorization"));
		if(conn.getResponseCode() != HttpStatus.SC_UNPROCESSABLE_ENTITY) {
			int responsecode = conn.getResponseCode();
			System.out.println(responsecode);
			int id = 1;
			if(responsecode == 200) {
		
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			
			JSONParser parser = new JSONParser();
			JSONObject jobject2 = (JSONObject) parser.parse(response.toString());
			Boolean success = (Boolean) jobject2.get("Success");
			String errormsg = (String) jobject2.get("ErrorMsg");
			JSONObject jobject1 = new JSONObject();
			jobject1.put("success", success);
			jobject1.put("errormsg", errormsg);
			jarray1.add(jobject1);
		}
	}
		return jarray1;
}
}