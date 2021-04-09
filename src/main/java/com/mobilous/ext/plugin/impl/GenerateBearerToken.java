 package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class GenerateBearerToken {
	
	public static String generatebearer()throws ParseException, IOException {
		
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
		
	
	String jwt_token=GenerateJwtToken.generatejwt();
	
	System.out.println("------------------------------------------Generating Bearer Token-------------------------------------------------");
	URL url=new URL("https://tslin-qa.apigee.net/generatejwt-agrico");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestProperty("jwt-token",jwt_token);
	conn.setDoOutput(true);
	
	String bearer_token = conn.getHeaderField("jwt-response");
	System.out.println("Bearer Token================"+bearer_token);
	System.out.println("------------------------------------------Bearer Token Generated--------------------------------------------------");
	
	return bearer_token;
	}
}
