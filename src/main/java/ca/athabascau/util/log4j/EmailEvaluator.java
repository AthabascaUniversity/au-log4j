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

import ca.athabascau.util.log4j.xml.ConfigType;
import ca.athabascau.util.log4j.xml.FilterType;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * Only allows triggering an event, if
 * <p/>
 * - it is an ERROR/FATAL.
 * <p/>
 * - the flood frequency has not been reached
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
     * <p>This method returns <code>true</code> if flood protection is enabled
     * but flooding is not in progress and if the matched filter from
     * filter-config.xml is designated to be included in logging. Otherwise it
     * returns <code>false</code>.
     */
    public boolean isTriggeringEvent(final LoggingEvent event)
    {
        if (eventTimeQueue == null)
        {
            eventTimeQueue = new EventTimeQueue(smtpAppender);
        }

        final boolean frequencyExceeded = !eventTimeQueue.add();
        long before;
        long after;

        before = System.currentTimeMillis();
        boolean log = true;
        if (config != null)
        {
            final FilterType filter = config.findMatch(
                event.getRenderedMessage());
            after = System.currentTimeMillis();
//        LogLog.warn("log match took: " + (after - before) + "ms");

            if (filter != null)
            {
                log = filter.isLog().booleanValue();
            }
        }

        return !frequencyExceeded && log;
    }


}
