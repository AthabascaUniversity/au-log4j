package ca.athabascau.util.log4j;

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

    public EventTimeQueue(final int frequency, final long minAgeInMilliseconds)
    {
        timestampList = new ArrayList(frequency);
        this.frequency = frequency;
        this.minAgeInMilliseconds = minAgeInMilliseconds;
    }

    /**
     * Adds another timestamp to the list, indicating the age of the current
     * event.
     *
     * @return true if the frequency per minAgeInMilliseconds has not been
     *         exceeded.
     */
    public boolean add()
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

        return oldestItem == -1 ||
            System.currentTimeMillis() - oldestItem > minAgeInMilliseconds;
    }

}
