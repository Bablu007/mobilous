package com.mobilous.ext.plugin.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertTargets {
	
	public static void main(String [] args) throws Exception, SQLException {
	
	Connection con=getConnection();
	
	InsertAsos(con);
	InsertDealers(con);
	InsertBms(con);
	InsertDist(con);
	InsertCse(con);
	
	}
	
	private static void InsertAsos(Connection con) throws Exception {
		
		System.out.println("Inserting ASO targets...");
		
		String leads_target_value="";
		String meets_target_value="";
		String aso_id="";
		String dist_ref_userid="";
		String dist_ref_id="";
		String dealers_count="";
		
		String sql="select leads, meets from t_tc_target where user_role='ASO'";
		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next())
	    {
	    	leads_target_value=rs.getString("leads");
			meets_target_value=rs.getString("meets");
	    }
		
		String sql1="select id, user_ref_dist_id from user_login where user_role='ASO'";
		System.out.println(sql1);
		java.sql.Statement stmnt1  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs1 = stmnt1.executeQuery(sql1);
		while (rs1.next())
	    {
			aso_id=rs1.getString("id");
			dist_ref_userid=rs1.getString("user_ref_dist_id");
		System.out.println(aso_id);
		System.out.println(dist_ref_userid);
		String sql2="select id from user_login where user_id='"+dist_ref_userid+"'";
		System.out.println(sql2);
		java.sql.Statement stmnt2  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs2 = stmnt2.executeQuery(sql2);
		while (rs2.next())
	    {
			dist_ref_id=rs2.getString("id");
	    }
		System.out.println(dist_ref_id);
		String sql3="select count(*) from user_login where user_role='DEALER' and aso_user_ref_id='"+aso_id+"'";
		System.out.println(sql3);
		java.sql.Statement stmnt3  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs3 = stmnt3.executeQuery(sql3);
		while (rs3.next())
	    {
			dealers_count=rs2.getString("count");
	    }
		
		dealers_count=String.valueOf( Integer.parseInt(dealers_count)*20);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    cal.add(Calendar.HOUR, (int) 5.5);
	    java.util.Date utilDate=cal.getTime();
	    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO t_target_compliance (created_by, target_user_id, leads_target, meets_target, sales_target, created_at) values (?, ?, ?, ?, ?)");
		pstmt.setString(1, dist_ref_id);
	    pstmt.setString(2, aso_id);
	    pstmt.setString(3, leads_target_value);
	    pstmt.setString(4, meets_target_value);
	    pstmt.setString(5, dealers_count);
	    pstmt.setTimestamp(6, timestamp);
	   
	    
	    int counter=pstmt.executeUpdate(); 
	    }
	}
	
public static void InsertDealers(Connection con) throws Exception {
		
		System.out.println("Inserting DEALER targets...");
		
		String leads_target_value="";
		String sales_target_value="";
		String dlr_id="";
		String dist_ref_userid="";
		String dist_ref_id="";
		
		String sql="select leads, sales from t_tc_target where user_role='DEALER'";
		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next())
	    {
	    	leads_target_value=rs.getString("leads");
			sales_target_value=rs.getString("sales");
	    }
		
		String sql1="select id, user_ref_dist_id from user_login where user_role='DEALER'";
		java.sql.Statement stmnt1  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs1 = stmnt1.executeQuery(sql1);
		while (rs1.next())
	    {
			dlr_id=rs1.getString("id");
			dist_ref_userid=rs1.getString("user_ref_dist_id");
	    
		String sql2="select id from user_login where user_id='"+dist_ref_userid+"'";
		java.sql.Statement stmnt2  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs2 = stmnt2.executeQuery(sql2);
		while (rs2.next())
	    {
			dist_ref_id=rs2.getString("id");
	    }
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    cal.add(Calendar.HOUR, (int) 5.5);
	    java.util.Date utilDate=cal.getTime();
	    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO t_target_compliance (created_by, target_user_id, leads_target, sales_target, created_at) values (?, ?, ?, ?, ?)");
		pstmt.setString(1, dist_ref_id);
	    pstmt.setString(2, dlr_id);
	    pstmt.setString(3, leads_target_value);
	    pstmt.setString(4, sales_target_value);
	    pstmt.setTimestamp(5, timestamp);
	   
	    
	    int counter=pstmt.executeUpdate(); 
	    }
	}

