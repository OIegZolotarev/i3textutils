/**
 *
 */
package org.quiteoldorange.i3textutils.refactoring;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;

/**
 * @author ozolotarev
 *
 */
public class ModuleReformatterContext
{
    IXtextDocument mDoc;


    public ModuleReformatterContext(IXtextDocument doc)
    {
        mDoc = doc;
    }
}
