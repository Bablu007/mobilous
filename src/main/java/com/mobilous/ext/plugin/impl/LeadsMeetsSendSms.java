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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;

public class LeadsMeetsSendSms 
{
	public static void main (String [] args) throws ParseException, IOException, ClassNotFoundException, SQLException 
		{
		
		Connection con=getconnection();
		getuserdetails(con);
		
		}
	
	public static Connection getconnection() throws ClassNotFoundException, SQLException 
	{
		
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
	    String username = "mobilous";
	    String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		return con;
		
	}
	
	public static void getuserdetails(Connection con) throws SQLException, ClassNotFoundException, ParseException, IOException 
		{
		
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String today_date = format.format(cal.getTime());
		
		String sql="select leads_meet_id,user_id,lead_meet_remarks from t_leads_meet where lead_meet_date='"+today_date+"'";
		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String user_id="";
		String lead_meet_remarks="";
		String leads_meet_id="";
		String mobile="";
	    while (rs.next())
	    {
	    	user_id=rs.getString("user_id");
	    	lead_meet_remarks=rs.getString("lead_meet_remarks");
	    	leads_meet_id=rs.getString("leads_meet_id");
	    	mobile=getmobile(con, user_id);
	    	sendsms(mobile, lead_meet_remarks);
	    	updatesmsstatus(con, leads_meet_id);
	    	
	    	
	    }
		
		}
		
		private static String getmobile(Connection con, String user_id) throws SQLException {
			
			String sql="select user_phone_no from user_login where id='"+user_id+"'";
			java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmnt.executeQuery(sql);
			String mobile="";
		    while (rs.next())
		    {
		    	mobile=rs.getString("user_phone_no");
		    }
			
		return mobile;
	}

		public static void sendsms(String mobile, String lead_meet_remarks) throws ParseException, IOException {
		
		String whr="";
		String bearer_token=GenerateBearerToken.generatebearer(whr);
		
		URL url=new URL("https://tslapps.tatasteel.co.in/eca/api/SendSMS/SendSMSDist");
		JSONObject payload=new JSONObject();
		payload.put("DistName","");
		payload.put("DistMobileNo",mobile);
		payload.put("SMS",lead_meet_remarks); 
		payload.put("DistID","");

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
	    java.util.Date date=new java.util.Date();
        System.out.println("[[ Sms Sent to "+mobile+" on "+date+" ]]" );
        System.out.println();
	    
	}

		public static void updatesmsstatus(Connection con, String leads_meet_id) throws SQLException, ClassNotFoundException, ParseException, IOException 
			{
			String update_query="update  t_leads_meet set sms_status='true' where leads_meet_id="+leads_meet_id+"";
			PreparedStatement stmt = con.prepareStatement(update_query);
            int c = stmt.executeUpdate();
		    }

}
