package ca.athabascau.util.log4j;

import ca.athabascau.util.log4j.xml.ConfigType;
import ca.athabascau.util.log4j.xml.FilterType;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * Only allows triggering an event, if
 * <p/>
 * - it is an ERROR/FATAL. -
 * <p/>
 * Created :  2012-06-22T15:44 MST
 *
 * @author trenta
 */
public class EmailEvaluator implements TriggeringEventEvaluator
{
    private ConfigType config;
    private EventTimeQueue eventTimeQueue;
    private SMTPAppender smtpAppender;


    public EmailEvaluator(final SMTPAppender smtpAppender)
    {
        this.smtpAppender = smtpAppender;
        config = smtpAppender.getConfig();
    }

    /**
     * Is this <code>event</code> the e-mail triggering event?
     * <p/>
     * <p>This method returns <code>true</code>, if the event level has ERROR
     * level or higher. Otherwise it returns <code>false</code>.
     */
    public boolean isTriggeringEvent(final LoggingEvent event)
    {
        if (eventTimeQueue == null)
        {
            eventTimeQueue = new EventTimeQueue(smtpAppender.getFloodFrequency(),
                smtpAppender.getFloodFrequencyMilliseconds());
        }

        final boolean frequencyExceeded = !eventTimeQueue.add();
        final FilterType filter = config.findMatch(event.getRenderedMessage());
        boolean log = true;
        if (filter != null)
        {
            log = filter.isLog().booleanValue();
        }

        return !frequencyExceeded && event.getLevel().isGreaterOrEqual(
            Level.ERROR) && log;
    }


}
