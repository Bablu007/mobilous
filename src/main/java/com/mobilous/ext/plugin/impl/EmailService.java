package com.mobilous.ext.plugin.impl;


import java.io.File;


import java.io.IOException;
import java.nio.channels.Channel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;    
import javax.mail.internet.*;
import java.util.StringTokenizer;



public class EmailService
{  
    public static void main(String [] args) throws IOException{  
    	
    	//StringTokenizer st = new StringTokenizer(where,";");
		
		final String from = "sampoorna@tatasteel.com";
		final String password = "Help@123";
		String to = "yash.bansal@mobilous.com";
	//	String to = st.nextToken();
		String subject = "test";
		String message = "test";
		
		String host = "smtp-mail.outlook.com";
		
		Properties p = new Properties();
	  //  p.put("mail.transport.protocol", "smtp");

		p.put("mail.smtp.host", host);
		p.put("mail.smtp.port", "587");
		p.put("mail.smtp..enable", "true");
		p.put("mail.smtp.starttls.enable", "true");

		p.put("mail.smtp.auth", "true");

	   // p.put("mail.debug", "true");

		p.put("mail.smtp.socketFactory.port", "465");
	    p.put("mail.smtp.socketFactory.class",
	            "javax.net.ssl.SSLSocketFactory");
        
		
		
		Session session = Session.getDefaultInstance(p,

	            new javax.mail.Authenticator() {

	                protected PasswordAuthentication getPasswordAuthentication() {

	                return new PasswordAuthentication(from, password);


	                }

	            });
		
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			msg.setSubject(subject);
	        msg.setText("This is actual message");

			//MimeBodyPart Part1 = new MimeBodyPart();
			//Part1.setText(message);
			
			//MimeMultipart manyParts = new MimeMultipart();
			//manyParts.addBodyPart(Part1);
			
			//Part1 = new MimeBodyPart();
			
			//String filename = "/tmp/data.csv"; 
			
			
			//FileDataSource source = new FileDataSource(filename);  
		    //Part1.setDataHandler(new DataHandler(source));  
		    //Part1.setFileName(source.getFile().getName()); 
		    //manyParts.addBodyPart(Part1);
		    
		   
		        
		    //MimeMultipart multipart = new MimeMultipart();  
		    //multipart.addBodyPart(Part2);  
		  
			System.out.println("Mail sent successfully1");
			//Transport transport = session.getTransport("smtp");
			//msg.setContent(manyParts);

			//transport.connect(host, from, password);
			//transport.sendMessage(msg, msg.getAllRecipients());
			
			//transport.close();
			Transport.send(msg);
			System.out.println("Mail sent successfully");
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		
       // return("Mail sent successfully");

		
		
		
}
    
    
        
        
        
        
}