package com.mobilous.ext.plugin.impl;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

public class GenerateOtp {

	public static String generateotp(String where)
			throws ParseException, IOException, ClassNotFoundException, SQLException, Exception {
		String api_result = "";
		String bearer_token = GenerateBearerToken.generatebearer();

		String end_point = "https://tslin-qa.apigee.net/smsproxy/SendOTP";
		JSONObject body = new JSONObject();
		body.put("mobNo", where);
		body.put("imeiNo", "");
		body.put("strMSG", "");

		JSONObject headers = new JSONObject();
		headers.put("content_type", "application/json");
		headers.put("accept", "json");
		headers.put("jwt-token", bearer_token);

		JSONObject payload = new JSONObject();
		payload.put("method", "post");
		payload.put("url", end_point);
		payload.put("parameters", body);
		payload.put("datatype", "json");
		payload.put("headers", headers);
		System.out.println("End Point Payload --> " + payload);

		StringEntity entity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("https://tslsampoorna.mobilous.com/run_api");
		System.out.println("End Point URL --> " + request);
		request.setEntity(entity);
		org.apache.http.HttpResponse response = httpClient.execute(request);
		HttpEntity respEntity = ((org.apache.http.HttpResponse) response).getEntity();
		if (respEntity != null) {
			api_result = EntityUtils.toString(respEntity);
		}
		System.out.println("End Point Result --> " + api_result);
		String status = saveotp(where, api_result);
		return status;
	}

	private static String saveotp(String where, String api_result) throws ClassNotFoundException, SQLException {

		String jdbcUrl = "jdbc:postgresql://tslsampoorna.mobilous.com:5432/db_582";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, (int) 5);
		cal.add(Calendar.MINUTE, (int) 30);
		java.util.Date utilDate = cal.getTime();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		PreparedStatement pstmt = con
				.prepareStatement("INSERT INTO  t_otp ( otp_code, phone_no, created_at ) values (?, ?, ?)");
		pstmt.setString(1, api_result);
		pstmt.setString(2, where);
		pstmt.setTimestamp(3, timestamp);
		int counter = pstmt.executeUpdate();
		if (counter == 1)
			return "true";
		else
			return "false";

	}

}
