/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.methodmissingpragmas;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.bsl.BSLMethodAnnotation;

class FixVariant
{
    private BSLMethodAnnotation mId;
    private String mPragmaRU;
    private String mPragmaEN;

    /**
     *
     */
    public FixVariant(BSLMethodAnnotation annotationId, String pragmaRU, String pragmaEN)
    {
        mId = annotationId;

        mPragmaRU = pragmaRU;
        mPragmaEN = pragmaEN;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return String.format("Добавить директиву \"%s\"", mPragmaRU); //$NON-NLS-1$
    }

    /**
     * @return the pragmaRU
     */
    public String getPragmaRU()
    {
        return mPragmaRU;
    }
    /**
     * @return the pragmaEN
     */
    public String getPragmaEN()
    {
        return mPragmaEN;
    }

    /**
     * @return the id
     */
    public BSLMethodAnnotation getId()
    {
        return mId;
    }

    static FixVariant[] fixVariants = { new FixVariant(BSLMethodAnnotation.AtServer, "&НаСервере", "&AtClient"), //$NON-NLS-1$ //$NON-NLS-2$
        /*new FixVariant(BSLMethodAnnotation.AtClient, "&НаКлиенте", "&AtServer"), //$NON-NLS-1$ //$NON-NLS-2$
        new FixVariant(BSLMethodAnnotation.AtServerNoContext, "&НаСервереБезКонтекста", "&AtServerNoContext"), //$NON-NLS-1$ //$NON-NLS-2$
        new FixVariant(BSLMethodAnnotation.AtClientAtServerNoContext, "&НаКлиентеНаСервереБезКонтекста", //$NON-NLS-1$
            "&AtClientAtServerNoContext"), //$NON-NLS-1$
        new FixVariant(BSLMethodAnnotation.AtClientAtServer, "&НаКлиентеНаСервере", "&AtClientAtServer"), //$NON-NLS-1$ //$NON-NLS-2$ */
    };

    public static List<FixVariant> buildResolutions(Issue issue, IssueResolutionAcceptor acceptor)
    {
        List<FixVariant> result = new LinkedList<>();

        for (FixVariant variant : fixVariants)
        {
            result.add(variant);
        }

        return result;

    }
}