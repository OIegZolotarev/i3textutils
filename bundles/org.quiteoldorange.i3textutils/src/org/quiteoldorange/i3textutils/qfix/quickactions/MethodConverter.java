/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.quickactions;

import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.StringUtils;
import org.quiteoldorange.i3textutils.refactoring.MethodSourceInfo;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Method;

/**
 * @author ozolotarev
 *
 */
final class MethodConverter
    implements IModification
{
    public enum ConversionDirection
    {
        ToProcedure,
        ToFunction
    };

    /**
     *
     */
    private final Issue mIssue;
    private ConversionDirection mDirection;

    /**
     * @param issue
     */
    MethodConverter(Issue issue, ConversionDirection direction)
    {
        mIssue = issue;
        mDirection = direction;
    }

    @Override
    public void apply(IModificationContext context) throws Exception
    {
        var doc = context.getXtextDocument();
        var module = Utils.getModuleFromXTextDocument(doc);
        String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());

        Method method = Utils.findModuleMethod(methodName, module);
        MethodSourceInfo info = Utils.getMethodSourceInfo(method, doc);


        String newBody = info.getSourceText();

        if (mDirection == ConversionDirection.ToFunction)
        {
            newBody = newBody.replaceAll("(?i)Процедура ", "Функция "); //$NON-NLS-1$ //$NON-NLS-2$
            newBody = newBody.replaceAll("(?i)КонецПроцедуры.*", String.format("КонецФункции // %s", methodName)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
            newBody = newBody.replaceAll("(?i)Функция ", "Процедура "); //$NON-NLS-1$ //$NON-NLS-2$
            newBody = newBody.replaceAll("(?i)КонецФункции.*", String.format("КонецПроцедуры // %s", methodName)); //$NON-NLS-1$ //$NON-NLS-2$
        }

        doc.replace(info.getStartOffset(), info.getLength(), newBody);

    }
}