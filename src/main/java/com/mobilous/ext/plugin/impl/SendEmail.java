package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.json.simple.JSONObject;

public class SendEmail {
	
public static String sendemail(String to_mail_id, String text) throws IOException 
	{
	
	System.out.println("[[ Sending Mail... ]]");

	URL url=new URL("http://104.211.73.197:8080/sendEmailWithAttachment");
	
	JSONObject payload=new JSONObject();
	payload.put("emailSubject","Tata Tiscon order details");
	payload.put("emailTo",to_mail_id);
	payload.put("emailBody",text); 
	payload.put("emailFilePath","/var/www/html/sale_invoice_pdf/sale_invoice_pdf.pdf");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestProperty("Content-type", "application/json");
	conn.setDoOutput(true);
	
	OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	writer.write(payload.toString());
	writer.flush();
	
	InputStreamReader in = new InputStreamReader(conn.getInputStream());
    BufferedReader br = new BufferedReader(in);
    String api_output="";
    String api_result="";
    while ((api_output = br.readLine()) != null) 
    {
    	api_result+=api_output;
    }
	return api_result;
	
		}

	}
