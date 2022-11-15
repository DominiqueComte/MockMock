package com.mockmock.mail;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

public class MockMail implements Comparable<MockMail>
{
    private long id;
    private String from;
    private List<String> to = new ArrayList<String>();
    private String subject;
    private String body;
    private String bodyHtml;
    private String rawMail;
    private MimeMessage mimeMessage;
    private long receivedTime;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getFrom()
    {
        return from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public String getTo()
    {
        return to.toString();
    }

    public void setTo(String to)
    {
        this.to.add(to);
    }
    
    public String getSubject() 
    {
        return subject;
    }
    
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getRawMail()
    {
        return rawMail;
    }

    public void setRawMail(String rawMail)
    {
        this.rawMail = rawMail;
    }

    public String getBodyHtml() 
    {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) 
    {
        this.bodyHtml = bodyHtml;
    }

    public MimeMessage getMimeMessage()
    {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage)
    {
        this.mimeMessage = mimeMessage;
    }

    @Override
    public int compareTo(MockMail o)
    {
        long receivedTime1 = this.getReceivedTime();
        long receivedTime2 = o.getReceivedTime();

        long diff = receivedTime1 - receivedTime2;
        return (int) diff;
    }

    public long getReceivedTime()
    {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime)
    {
        this.receivedTime = receivedTime;
    }
}
