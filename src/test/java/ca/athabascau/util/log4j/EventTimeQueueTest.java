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

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the EventTimeQueue, to ensure that it is returning false when the
 * frequency has been exceeded.
 * <p/>
 * Created :  2012-06-22T16:54 MST
 *
 * @author trenta
 */
public class EventTimeQueueTest extends TestCase
{
    public void testFlood() throws InterruptedException
    {
        // Allow a maximum of 5 events per second
        final EventTimeQueue eventTimeQueue = new EventTimeQueue(5, 1000,
            "Flood protection enabled");
        for (int index = 0; index < 10; index++)
        {
            final boolean exceeded = !eventTimeQueue.add();
//            System.out.println(index + " exceeded: " + exceeded);
            Assert.assertTrue(
                "The frequency should only be exceeded on index 5. index: " +
                    index, exceeded ? index == 5 : index < 5 || index > 5);
            if (index == 5)
            {
                Thread.sleep(1100);
            }
        }
    }

    public void testPerf()
    {

        final EventTimeQueue eventTimeQueue = new EventTimeQueue(5, 1000,
            "Flood protection enabled");
        final long before;
        final long after;

        before = System.currentTimeMillis();
        for (int index = 0; index < 1000; index++)
        {
            eventTimeQueue.add();
        }
        after = System.currentTimeMillis();
        System.out.println(
            "event add loop of 1000 took: " + (after - before) + "ms");
        assertTrue("performance fails expected <100ms: " + (after - before),
            after - before < 100);
    }
}
