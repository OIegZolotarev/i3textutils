/**
 *
 */
package org.quiteoldorange.i3textutils.refactoring;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;

/**
 * @author ozolotarev
 *
 */
public class ModuleElement
{
    private String mSourceText;
    private String mName;
    private boolean mExported;

    List<ModuleElement> mChidlren = null;

    private final static String METHOD = "Method"; //$NON-NLS-1$
//    private final static String DECLARATION = "Declaration"; //$NON-NLS-1$
//    private final static String REGION = "Region"; //$NON-NLS-1$
//    private final static String MAINPROGRAM = "MainProgram"; //$NON-NLS-1$
//
    @SuppressWarnings("unused")
    private String mType;

    /**
     * @return the sourceText
     */
    public String getSourceText()
    {
        return mSourceText;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return mName;
    }

    ModuleElement(String name, boolean isExported, String sourceText, String typeHint)
    {
        mSourceText = sourceText;
        mType = typeHint;
        mName = name;
        mExported = isExported;
    }

    /**
     * @return the exported
     */
    public boolean isExported()
    {
        return mExported;
    }

    public static List<ModuleElement> collectFromModule(IXtextDocument doc)
    {
        var module = Utils.getModuleFromXTextDocument(doc);
        List<ModuleElement> result = new LinkedList<>();

        collectMethods(doc, module, result);

        return result;
    }

    /**
     * @param doc
     * @param module
     * @param result
     */
    private static void collectMethods(IXtextDocument doc, Module module, List<ModuleElement> result)
    {
        EList<Method> methods = module.allMethods();

        for (Method m : methods)
        {
            MethodSourceInfo info = null;

            try
            {
                info = Utils.getMethodSourceInfo(m, doc);
            }
            catch (BadLocationException e)
            {
                continue;
            }

            ModuleElement el = new ModuleElement(m.getName(), m.isExport(), info.getSourceText(), METHOD);
            result.add(el);
        }
    }

}
