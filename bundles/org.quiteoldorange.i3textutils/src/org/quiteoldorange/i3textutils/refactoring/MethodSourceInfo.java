/**
 *
 */
package org.quiteoldorange.i3textutils.refactoring;

/**
 * @author ozolotarev
 *
 */
public class MethodSourceInfo
{
    private Integer mStartOffset;
    private Integer mLength;
    private String mSourceText;

    public MethodSourceInfo(Integer startOffset, Integer endOffset, String source)
    {
        mStartOffset = startOffset;
        mLength = endOffset - startOffset;

        mSourceText = source;
    }

    /**
     * @return the startOffset
     */
    public Integer getStartOffset()
    {
        return mStartOffset;
    }
    /**
     * @return the length
     */
    public Integer getLength()
    {
        return mLength;
    }
    /**
     * @return the sourceText
     */
    public String getSourceText()
    {
        return mSourceText;
    }
}
