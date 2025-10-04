package com.pcc.sys.lib;

import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class FEmailClient {
	
	public static void main(String[] args) {
		try {
			test_send_emailV2(); 
			test_send_email_attachV2();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String ServerPort;
	private String hostname;
	private String subject;
	private String bodyMessage;
	private String from;
	private String[] to;
	private String[] cc;
	private String Pass;
	private ArrayList<String> attachfile;
	private String charset;
	private String bodyMessageType;
	private Message mimemessage = null;
	private int secureType = 0; //1=smtp ธรรมดา , 2=SSL, 3=TLS

	public FEmailClient() {
		//ค่า default
		secureType = 2; 
		charset = "windows-874";
		bodyMessageType = "text/html";
	}
	
	/**
	 * 
	 * @param hostName smtp server
	 * @param serverPort port ที่ใช้เชื่อมต่อ
	 * @param secureType 1=smtp ธรรมดา , 2=SSL, 3=TLS
	 * @param user user
	 * @param password password
	 */
	public FEmailClient(String hostName, String serverPort, int secureType, String user, String password) {
		
		this.setHostname(hostName);
		this.setServerPort(serverPort);
		this.setSecureType(secureType);
		this.setFrom(user);
		this.setPass(password);
		this.charset = "windows-874";
		this.bodyMessageType = "text/html";
		
	}

	public String getServerPort() {
		return this.ServerPort;
	}

	public void setServerPort(String serverPort) {
		this.ServerPort = serverPort;
	}

	public String[] getCc() {
		return this.cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getBodyMessage() {
		return this.bodyMessage;
	}

	public void setBodyMessage(String bodyMessage) {
		this.bodyMessage = bodyMessage;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getTo() {
		return this.to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public ArrayList<String> getAttachfile() {
		return this.attachfile;
	}

	public void setAttachfile(ArrayList<String> attachfile) {
		this.attachfile = attachfile;
	}

	public String getPass() {
		return this.Pass;
	}

	public void setPass(String pass) {
		this.Pass = pass;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getBodyMessageType() {
		return bodyMessageType;
	}

	public void setBodyMessageType(String bodyMessageType) {
		this.bodyMessageType = bodyMessageType;
	}

	/**
	 * การเข้ารหัส email ถ้าไม่ set จะเท่ากับ 3=TLS
	 * @return 1=smtp ธรรมดา , 2=SSL, 3=TLS
	 */
	public int getSecureType() {
		return secureType;
	}

	/**
	 *  การเข้ารหัส email ถ้าไม่ set จะเท่ากับ 3=TLS
	 * @param secureType 1=smtp ธรรมดา , 2=SSL, 3=TLS 
	 */
	public void setSecureType(int secureType) {
		this.secureType = secureType;
	}

	public void sendEmail() throws Exception {
		Properties props = new Properties();

		//กรณีธรรมดา
		if (secureType == 1) {
			props.put("mail.smtp.host", this.hostname);
			props.put("mail.smtp.port", this.ServerPort);
			props.put("mail.smtp.auth", "true");
			
		} else if (secureType == 2) { //กรณี SSL
			props.put("mail.smtp.host", this.hostname);
			props.put("mail.smtp.port", this.ServerPort);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", this.ServerPort);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
		} if (secureType == 3) { //กรณี TLS
			props.put("mail.smtp.host", this.hostname);
			props.put("mail.smtp.port", this.ServerPort);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			
		}

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, Pass);
					}
				});

		// Create a default MimeMessage object.
		mimemessage = new MimeMessage(session);

		// Set From: header field of the header.
		mimemessage.setFrom(new InternetAddress(from));

		// Set To: header field of the header.
		InternetAddress[] toEmail = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			toEmail[i] = new InternetAddress(to[i].trim());
		}
		mimemessage.setRecipients(Message.RecipientType.TO, toEmail);

		//== Set CC
		if (cc != null) {
			if (cc.length > 0) {
				if (!cc[0].equals("")) {//ตัวแรกไม่ว่าง
					InternetAddress[] ccEmail = new InternetAddress[cc.length];
					for (int v = 0; v < cc.length; v++) {
						if (!cc[v].equals("")) {
							ccEmail[v] = new InternetAddress(cc[v].trim());
						}
					}
					mimemessage.setRecipients(Message.RecipientType.CC, ccEmail);
				}
			}
		}

		// Set Subject: header field
		mimemessage.setSubject(subject);

		// Now set the actual message
		boolean is_attachfile = false;
		if (attachfile != null) {
			if (attachfile.size() > 0) {
				is_attachfile = true;
			}
		}
		if (is_attachfile) {
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(bodyMessage, this.bodyMessageType + "; charset=" + this.charset);
			multipart.addBodyPart(messageBodyPart);

			for (int i = 0; i < attachfile.size(); i++) {
				addAttachment(multipart, (String) attachfile.get(i));
			}
			mimemessage.setContent(multipart);
		} else {
			mimemessage.setContent(bodyMessage, this.bodyMessageType + "; charset=" + this.charset);
		}

		// Send message
		Transport.send(mimemessage);
		System.out.println("Sent message successfully....");

	}

	private void addAttachment(Multipart multipart, String filename) throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		FileDataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(source.getName());
		multipart.addBodyPart(messageBodyPart);
	}
	
	public static void test_send_emailV2() throws Exception {
		FEmailClient myMail = new FEmailClient("zcs240.smartmailserver.net","587",2,"Admin@abc.com","myPass@99"); //test 16/10/67
		myMail.setTo(new String[] { "prch13@pstg.co.th" });
		myMail.setCc(new String[] {});
		myMail.setSubject("test_send_email()");
		myMail.setBodyMessage("test_send_emailV2 : " + FnDate.getTodayDateTime());
		
		myMail.sendEmail();
	}
	
	public static void test_send_email_attachV2() throws Exception {
		FEmailClient myMail = new FEmailClient("zcs240.smartmailserver.net","587",2,"Admin@abc.com","myPass@99"); //test 16/10/67
		myMail.setTo(new String[] { "prch13@pstg.co.th" });
		myMail.setCc(new String[] {});
		myMail.setSubject("test_send_email()");
		myMail.setBodyMessage("test_send_email_attachV2 : " + FnDate.getTodayDateTime());

		ArrayList<String> list = new ArrayList<String>();
		list.add("C:\\tmp\\myjob.TXT");
		myMail.setAttachfile(list);

		myMail.sendEmail();
	}
	
}
