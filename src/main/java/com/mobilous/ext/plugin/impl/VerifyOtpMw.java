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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;

public class VerifyOtpMw {

	public static String verifyotpmw(String where)
			throws ParseException, IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		StringTokenizer stk = new StringTokenizer(where, ";");
		String mobile = stk.nextToken();
		String otp = stk.nextToken();
		String status = search_otp(mobile, otp);
		return status;
	}

	private static String search_otp(String mobile, String otp) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {

		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String jdbcUrl = "jdbc:postgresql://tslsampoorna.mobilous.com:5432/db_582";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
	    String sql="select otp_code from t_otp where phone_no='"+mobile+"' order by created_at desc limit 1";
		
	    java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String otp_res = "";
		while (rs.next()) {
			otp_res = rs.getString("otp_code");

		}
		if (otp_res.equalsIgnoreCase(otp)) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String msg = mobile + "otpsuccess";
			md.update(msg.getBytes());
			byte[] digest = md.digest();
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < digest.length; i++) {
				String hex = Integer.toHexString(0xFF & digest[i]);
				if(hex.length()==1)
					hexString.append('0');
					hexString.append(hex);
			}
			String response = hexString.toString();
			return response;
		} else
			return "false";

	}

}
