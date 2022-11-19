package com.mockmock.controllers;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mockmock.mail.MailQueue;
import com.mockmock.mail.MockMail;
import com.mockmock.controllers.model.MockMailDto;
import com.mockmock.controllers.model.SearchParameters;

@RestController
@Service
public class InboxController {

    private MailQueue mailQueue;

    @GetMapping("/inbox")
    public ArrayList<MockMailDto> inbox() {
        ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        return MockMailDto.mapFrom(inbox);
    }

    @GetMapping("/inbox/{id}")
    public MockMailDto inbox(@PathVariable long id) {
        ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        MockMail foundMail = inbox.stream().filter(mail -> mail.getId() == id).findAny().orElse(null);

        return MockMailDto.mapFrom(foundMail);
    }

    @PutMapping("/inbox/find")
    public ArrayList<MockMailDto> find(@RequestBody SearchParameters searchParameters) {
        ArrayList<MockMail> inbox = mailQueue.getMailQueue();

        Stream<MockMail> stream = inbox.stream();

        Long id = searchParameters.getId();
        String from = searchParameters.getFrom();
        String to = searchParameters.getTo();
        String subject = searchParameters.getSubject();
        String content = searchParameters.getContent();
        Date dateFrom = searchParameters.getDateFrom();

        if (id != null) {
            stream = stream.filter(mail -> mail.getId() == id);
        }

        if (from != null) {
            stream = stream.filter(mail -> mail.getFrom().contains(from));
        }

        if (to != null) {
            stream = stream.filter(mail -> mail.getTo().contains(to));
        }

        if (subject != null) {
            Pattern p = Pattern.compile(subject, Pattern.CASE_INSENSITIVE);
            stream = stream.filter(mail -> p.matcher(mail.getSubject()).find());
        }

        if (content != null) {
            Pattern p = Pattern.compile(content, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            stream = stream.filter(mail -> {
                String body = mail.getBody();
                String bodyHtml = mail.getBodyHtml();

                if (body != null) {
                    return p.matcher(body).find();
                } else if (bodyHtml != null) {
                    return p.matcher(bodyHtml).find();
                } else {
                    return false;
                }
            });
        }

        if (dateFrom != null) {
            stream = stream.filter(mail -> {
                long timeStampFrom = new DateTime(dateFrom).getMillis();
                return mail.getReceivedTime() > timeStampFrom;
            });
        }

        ArrayList<MockMail> foundMails = (ArrayList<MockMail>) stream.collect(Collectors.toList());

        return MockMailDto.mapFrom(foundMails);
    }

    @Autowired
    public void setMailQueue(MailQueue mailQueue) {
        this.mailQueue = mailQueue;
    }
}