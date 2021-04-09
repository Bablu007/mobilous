package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ModifyDealerDetails {

	public static boolean modifydealerdetails(String where) throws ParseException, IOException,
			org.json.simple.parser.ParseException, ClassNotFoundException, SQLException {

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

		String whr = "";
		String bearer_token = GenerateBearerToken.generatebearer(whr);

		StringTokenizer stk = new StringTokenizer(where, ";");
		String dealer_code = stk.nextToken();
		String dealer_name = stk.nextToken();
		String dist_code = stk.nextToken();
		String phone_no = stk.nextToken();
		String address_one = stk.nextToken();
		String district = stk.nextToken();
		String state = stk.nextToken();
		String taluk = stk.nextToken();
		String pincode = stk.nextToken();
		// String mobile=stk.nextToken();
		// String retail_cat=stk.nextToken();
		String sub_cat = stk.nextToken();
		String outlet_cat = stk.nextToken();
		// String route=stk.nextToken();
		String email = stk.nextToken();
		String brand = stk.nextToken();
		// String aso=stk.nextToken();
		String gstno = stk.nextToken();
		String gst_state_code = stk.nextToken();
		// String gst_regd_type=stk.nextToken();
		String cat = stk.nextToken();
		String status = stk.nextToken();
		String tier = stk.nextToken();
		String con_person="";
	    try {
	    con_person=stk.nextToken();
	    }
	    catch(Exception e) {
	    	con_person=getContactPerson(dealer_code, dist_code);
	    }

		// URL url=new
		// URL("https://tslapps.tatasteel.co.in/eca/api/TisconDealerEdit/ModifyDealerDetails");
		//URL url = new URL("https://tslappsqa.tatasteel.co.in/eca/api/TisconDealerEdit/ModifyDealerDetails");
		
		URL url=new URL("https://tslapps.tatasteel.co.in/eca/api/TisconDealerEdit/ModifyDealerDetails");
		
//		System.out.println("URL -> https://tslapps.tatasteel.co.in/eca/api/TisconDealerEdit/ModifyDealerDetails");
		
		String zone = get_zone(state);

		System.out.println("Users zone --> "+zone);
		
		
		JSONObject payload = new JSONObject();
		payload.put("dealerCode", dealer_code);
		payload.put("dealerName", dealer_name);
		payload.put("distCode", dist_code);
		payload.put("phoneNo", phone_no);
		payload.put("addressOne", address_one);
		payload.put("addressTwo", "");
		payload.put("contactPerson", con_person);
		payload.put("district", district);
		payload.put("state", state);
		payload.put("town", taluk);
		payload.put("pinCode", pincode);
		payload.put("mobile", "");
		payload.put("retailCat", "NE-OTHERS");
		payload.put("subCat", sub_cat);
		payload.put("OutletCat", outlet_cat);
		payload.put("weighbridge", "MULTI-BRANDED");
		payload.put("intransitDays", "");
		payload.put("creditLimit", "");
		payload.put("route", "");
		payload.put("Data_send_Days", "");
		payload.put("sndleg_Coverage", "");
		payload.put("status", status);
		payload.put("TOC_CRM_FLAG", "");
		payload.put("RETAILER_ZONE", zone);
		payload.put("EMAIL", email);
		payload.put("BRAND", brand);
		payload.put("RETAILER_CONVNAME", "");
		payload.put("COUNTER_SIZE", "");
		payload.put("PROD_APP", "");
		payload.put("CUSTPOT", "");
		payload.put("ASO", "");
		payload.put("distGrade", "NO");
		payload.put("GSTIN", gstno);
		payload.put("GST_State_Code", gst_state_code);
		payload.put("GST_Regd_Typ", "");
		payload.put("Bhim", "");
		payload.put("SMName", "");
		payload.put("ploCatExclu", cat);
		payload.put("tier", tier);

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
		JSONObject obj = new JSONObject();
		obj = (JSONObject) parser.parse(api_result);
		boolean response = (Boolean) obj.get("Success");

		return response;
	}

	private static String getContactPerson(String dealer_code, String dist_code) throws ClassNotFoundException, SQLException {
		String con_person="";
		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String jdbcUrl = "jdbc:postgresql://52.172.178.25:5432/db_2_release";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		String sql="select contact_person from t_dealers where dealer_code ='"+dealer_code+"' and dist_code='"+dist_code+"'";
		//System.out.println("Query --> "+sql);
		
	    java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next()) {
			con_person =rs.getString("contact_person");}
		return con_person;
		
	}
	private static String get_zone(String state) throws ClassNotFoundException, SQLException {
		String zone = "";
		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String jdbcUrl = "jdbc:postgresql://52.172.178.25:5432/db_2_release";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		String sql = "select distinct(zone_name) from pincodes where state_name ='" + state + "'";
		
		System.out.println("Query --> "+sql);
		java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next()) {
			zone = rs.getString("zone_name");
		}
		zone = zone.toUpperCase();
		return zone;
	}

}