public static void InsertBms(Connection con) throws Exception {
	
	System.out.println("Inserting BM targets...");
	
	String leads_target_value="";
	String meets_target_value="";
	String sales_target_value="";
	String bm_id="";
	String dist_ref_userid="";
	String dist_ref_id="";
	String asos_count="";
	
	String sql="select id, user_ref_dist_id from user_login where user_role='BM'";
	java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs = stmnt.executeQuery(sql);
	while (rs.next())
    {
		bm_id=rs.getString("id");
		dist_ref_userid=rs.getString("user_ref_dist_id");
    
	String sql1="select id from user_login where user_id='"+dist_ref_userid+"'";
	System.out.println(sql1);
	java.sql.Statement stmnt1  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs1 = stmnt1.executeQuery(sql1);
	while (rs1.next())
    {
		dist_ref_id=rs1.getString("id");
    }
	
	String sql2="select count(*) from user_login where user_role='ASO' and bm_user_ref_id='"+bm_id+"'";
	System.out.println(sql2);
	java.sql.Statement stmnt2  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs2 = stmnt2.executeQuery(sql2);
	while (rs2.next())
    {
		asos_count=rs2.getString("count");
    }
	
	leads_target_value=String.valueOf( Integer.parseInt(asos_count)*100);
	meets_target_value=String.valueOf( Integer.parseInt(asos_count)*2);
	
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    cal.add(Calendar.HOUR, (int) 5.5);
    java.util.Date utilDate=cal.getTime();
    java.sql.Date date = new java.sql.Date(utilDate.getTime());
    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
	
	String sql3="select sum(sales_target) as count from t_target_compliance where target_user_id in ( select id from user_login where user_role='ASO' and bm_user_ref_id='"+bm_id+"') and created_at::date='"+date+"'";
	System.out.println(sql3);
	java.sql.Statement stmnt3  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs3 = stmnt3.executeQuery(sql3);
	while (rs3.next())
    {
		sales_target_value=rs3.getString("count");
    }
	
	
	
	
	
	PreparedStatement pstmt = con.prepareStatement("INSERT INTO t_target_compliance (created_by, target_user_id, leads_target, meets_target, sales_target, created_at) values (?, ?, ?, ?, ?)");
	pstmt.setString(1, dist_ref_id);
    pstmt.setString(2, bm_id);
    pstmt.setString(3, leads_target_value);
    pstmt.setString(4, meets_target_value);
    pstmt.setString(5, sales_target_value);
    pstmt.setTimestamp(6, timestamp);
   
    
    int counter=pstmt.executeUpdate(); 
    }
}

public static void InsertDist(Connection con) throws Exception {
	
	System.out.println("Inserting DISTRIBUTOR targets...");
	
	String leads_target_value="";
	String meets_target_value="";
	String sales_target_value="";
	String dist_id="";
	String dist_userid="";
	
	String asos_count="";
	
	String sql="select id, user_id from user_login where user_role='DISTRIBUTOR'";
	java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs = stmnt.executeQuery(sql);
	while (rs.next())
    {
		dist_id=rs.getString("id");
		dist_userid=rs.getString("user_id");
		
		
		
    Calendar cal = Calendar.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    cal.add(Calendar.HOUR, (int) 5.5);
    java.util.Date utilDate=cal.getTime();
    java.sql.Date date = new java.sql.Date(utilDate.getTime());
    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
	
	String sql3="select sum(leads_target) as leads, sum(meets_target) as meets, sum(sales_target) as sales  from t_target_compliance where target_user_id in ( select id from user_login where user_ref_dist_id='"+dist_userid+"' and (user_role='CSE' or user_role='ASO' or user_role='DEALER')) and created_at::date='"+date+"'";
	System.out.println(sql3);
	java.sql.Statement stmnt3  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs3 = stmnt3.executeQuery(sql3);
	while (rs3.next())
    {
		leads_target_value=rs3.getString("leads");
		meets_target_value=rs3.getString("meets");
		sales_target_value=rs3.getString("sales");
    }
	
	
	
	
	
	PreparedStatement pstmt = con.prepareStatement("INSERT INTO t_target_compliance (created_by, target_user_id, leads_target, meets_target, sales_target, created_at) values (?, ?, ?, ?, ?)");
	pstmt.setString(1, dist_id);
    pstmt.setString(2, dist_id);
    pstmt.setString(3, leads_target_value);
    pstmt.setString(4, meets_target_value);
    pstmt.setString(5, sales_target_value);
    pstmt.setTimestamp(6, timestamp);
   
    
    int counter=pstmt.executeUpdate(); 
    }
}


 public static void InsertCse(Connection con) throws Exception {
		
		System.out.println("Inserting CSE targets...");
		
		String leads_target_value="";
		String meets_target_value="";
		String cse_id="";
		String dist_ref_userid="";
		String dist_ref_id="";
		
		String sql="select leads, meets from t_tc_target where user_role='CSE'";
		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next())
	    {
	    	leads_target_value=rs.getString("leads");
			meets_target_value=rs.getString("meets");
	    }
		
		String sql1="select id, user_ref_dist_id from user_login where user_role='CSE'";
		java.sql.Statement stmnt1  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs1 = stmnt1.executeQuery(sql1);
		while (rs1.next())
	    {
			cse_id=rs1.getString("id");
			dist_ref_userid=rs1.getString("user_ref_dist_id");
	    
		String sql2="select id from user_login where user_id='"+dist_ref_userid+"'";
		java.sql.Statement stmnt2  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs2 = stmnt2.executeQuery(sql2);
		while (rs2.next())
	    {
			dist_ref_id=rs2.getString("id");
	    }
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    cal.add(Calendar.HOUR, (int) 5.5);
	    java.util.Date utilDate=cal.getTime();
	    java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO t_target_compliance (created_by, target_user_id, leads_target, meets_target, created_at) values (?, ?, ?, ?, ?)");
		pstmt.setString(1, dist_ref_id);
	    pstmt.setString(2, cse_id);
	    pstmt.setString(3, leads_target_value);
	    pstmt.setString(4, meets_target_value);
	    pstmt.setTimestamp(5, timestamp);
	   
	    
	    int counter=pstmt.executeUpdate(); 
	    }
	}



	public static Connection getConnection() throws ClassNotFoundException, SQLException 
	{
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_303";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		System.out.println();
		System.out.println("[[ Postgres DB Username : "+username+" ]]\n");
		System.out.println("[[ Postgres DB Password : "+password+" ]]\n");
		return con;
	}
	

}
