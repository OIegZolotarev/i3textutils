/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;

/**
 * @author ozolotarev
 *
 */
public class CandidateRegion
{
    private String mName;
    private boolean mExists;
    private int mOffset;


    public CandidateRegion(String name, boolean isExists)
    {
        mName = name;
        mExists = isExists;
    }

    public CandidateRegion(RegionPreprocessor region, IXtextDocument doc)
    {
        mName = region.getName();
        mExists = true;

        var node = NodeModelUtils.findActualNodeFor(region);
        mOffset = node.getOffset();

        int line;
        try
        {
            line = doc.getLineOfOffset(mOffset);
            mOffset = doc.getLineOffset(line + 1);
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getName()
    {
        return mName;
    }

    public boolean isExists()
    {
        return mExists;
    }

    public int getOffset()
    {
        return mOffset;
    }
}
