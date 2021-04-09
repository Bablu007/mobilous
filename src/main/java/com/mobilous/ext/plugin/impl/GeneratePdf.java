package com.mobilous.ext.plugin.impl;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.channels.Channel;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.mail.Session;
import javax.validation.Path;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class GeneratePdf {
	public static String generatepdf (String where) throws  ClassNotFoundException, SQLException, DocumentException, MalformedURLException, IOException, URISyntaxException, JSchException, SftpException {
	
		StringTokenizer stk= new  StringTokenizer(where , ";");
		
		Connection con= getconnection();
		String to_mail_id=stk.nextToken();
	    String sale_id=stk.nextToken();
	    
		java.sql.Statement stmnt  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		String sql="select * from sales where sale_id="+sale_id;
		System.out.println("[[  [[ SQL Query --> get sales details ]] [["+sql+" ]]  ]]\n");
		String customer_name="";
		Date date = null;
		String address="";
		String district="";
		String state="";
		String customer_mobile="";
		String invoice_no="";
		String created_by_userid ="";
		String user_name="";
		String user_phone_no="";
		ResultSet rs = stmnt.executeQuery(sql);
		while (rs.next())
		{
				customer_name = rs.getString("customer_name");
				date = rs.getDate("created_at");
				address=rs.getString("address");
				district=rs.getString("district_name");
				state=rs.getString("state_name");
				customer_mobile=rs.getString("customer_mobile");
				invoice_no=rs.getString("invoice_no");
				created_by_userid=rs.getString("created_by_userid");
		}
		
		String sql3="select * from user_login where id='"+created_by_userid+"'";
		java.sql.Statement stmnt3  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet rs3 = stmnt3.executeQuery(sql3);
		while (rs3.next())
		{
			user_name=rs3.getString("user_name");
			user_phone_no=rs3.getString("user_phone_no");
			
		}
		
		System.out.println("[[  [[ Response of SQL Query Customer Name --> ]]  [[ "+customer_name+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query Sale Creation Date --> ]]  [[ "+date+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query Address --> ]]  [[ "+address+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query District --> ]]  [[ "+district+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query State --> ]]  [[ "+state+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query Customer Mobile --> ]]  [[ "+customer_mobile+" ]]  ]]\n");
		System.out.println("[[  [[ Response of SQL Query Invoice No. --> ]]  [[ "+invoice_no+" ]]  ]]\n");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String f_date= sdf.format(date);
		
	    String text="Hi "+customer_name+",\nYour TATA Tiscon order dated ("+f_date+") has been successfully processed.\nFind below, your invoice numbered "+invoice_no+".\n\n";
		
		System.out.println("[[ Creating Invoice PDF ]]\n");
		Document document = new Document( PageSize.A4,60,60,30,30);
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/var/www/html/sale_invoice_pdf/sale_invoice_pdf.pdf"));    
	    document.open();
	    document.addAuthor("Yash Bansal");
	    document.addCreator("Mobilous Inc");
	    document.addTitle("Invoice Deatils");
	    document.addSubject("TATA Tiscon SD Sale Details");
	    
	    Image image1 = Image.getInstance("/var/www/html/sale_invoice_pdf/tata_banner_logo.png");
	    image1.scaleAbsolute(130, 35);
	    image1.setAbsolutePosition(30, 790);
	    document.add(image1);
	    
	    Image image2 = Image.getInstance("/var/www/html/sale_invoice_pdf/tata_sampoorna_logo.png");
	    image2.scaleAbsolute(150, 35);
	    image2.setAbsolutePosition(420, 790);
	    document.add(image2);
	    
	    Image image3 = Image.getInstance("/var/www/html/sale_invoice_pdf/yellow_heading.png");
	    image3.scaleAbsolute(595, 90);
	    image3.setAbsolutePosition(0, 675);
	    document.add(image3);
	    
	    
	    
	    Font top_heading = new Font(Font.FontFamily.TIMES_ROMAN,26,Font.BOLD,BaseColor.WHITE);
	    Font name_line = new Font(Font.FontFamily.TIMES_ROMAN,22, Font.BOLD);
	    Font headers= new Font(Font.FontFamily.TIMES_ROMAN, 15,Font.BOLD);
	    Font bill= new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD);
	    Font data= new Font(Font.FontFamily.TIMES_ROMAN, 16);
	    Font data_italic= new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.ITALIC);
	    
	    Paragraph p00=new Paragraph("Pro Forma Invoice",name_line);
	    p00.setSpacingAfter(10);
	    p00.setAlignment(Element.ALIGN_CENTER);
	    document.add(p00);
	    
	    
	    Paragraph p0=new Paragraph("Thank you for building\nyour dream home with us.\n",top_heading);
	    p0.setAlignment(Element.ALIGN_LEFT);
	    p0.setSpacingAfter(15);
	    document.add(p0);
	    
	    
	    Paragraph p1=new Paragraph("Hi "+customer_name+",",name_line);
	    p1.setSpacingAfter(3);
	    document.add(p1);
	    
	    Paragraph p2=new Paragraph("Your TATA Tiscon order dated \n("+f_date+") has been successfully processed.",headers);
	    document.add(p2);
	    
	    Paragraph p3=new Paragraph("Find below, your invoice numbered "+invoice_no+".",headers);
	    p3.setSpacingAfter(15);
	    document.add(p3);
	    
	    PdfPTable skus = new PdfPTable(new float [] {0.7f,1.7f,2,1,1});
        skus.setWidthPercentage(100);
        
        //Row 1 with Headers
        PdfPCell s_no = new PdfPCell(new Phrase("S.NO.",headers));
        s_no.setHorizontalAlignment(Element.ALIGN_CENTER);
        s_no.setVerticalAlignment(Element.ALIGN_MIDDLE);
        s_no.setBorderColor(BaseColor.GRAY);
        s_no.setBorderWidth(1);
        s_no.setPaddingTop(2);
        s_no.setPaddingBottom(6);
	    skus.addCell(s_no);
        
        PdfPCell p_name = new PdfPCell(new Phrase("Product Name",headers));
        p_name.setHorizontalAlignment(Element.ALIGN_CENTER);
        p_name.setVerticalAlignment(Element.ALIGN_MIDDLE);
        p_name.setBorderColor(BaseColor.GRAY);
        p_name.setBorderWidth(1);
        p_name.setPaddingTop(2);
        p_name.setPaddingBottom(6);
	    skus.addCell(p_name);
	    
	    
	    PdfPCell p_dimension = new PdfPCell(new Phrase("Product Dimensions\n(in mm)",headers));
	    p_dimension.setHorizontalAlignment(Element.ALIGN_CENTER);
	    p_dimension.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    p_dimension.setBorderColor(BaseColor.GRAY);
	    p_dimension.setBorderWidth(1);
	    p_dimension.setPaddingTop(2);
	    p_dimension.setPaddingBottom(6);
	    skus.addCell(p_dimension);
	    
	    PdfPCell quantity= new PdfPCell(new Phrase("Quantity\n(in pcs)",headers));
	    quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
	    quantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    quantity.setBorderColor(BaseColor.GRAY);
	    quantity.setBorderWidth(1);
	    quantity.setPaddingTop(2);
	    quantity.setPaddingBottom(6);
	    skus.addCell(quantity);
	    
	    PdfPCell mt= new PdfPCell(new Phrase("Tonnage\n(in MT)",headers));
	    mt.setHorizontalAlignment(Element.ALIGN_CENTER);
	    mt.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    mt.setBorderColor(BaseColor.GRAY);
	    mt.setBorderWidth(1);
	    mt.setPaddingTop(2);
	    mt.setPaddingBottom(6);
	    skus.addCell(mt);
	    skus.completeRow();
	    
	    //Dynamic Rows with data
	    java.sql.Statement stmnt1  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		String sql1="select * from sale_info where sale_id="+sale_id+" order by cast(dimensions as integer) asc";
		ResultSet rs1 = stmnt1.executeQuery(sql1);
		
		java.sql.Statement stmnt2  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		String sql2="select sum(cumulative_quantity_in_mt::double precision) as result from sale_info where sale_id="+sale_id;
		ResultSet rs2 = stmnt2.executeQuery(sql2);
		double total_result = 0;
		while (rs2.next())
		{
			 total_result=rs2.getDouble("result");
		}
		//DecimalFormat df= new DecimalFormat("##########.###");
		//df.setRoundingMode(RoundingMode.DOWN);
		String sum=String.valueOf(total_result); 
		sum=getdecimal(sum);
		int i=0;
		String s="";
		System.out.println("[[  [[ Response of SQL Query Cumulative Quantity --> ]]  [[ "+sum+" ]]  ]]\n");
		while (rs1.next())
			{
				i++;
				s=String.valueOf(i);
				PdfPCell sno= new PdfPCell(new Phrase(s,data));
				sno.setHorizontalAlignment(Element.ALIGN_CENTER);
				sno.setVerticalAlignment(Element.ALIGN_MIDDLE);
				sno.setBorderColor(BaseColor.GRAY);
				sno.setBorderWidth(1);
				sno.setPaddingTop(4);
				sno.setPaddingBottom(8);
				skus.addCell(sno);
	    	
	    		PdfPCell tiscon= new PdfPCell(new Phrase("TATA Tiscon SD",data));
	    		tiscon.setHorizontalAlignment(Element.ALIGN_CENTER);
	    		tiscon.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    		tiscon.setBorderColor(BaseColor.GRAY);
	    		tiscon.setBorderWidth(1);
	    		tiscon.setPaddingTop(4);
	    		tiscon.setPaddingBottom(8);
	    		skus.addCell(tiscon);
	    		
	    		String dimension_data=rs1.getString("dimensions");
	    		PdfPCell dimension= new PdfPCell(new Phrase(dimension_data,data));
	    		dimension.setHorizontalAlignment(Element.ALIGN_CENTER);
	    		dimension.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    		dimension.setBorderColor(BaseColor.GRAY);
	    		dimension.setBorderWidth(1);
	    		dimension.setPaddingTop(4);
	    		dimension.setPaddingBottom(8);
	    		skus.addCell(dimension);
	    	
	    		String cumulative_quantity_data=rs1.getString("cumulative_quantity");
	    		PdfPCell cumulative_quantity= new PdfPCell(new Phrase(cumulative_quantity_data,data));
	    		cumulative_quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
	    		cumulative_quantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    		cumulative_quantity.setBorderColor(BaseColor.GRAY);
	    		cumulative_quantity.setBorderWidth(1);
	    		cumulative_quantity.setPaddingTop(4);
	    		cumulative_quantity.setPaddingBottom(8);
	    		skus.addCell(cumulative_quantity);
	    		
	    		String cumulative_quantity_in_mt_data=rs1.getString("cumulative_quantity_in_mt");
	    		String f=getdecimal(cumulative_quantity_in_mt_data);
	    		PdfPCell cumulative_quantity_in_mt= new PdfPCell(new Phrase(f,data_italic));
	    		cumulative_quantity_in_mt.setHorizontalAlignment(Element.ALIGN_CENTER);
	    		cumulative_quantity_in_mt.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    		cumulative_quantity_in_mt.setBorderColor(BaseColor.GRAY);
	    		cumulative_quantity_in_mt.setBorderWidth(1);
	    		cumulative_quantity_in_mt.setPaddingTop(4);
	    		cumulative_quantity_in_mt.setPaddingBottom(8);
	    		skus.addCell(cumulative_quantity_in_mt);
	    		skus.completeRow();
	    	
			}
		
		//Subtotal Row
		PdfPCell blank1=new PdfPCell(new Phrase("\n"));
		blank1.setColspan(3);
		blank1.setHorizontalAlignment(Element.ALIGN_CENTER);
		blank1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		blank1.setBorderColor(BaseColor.GRAY);
		blank1.setBorderWidth(1);
		blank1.setPaddingTop(4);
		blank1.setPaddingBottom(8);
		skus.addCell(blank1);
		
		PdfPCell total=new PdfPCell(new Phrase("Total",headers));
		total.setHorizontalAlignment(Element.ALIGN_CENTER);
		total.setVerticalAlignment(Element.ALIGN_MIDDLE);
		total.setBorderColor(BaseColor.GRAY);
		total.setBorderWidth(1);
		total.setPaddingTop(4);
		total.setPaddingBottom(8);
		skus.addCell(total);

		PdfPCell total_value=new PdfPCell(new Phrase(sum,data_italic));
		total_value.setHorizontalAlignment(Element.ALIGN_CENTER);
		total_value.setVerticalAlignment(Element.ALIGN_MIDDLE);
		total_value.setBorderColor(BaseColor.GRAY);
		total_value.setBorderWidth(1);
		total_value.setPaddingTop(4);
		total_value.setPaddingBottom(8);
		skus.addCell(total_value);
		skus.completeRow();
		
		skus.setSpacingAfter(2);
		document.add(skus);

	   
	    Paragraph p4=new Paragraph("Billing Address:",bill);
	    p4.setSpacingAfter(2);
	    document.add(p4);
	    
	    Paragraph p5=new Paragraph("");
	    document.add(p5);
	    
	    Paragraph p6=new Paragraph(address+", "+district+", "+state+",",data);
	    document.add(p6);
	    
	    Paragraph p7=new Paragraph(customer_name+",",data);
	    document.add(p7);
	    
	    Paragraph p8=new Paragraph(customer_mobile+",",data);
	    document.add(p8);
	    
	    Paragraph p9=new Paragraph(to_mail_id,data);
	    document.add(p9);
	    
	    Paragraph p12=new Paragraph("Dealer Details : "+user_name+", "+user_phone_no,data);
	    p12.setSpacingAfter(12);
	    document.add(p12);
	    
	    
	    Paragraph p11=new Paragraph("Thank you for shopping TATA Tiscon with us.",data);
	    document.add(p11);
	    
	    Image image4 = Image.getInstance("/var/www/html/sale_invoice_pdf/social_info.jpg");
	    image4.scaleAbsolute(595, 90);
	    image4.setAbsolutePosition(0, 0);
	    document.add(image4);
	    document.close();
	    System.out.println("[[ Invoice PDF Created Successfully ]]");
	    sftp();
	    String response = SendEmail.sendemail(to_mail_id, text);
	    System.out.println("[[ Mail Sent... ]]");
		return "";
	    
	}
	private static void sftp() throws JSchException, UnknownHostException, SftpException, FileNotFoundException {
		
		System.out.println("[[ Transfering File to Cron Server... ]]");
		String ip="104.211.73.197";
		//ip=InetAddress.getLocalHost().getHostAddress();
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
	    JSch jsch = new JSch();
	    jsch.addIdentity("/var/www/html/sale_invoice_pdf/mobilous_instance.key");
	    com.jcraft.jsch.Session jschSession = jsch.getSession(username, ip, 22);
	    jschSession.setPassword(password);
	    jschSession.setConfig("StrictHostKeyChecking", "no");
	    jschSession.connect();
	    ChannelSftp sftpChannel = (ChannelSftp) jschSession.openChannel("sftp");
	    sftpChannel.connect();
	    File file=new File("/var/www/html/sale_invoice_pdf/sale_invoice_pdf.pdf");
	    file.setExecutable(true);
	    file.setWritable(true);
	    sftpChannel.put(file.toString(), "/var/www/html/sale_invoice_pdf");
	    sftpChannel.exit();
		System.out.println("[[ File Uploaded Successfully to Cron Server... ]]");


		
	}
	private static String getdecimal(String s1) {
		
        String formatted = s1;
        int numDecimalPlaces = 3;
        int i = s1.indexOf('.');
        if (i != -1 && s1.length() > i + numDecimalPlaces) {
            formatted = s1.substring(0, i + numDecimalPlaces + 1);
        }
		return formatted;
	}

	
	public static Connection getconnection() throws ClassNotFoundException, SQLException 
	{
		String jdbcUrl = "jdbc:postgresql://52.172.178.25:5432/db_2_release";
		//String jdbcUrl = "jdbc:postgresql://localhost:5432/db_2";
		
		
		String username = "mobilous";
	    String password = "ctcpy9823\"x~";
		System.out.println();
		System.out.println("[[ Postgres DB Username : "+username+" ]]\n");
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(jdbcUrl, username, password);
		return con;
	}






}
