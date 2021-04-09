package com.mobilous.ext.plugin.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.json.simple.JSONObject;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SendReportEmail {

	public static void sendemail(String report_type) throws IOException, ClassNotFoundException, SQLException, JSchException, SftpException {

		System.out.println("[[ Transfering File to Cron Server... ]]");
		String ip = "52.172.178.25";
		String username = "mobilous";
		String password = "ctcpy9823\"x~";
		JSch jsch = new JSch();
		jsch.addIdentity("/var/www/html/synergy_reports_mail/mobilous_instance.key");
		com.jcraft.jsch.Session jschSession = jsch.getSession(username, ip, 22);
		jschSession.setPassword(password);
		jschSession.setConfig("StrictHostKeyChecking", "no");
		jschSession.connect();
		ChannelSftp sftpChannel = (ChannelSftp) jschSession.openChannel("sftp");
		sftpChannel.connect();
		File file = new File("/var/www/html/synergy_reports_mail/"+report_type);
		file.setExecutable(true);
		file.setWritable(true);
		sftpChannel.get(file.toString(), "/var/www/html/synergy_reports_mail");
		sftpChannel.exit();
		System.out.println("[[ File Uploaded Successfully to Cron Server... ]]");

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.HOUR, (int) 6);
		cal.add(Calendar.DATE, -1);
		String date = format.format(cal.getTime());
		String jdbcUrl = "jdbc:postgresql://52.172.178.25:5432/db_2_release";
		
		String query;
		Class.forName("org.postgresql.Driver");
		System.out.println("Entered in connection");
		String sql = "";
		Connection con = (Connection) DriverManager.getConnection(jdbcUrl, username, password);
		java.sql.Statement stmnt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmnt.executeQuery(sql);
		String to_mail_id = "";
		while (rs.next()) {

			to_mail_id = rs.getString("mail_addr");
			URL url = new URL("http://104.211.73.197:8080/sendEmailWithAttachment");
			JSONObject payload = new JSONObject();
			payload.put("emailSubject", "Leads and Sales reports dt. " + date);
			payload.put("emailTo", to_mail_id);
			payload.put("emailBody", "Hello, PFA leads and sales reports for " + date);
			payload.put("emailFilePath", "/var/www/html/synergy_reports_mail/" + report_type);
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

		}

	}
}