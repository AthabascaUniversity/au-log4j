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
        final EventTimeQueue eventTimeQueue = new EventTimeQueue(5, 1000);
        for (int index = 0; index < 10; index++)
        {
            boolean exceeded = !eventTimeQueue.add();
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
}
