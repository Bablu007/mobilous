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

public class AutofillViaSAP {

	public JSONArray getUserData(String where) throws Exception {
		
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
		
		
		String path_name = "https://tslappsqa.tatasteel.co.in/eca/api/Custdata/GetCustData";
		
		URL url1 = new URL(path_name);		
		JSONArray jarray1 = new JSONArray();
		JSONObject jobject1 = new JSONObject();
		
		StringTokenizer stk = new StringTokenizer(where, ";");
        String inkunnr = stk.nextToken();
        String parameter = "inkunnr="+inkunnr;
		
		HttpURLConnection conn = (HttpURLConnection)url1.openConnection(); 
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("Accept", "application/json");
//		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);		
//		conn.connect();
		
//		OutputStream os = conn.getOutputStream();
//        os.write(parameter.getBytes());
//        os.flush();
//        os.close();
        
        
        JSONObject jo = new JSONObject();
        jo.put("inkunnr", inkunnr);
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
		
		//Print real json
//			System.out.println(response.toString());
			JSONParser parser = new JSONParser();
			JSONArray jarray2 = (JSONArray) parser.parse(response.toString());
			
			
			Iterator itr1 = jarray2.iterator();
	        while (itr1.hasNext())  
	        { 
	            itr1 = ((Map) itr1.next()).entrySet().iterator(); 
	            while (itr1.hasNext()) { 
	                Map.Entry pair = (Entry) itr1.next();
	                pair.getKey().toString().toLowerCase();
	                jobject1.put(pair.getKey().toString().toLowerCase(), pair.getValue()); 
	            } 
	        }
		}
	}
		jarray1.add(jobject1);
		return jarray1;
}
}