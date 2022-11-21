package com.mockmock.mail;

import com.google.common.eventbus.Subscribe;
import com.mockmock.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

@Service
public class MailQueue
{
    private static final ArrayList<MockMail> MAIL_QUEUE = new ArrayList<>();

    private Settings settings;

    /**
     * Add a MockMail to the queue. Queue is sorted and trimmed right after it.
     * @param mail The MockMail object to add to the queue
     */
    @Subscribe
    public void add(MockMail mail)
    {
        MAIL_QUEUE.add(mail);
        Collections.sort(MAIL_QUEUE);
        Collections.reverse(MAIL_QUEUE);

        trimQueue();
    }

    /**
     * @return Returns the complete mailQueue
     */
    public ArrayList<MockMail> getMailQueue()
    {
        return MAIL_QUEUE;
    }

    /**
     * Returns the MockMail that belongs to the given ID
     * @param id The id of the mail that needs to be retrieved
     * @return Returns the MockMail when found or null otherwise
     */
    public MockMail getById(long id)
    {
        for(MockMail mockMail : MAIL_QUEUE)
        {
            if(mockMail.getId() == id)
            {
                return mockMail;
            }
        }

        return null;
    }

    /**
     * Returns the MockMail that was last send.
     *
     * @return  Returns the MockMail when found or null otherwise
     */
    public MockMail getLastSendMail()
    {
        if (MAIL_QUEUE.size() == 0)
            return null;

        return MAIL_QUEUE.get(0);
    }

    /**
     * Removes all mail in the queue
     */
    public void emptyQueue()
    {
        MAIL_QUEUE.clear();
        MAIL_QUEUE.trimToSize();
    }

    /**
     * Removes the mail with the given id from the queue
     * @param id long
     * @return boolean
     */
    public boolean deleteById(long id)
    {
        for(MockMail mockMail : MAIL_QUEUE)
        {
            if(mockMail.getId() == id)
            {
                MAIL_QUEUE.remove(mockMail);
                return true;
            }
        }

        return false;
    }

    /**
     * Trims the mail queue so there aren't too many mails in it.
     */
    private void trimQueue()
    {
        if(MAIL_QUEUE.size() > settings.getMaxMailQueueSize())
        {
            for (ListIterator<MockMail> iter = MAIL_QUEUE.listIterator(MAIL_QUEUE.size()); iter.hasPrevious();)
            {
                iter.previous();

                if(MAIL_QUEUE.size() <= settings.getMaxMailQueueSize())
                {
                    break;
                }
                else
                {
                    iter.remove();
                }
            }
        }

        MAIL_QUEUE.trimToSize();
    }

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
