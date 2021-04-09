package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.json.simple.JSONObject;

public class SendCMTEmail {

	public static String sendemail(String where) throws Exception {

		StringTokenizer stk = new StringTokenizer(where, ";");

		Connection con = getCon();
		String dist_code = stk.nextToken();
		String old_cat = stk.nextToken();
		String new_cat = stk.nextToken();
		String old_subcat = stk.nextToken();
		String new_subcat = stk.nextToken();
		String dealer_code = stk.nextToken();
		String dealer_name = stk.nextToken();
		String dealer_mob = stk.nextToken();

		ArrayList al_cc1 = new ArrayList();
		ArrayList al_cc2 = new ArrayList();
		ArrayList al_cc = new ArrayList();
		ArrayList al_to = new ArrayList();

		al_cc1 = getSmEmails(dist_code, con);
		al_cc2 = getCMTEmails("cc", con);
		al_cc.addAll(al_cc1);
		al_cc.addAll(al_cc2);
		al_cc.removeAll(Collections.singleton(null));
		
		
		al_to = getCMTEmails("to", con);
		al_to.removeAll(Collections.singleton(null));
		
		
		String dist_name = getDistName(dist_code, con);
		System.out.println("[[ Sending Mail... ]]");

		URL url = new URL("http://104.211.73.197:8080/sendEmail");
		JSONObject payload = new JSONObject();
		String message = "";
		message += "<html><body><p><br>";
		message += "Hi,<br><br>";
		message += "Greetings for the day!<br><br>Please find below the change request from " + dist_name + " ("
				+ dist_code + ").";
		message += "<br><br><br>Dealer Shop Name : " + dealer_name + "<br><br>Dealer Contact Number : " + dealer_mob
				+ "<br><br>Dealer Code : " + dealer_code + "<br><br><p>";
		message += "<style>table, th, td{ border: 1px solid black;  border-collapse: collapse; text-align : center } th, td { padding : 10px;}";
		message += "</style><table style=\"width:1000\">";
		message += "<tr><td>Header</td><td>Existing</td><td>Requested</td>";
		message += "<tr><td>Category</td><td>" + old_cat + "</td><td>" + new_cat + "</td>";
		message += "<tr><td>Sub-category</td><td>" + old_subcat + "</td><td>" + new_subcat + "</td></table>";
		message += "<br><p>Kindly approve or reject using super admin on Tata Sampoorna App. Please intimate the distributor once done.<p><br>";

		payload.put("emailSubject", "Dealer Details Change Request");
		payload.put("emailTo", al_to);
		payload.put("emailCc", al_cc);
		payload.put("emailBody", "E-Mail-Client not supported");
		payload.put("htmlBody", message);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
		return "success";
	}

	private static String getDistName(String dist_code, Connection con) throws Exception {
		String sql = "select user_name from user_login where user_id='" + dist_code + "'";

		java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String dist_name = "";
		while (rs.next()) {
			dist_name = rs.getString("user_name");

		}
		return dist_name;
	}

	private static ArrayList getCMTEmails(String status, Connection con) throws Exception {

		String sql = "select email_id from cmt_cateogy_mails where status='" + status + "'";

		java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		ArrayList al = new ArrayList();
		while (rs.next()) {
			al.add(rs.getString("email_id"));

		}
		return al;

	}

	private static ArrayList getSmEmails(String dist_code, Connection con) throws Exception {

		String sql = "select user_email from user_login where user_id in (select sm_phone_no from t_sm_mapping where dist_code='"
				+ dist_code + "') or user_id='" + dist_code + "'";
		// System.out.println(sql);
		java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		ArrayList al = new ArrayList();
		while (rs.next()) {
			al.add(rs.getString("user_email"));

		}
		return al;
	}

	private static Connection getCon() throws ClassNotFoundException, SQLException {

		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String jdbcUrl = "jdbc:postgresql://52.172.178.25:5432/db_2_release";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		
		return con;

	}

}
