package com.mockmock.http;

import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;
import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Service
public class MailForwardHandler extends BaseHandler
{
	private MailQueue mailQueue;

	@Override
	public void handle(String target, Request request, HttpServletRequest httpServletRequest,
					   HttpServletResponse response) throws IOException, ServletException
	{
		String[] delen = target.split("/");
		if (delen.length != 8){
			return;
		}

		long mailId = Long.valueOf(delen[2]);

		String forwardServer = delen[3];
		String forwardServerDomain = delen[4];
		String forwardServerUserName = delen[5];
		String forwardServerPassword = delen[6];
		String forwardMailAdres = delen[7];

		if(mailId == 0)
		{
			return;
		}

		if(forwardMailAdres == "" || forwardServer == "" || forwardMailAdres == "" || forwardMailAdres == "")
		{
			return;
		}

		MockMail mockMail = this.mailQueue.getById(mailId);
		if(mockMail == null)
		{
			return;
		}

        // forward mail
		//this.SendMail(mockMail, forwardMailAdres);
		forwardEmail(mockMail, forwardServer, forwardServerDomain, forwardServerUserName, forwardServerPassword, forwardMailAdres);
	
		response.setHeader("Location:", "/view/" + String.valueOf(mailId));
		response.setStatus(302);

		request.setHandled(true);
	}

	private static void forwardEmail(MockMail mockMail, String forwardServer, String forwardServerDomain, String forwardServerUserName, String forwardServerPassword, String forwardMailAdres){
        try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", forwardServer);
			//props.put("mail.debug", "true");
	
			props.put("mail.smtp.auth.mechanisms", "NTLM");
			props.put("mail.smtp.auth.ntlm.domain", forwardServerDomain);
	
			Session session;
			if (forwardServerUserName != "" && forwardServerPassword != ""){
				session = Session.getInstance(props, new javax.mail.Authenticator() {
					@Override protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(forwardServerUserName, forwardServerPassword);
					}
				});
			}
			else {
				session = Session.getInstance(props);
			}
	
			// compose the message to forward  
			MimeMessage message = new MimeMessage(session);  
			message.setHeader("Content-Type", "text/html");
			message.setSubject("Fwd: " + mockMail.getSubject());  
			message.setFrom(new InternetAddress(mockMail.getFrom()));  
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(forwardMailAdres));

			// Create your new message part  
			BodyPart messageBodyPart = new MimeBodyPart();  
			messageBodyPart.setContent(mockMail.getBodyHtml(), "text/html; charset=utf-8");

			// Create a multi-part to combine the parts  
			Multipart multipart = new MimeMultipart();  

			// Create and fill part for the forwarded content  
			messageBodyPart = new MimeBodyPart();  
			messageBodyPart.setDataHandler(mockMail.getMimeMessage().getDataHandler());  

			// Add part to multi part  
			multipart.addBodyPart(messageBodyPart);  

			// Associate multi-part with message  
			message.setContent(multipart);  

            Transport.send(message);

            System.out.println("Message forwarded to " + forwardMailAdres + " successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
	}

	@Autowired
	public void setMailQueue(MailQueue mailQueue) {
		this.mailQueue = mailQueue;
	}
}
