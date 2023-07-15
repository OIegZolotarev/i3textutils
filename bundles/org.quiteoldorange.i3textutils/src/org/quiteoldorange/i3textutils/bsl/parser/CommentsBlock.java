/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import java.util.List;

/**
 * @author ozolotarev
 *
 */
public class CommentsBlock
    extends AbsractBSLElementNode
{

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
