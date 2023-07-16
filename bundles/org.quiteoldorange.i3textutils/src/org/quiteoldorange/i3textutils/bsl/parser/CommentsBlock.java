/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.List;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class CommentsBlock
    extends AbsractBSLElementNode
{

    @Override
    public String serialize(ScriptVariant scriptVariant)
    {
        return serializeChildren(scriptVariant, true);
    }

    /**
     * @param stream
     */
    public CommentsBlock(List<CommentNode> comments)
    {
        super(null);

        for (var item : comments)
        {
            addChildren(item);
            //item.setParent(this);
        }

    }

}
