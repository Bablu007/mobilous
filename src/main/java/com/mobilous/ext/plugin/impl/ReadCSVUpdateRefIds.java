package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.itextpdf.text.List;

public class ReadCSVUpdateRefIds 
{
	
	

	public static void main (String [] args) throws IOException, ClassNotFoundException, SQLException 
	{
		
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		String username = "postgres";
		String password = "0000";
		Class.forName("org.postgresql.Driver");
		
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
        String csvFile  = "/home/yashbansal/snap/skype/common/users_login_mongo.csv"; 
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int i=1;
        br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) 
            {
            	i++;
                // use comma as separator
                String[] ref = line.split(cvsSplitBy);
                System.out.println("processing " + i + "-->" +ref[15] +" --> ");
                select(con, ref[2],ref[15]);
            }
	
	}


	private static void select(Connection con, String user_id, String ref_id) throws ClassNotFoundException, SQLException {
		
		if (ref_id.equalsIgnoreCase("null"))
		{}
		else {
		
		String sql="select id from user_login where user_id='"+ref_id+"'";
		

		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String id="";
	    while (rs.next()) {
	    	
	    	id=rs.getString("id");
	    	System.out.print(id+"\n");
	    	int c=update(con, user_id,id);
	    	}
	}
	}

	private static int update(Connection con,String user_id, String id) throws SQLException {
		
		
		String sql="update user_login set cse_user_ref_id='"+id+"' where user_id='"+user_id+"'";
		PreparedStatement stmt = con.prepareStatement(sql);
		int c = stmt.executeUpdate();
		return c;
		
	}}
