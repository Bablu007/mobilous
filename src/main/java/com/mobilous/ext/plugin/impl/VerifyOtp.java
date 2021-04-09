package com.mobilous.ext.plugin.impl;


import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.StringTokenizer;

public class VerifyOtp {
	
	
	public static String verifyotp (String where) throws ParseException, IOException, ClassNotFoundException, SQLException {
		StringTokenizer stk=new StringTokenizer(where, ";");
		String mobile=stk.nextToken();
		String otp=stk.nextToken();
		String status=search_otp(mobile, otp);
	    return status;
	}

	private static String search_otp(String mobile, String otp) throws ClassNotFoundException, SQLException {
		
		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String jdbcUrl = "jdbc:postgresql://tslsampoorna.mobilous.com:5432/db_582";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		Calendar cal = Calendar.getInstance();
		java.util.Date utilDate=cal.getTime();
	    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
	    String sql="select otp_code from t_otp where phone_no='"+mobile+"' order by created_at desc limit 1";

		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String otp_res = "";
		while (rs.next())
	    		{
			 otp_res =rs.getString("otp_code");
			
			}
		if(otp_res.equalsIgnoreCase(otp))
	    	return "true";
	    else
	    	return "false";
	    	
		
		
	}

}
