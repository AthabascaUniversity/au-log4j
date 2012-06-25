package ca.athabascau.util.log4j;

import org.apache.log4j.helpers.LogLog;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  2012-06-22T16:57 MST
 *
 * @author trenta
 */
public class EventTimeQueue
{
    /**
     * Frequency per minAgeInMilliseconds
     */
    private int frequency;
    private ArrayList timestampList;
    private long minAgeInMilliseconds;
    private boolean currentlyFlooding;
    private SMTPAppender smtpAppender;

    public EventTimeQueue(final int frequency, final long minAgeInMilliseconds,
        final SMTPAppender smtpAppender)
    {
        timestampList = new ArrayList(frequency);
        this.frequency = frequency;
        this.minAgeInMilliseconds = minAgeInMilliseconds;
        this.smtpAppender = smtpAppender;
    }

    /**
     * Adds another timestamp to the list, indicating the age of the current
     * event.
     *
     * @return true if the frequency per minAgeInMilliseconds has not been
     *         exceeded.
     */
    public synchronized boolean add()
    {
        long oldestItem = -1;
        if (timestampList.size() <= frequency - 1)
        {
            timestampList.add(new Long(System.currentTimeMillis()));
        }
        else
        {
            timestampList.add(new Long(System.currentTimeMillis()));
            oldestItem = ((Long) timestampList.remove(0)).longValue();
        }

//        currentlyFlooding = !;
/*        if (currentlyFlooding)
        {
            sendNotification();
        }*/
        // the oldest item is older than the minimum age, we're not flooding
        return oldestItem == -1 ||
            System.currentTimeMillis() - oldestItem > minAgeInMilliseconds;
    }

    private void sendNotification()
    {
        final Session session = smtpAppender.createSession();
        final MimeMessage msg = new MimeMessage(session);

        try
        {
            smtpAppender.addressMessage(msg);
            if (smtpAppender.getSubject() != null)
            {
                try
                {
                    msg.setSubject(MimeUtility.encodeText(
                        smtpAppender.getSubject(), "UTF-8", null));
                }
                catch (UnsupportedEncodingException ex)
                {
                    LogLog.error("Unable to encode SMTP subject", ex);
                }
            }
        }
        catch (MessagingException e)
        {
            LogLog.error("Could not notify someone of flooding.", e);
        }
    }

}
