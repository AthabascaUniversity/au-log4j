/**
 * This file is part of the au-log4j package; aka Athabasca University log4j
 * addons.
 * 
 * Copyright Trenton D. Adams <trenton daught d daught adams at gmail daught ca>
 * 
 * au-log4j is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * au-log4j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with au-log4j.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * See the COPYING file for more information.
 */
package ca.athabascau.util.log4j;

import org.apache.log4j.helpers.LogLog;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implements a time queue that maintains a list of event times.  If the logging
 * events are coming too quickly, the add method returns false, and email
 * logging should be terminated.  If "frequency" logging events are trigger
 * before "minAgeInMilliseconds", logging should be terminated. The underlying
 * details do not need to be explained, you can read the source. You may assume
 * it works, and if it has a bug, YOU (there's no one behind you, I'm talking to
 * you!) can fix it. :D
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
    private String floodEnabledMessage;

    public EventTimeQueue(final SMTPAppender smtpAppender)
    {
        timestampList = new ArrayList(frequency);
        this.frequency = smtpAppender.getFloodFrequency();
        this.minAgeInMilliseconds =
            smtpAppender.getFloodFrequencyMilliseconds();
        this.floodEnabledMessage = smtpAppender.getFloodEnabledMessage();
        this.smtpAppender = smtpAppender;
    }

    /**
     * This constructor is to be used for TESTING ONLY.  It creates an
     * SMTPAppender to utilize some of it's methods, such as createSession().
     * However, because it's created here, it is not associated with log4j in
     * any way.
     *
     * @param frequency            the flood frequency
     * @param minAgeInMilliseconds the flood frequency in milliseconds
     * @param floodEnabledMessage  the last message to send by email when the
     *                             frequncy/minAgeInMilliseconds has been
     *                             reached.
     */
    public EventTimeQueue(final int frequency, final long minAgeInMilliseconds,
        final String floodEnabledMessage)
    {
        timestampList = new ArrayList(frequency);
        this.frequency = frequency;
        this.minAgeInMilliseconds = minAgeInMilliseconds;
        this.floodEnabledMessage = floodEnabledMessage;
        smtpAppender = new SMTPAppender();
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
        if (frequency == 0 || minAgeInMilliseconds == 0)
        {   // flood protection not enabled
            return true;
        }

        final boolean alreadyFlooding = currentlyFlooding;
        long oldestTime = -1;
        final Long newestTime = new Long(System.currentTimeMillis());
        if (timestampList.size() <= frequency - 1)
        {
            timestampList.add(newestTime);
        }
        else
        {
            timestampList.add(newestTime);
            oldestTime = ((Long) timestampList.remove(0)).longValue();
        }

        // if the oldest time hasn't aged enough, then we're flooding.
        // Essentially, we negate the following to determine we're flooding.
        // 1. oldestTime does not exist, because we haven't reached frequency
        // 2. newestTime - oldestTime is older than min age
        currentlyFlooding = !(oldestTime == -1 ||
            newestTime.longValue() - oldestTime > minAgeInMilliseconds);
        if (!alreadyFlooding && currentlyFlooding)
        {
            sendNotification();
        }

        // the oldest times is older than the minimum age, we're not flooding
        return !currentlyFlooding;
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
                        smtpAppender.getSubject() + " flood protection enabled",
                        "UTF-8", null));
                    msg.setContent(smtpAppender.getFloodEnabledMessage(),
                        "text/plain");
                    msg.setSentDate(new Date());
                    Transport.send(msg);
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
