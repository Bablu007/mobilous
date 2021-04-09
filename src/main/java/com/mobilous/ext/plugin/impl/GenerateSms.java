package com.mobilous.ext.plugin.impl;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;

public class GenerateSms {
	
	
	public static String generatesms  (String where) throws ParseException, IOException {
		
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
		
		
		String whr="";
		String bearer_token=GenerateBearerToken.generatebearer(whr);
		
		
		
		
		URL url=new URL("https://tslin-qa.apigee.net/smsproxy/SendSMS");
		
		JSONObject payload=new JSONObject();
		StringTokenizer stk=new StringTokenizer(where,";");
		String mobile=stk.nextToken();
		String id=stk.nextToken();
		String quantity=stk.nextToken();
		String dealer_name=stk.nextToken();
		
		
		String text="Dear Mason "+id+", your sale of "+quantity+" MT has been recorded on the TATA Sampoorna App by the dealer "+dealer_name+".\n\nTeam Tata Sampoorna";
		payload.put("mobNo",mobile);
		payload.put("imeiNo","");
		payload.put("strMSG",text); 
		System.out.println(payload);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("jwt-token",bearer_token);
		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);
		
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(payload.toString());
		writer.flush();
		
		InputStreamReader in = new InputStreamReader(conn.getInputStream());
	    BufferedReader br = new BufferedReader(in);
	    String api_output="";
	    String api_result="";
	    while ((api_output = br.readLine()) != null) {
	    	api_result+=api_output;
	    }
	    api_result=api_result.replace("\"", "");
	    api_result="Success";
	    System.out.println(api_result);
		return api_result;
	}

}
