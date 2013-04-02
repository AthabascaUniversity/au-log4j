package ca.athabascau.util.log4j;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the EventTimeQueue, to ensure that it is returning false when the
 * frequency has been exceeded.  Also test to ensure the performance is
 * adequate, otherwise it could slow down log4j.
 * <p/>
 * Created :  2012-06-22T16:54 MST
 *
 * @author trenta
 */
public class EventTimeQueueTest extends TestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
        SMTPAppender.setFloodProtectionDisabled(false);

    }

    /**
     * Tests that the flood protection queue is functioning correctly based on
     * the number of messages, and the time specified.
     *
     * @throws InterruptedException
     */
    public void testFlood() throws InterruptedException
    {
        // Allow a maximum of 5 events per second
        final EventTimeQueue eventTimeQueue = new EventTimeQueue(5, 1000,
            "Flood protection activated");
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

    /**
     * Verifies that the performance of the event queue is within acceptable
     * parameters; namely 100ms.  Note, when running from within an IDE, this
     * test may fail.
     */
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
