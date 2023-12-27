/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.LinkedList;
import java.util.List;

public class SuggestedRegions
{
    List<String> mRecommendedRegions = null;
    List<String> mBadRegions = null;

    public SuggestedRegions()
    {
        mRecommendedRegions = new LinkedList<>();
        mBadRegions = new LinkedList<>();
    }

    void addRecommededRegion(String region)
    {
        mRecommendedRegions.add(region);
    }

    void addBadRegion(String region)
    {
        mBadRegions.add(region);
    }

    /**
     * @return the badRegions
     */
    public List<String> getBadRegions()
    {
        return mBadRegions;
    }

    /**
     * @return the recommendedRegions
     */
    public List<String> getRecommendedRegions()
    {
        return mRecommendedRegions;
    }
}