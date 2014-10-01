package ca.athabascau.util.log4j;

import ca.athabascau.util.log4j.xml.ConfigType;
import ca.athabascau.util.log4j.xml.FilterType;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * Only allows triggering an event, if
 * <p/>
 * - the flood frequency has not been reached
 * <p/>
 * Created :  2012-06-22T15:44 MST
 *
 * @author trenta
 */
public class EmailEvaluator implements TriggeringEventEvaluator
{
    private EventTimeQueue eventTimeQueue;
    private SMTPAppender smtpAppender;


    public EmailEvaluator(final SMTPAppender smtpAppender)
    {
        this.smtpAppender = smtpAppender;
    }

    /**
     * Is this <code>event</code> the e-mail triggering event?
     * <p/>
     * <p>This method returns <code>true</code> if flood protection is enabled
     * but flooding is not in progress and if the matched filter from
     * filter-config.xml is designated to be included in logging. Otherwise it
     * returns <code>false</code>.
     */
    public boolean isTriggeringEvent(final LoggingEvent event)
    {
        final ConfigType config = smtpAppender.getConfig();
        if (eventTimeQueue == null)
        {
            eventTimeQueue = new EventTimeQueue(smtpAppender);
        }

        final boolean frequencyExceeded = !eventTimeQueue.add();

        boolean log = true;
        if (config != null)
        {
            final FilterType filter = config.findMatch(
                event.getRenderedMessage());

            if (filter != null)
            {
                log = filter.isLog().booleanValue();
            }
        }

        return !frequencyExceeded && log;
    }


}
