package com.mockmock.server;

import com.google.common.eventbus.EventBus;
import com.mockmock.EventBusFactory;
import com.mockmock.Settings;
import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;
import com.mockmock.mail.MockMockMessageHandlerFactory;
import org.junit.Before;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.*;

public class SmtpServerTest {
    private Session session;
    private MailQueue queue;
    private Settings settings;

    @Before
    public void setUp() throws Exception {
        int smtpPort = getFreePort();
        settings = new Settings();
        settings.setSmtpPort(smtpPort);
        SmtpServer smtpServer = new SmtpServer();
        queue = new MailQueue();
        queue.setSettings(settings);
        EventBusFactory eventBusFactory = new EventBusFactory();
        eventBusFactory.setMailQueue(queue);
        EventBus eventBus = eventBusFactory.createEventBus();
        MockMockMessageHandlerFactory handlerFactory = new MockMockMessageHandlerFactory(eventBus);
        handlerFactory.setSettings(settings);
        smtpServer.setHandlerFactory(handlerFactory);
        smtpServer.setPort(settings.getSmtpPort());
        smtpServer.start();

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "localhost");
        props.setProperty("mail.smtp.port", Integer.toString(settings.getSmtpPort()));
        this.session = Session.getInstance(props);
    }

    @Test
    public void tryToSendOneEmail() throws MessagingException, IOException {
        /* given an email message */
        MimeMessage message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress("someone@somewhereelse.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("anyone@anywhere.com"));
        message.setSubject("a test");
        message.setText("hello\nbody\n\nsignature");
        /* when it is sent */
        Transport.send(message);
        /* then the mail queue contains only this message */
        ArrayList<MockMail> mailQueue = queue.getMailQueue();
        assertEquals(1, mailQueue.size());
        MockMail firstMail = mailQueue.get(0);
        assertEquals(message.getFrom()[0].toString(), firstMail.getFrom());
        assertEquals("["+message.getAllRecipients()[0].toString()+"]", firstMail.getTo());
        assertEquals(message.getSubject(), firstMail.getSubject());
        assertEquals(message.getContent() + "\n", firstMail.getBody());
    }

    @Test
    public void sendAnEmailToMultipleRecipients() throws MessagingException {
        /* given an email message */
        MimeMessage message = new MimeMessage(this.session);
        message.setFrom(new InternetAddress("someone@somewhereelse.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("anyone@anywhere.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("nobody@anywhere.com"));
        message.setSubject("subject");
        message.setText("body");
        /* when it is sent */
        Transport.send(message);
        /* then the mail queue contains only this message */
        ArrayList<MockMail> mailQueue = queue.getMailQueue();
        assertEquals(1, mailQueue.size());
        MockMail firstMail = mailQueue.get(0);
        assertEquals(message.getFrom()[0].toString(), firstMail.getFrom());
        MimeMessage firstMimeMessage = firstMail.getMimeMessage();
        assertEquals(2, firstMimeMessage.getRecipients(Message.RecipientType.TO).length);
        assertTrue(firstMail.getTo().contains("anyone@anywhere.com"));
        assertTrue(firstMail.getTo().contains("nobody@anywhere.com"));
    }

    /**
     * @return a free port number
     */
    private int getFreePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            assertNotNull(serverSocket);
            assertTrue(serverSocket.getLocalPort() > 0);
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            fail("Port is not available");
        }
        return -1;
    }
}
