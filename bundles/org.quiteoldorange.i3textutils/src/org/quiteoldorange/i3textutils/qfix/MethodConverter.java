/**
 *
 */
package org.quiteoldorange.i3textutils.qfix;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode;
import org.quiteoldorange.i3textutils.dialogs.GracefullErrorDialog;

import com._1c.g5.v8.dt.bsl.model.Method;
import com.google.inject.Inject;

/**
 * @author ozolotarev
 *
 */
public final class MethodConverter
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

    @Inject
    private EObjectAtOffsetHelper offsetHelper;

    public Method getMethodByOffset(XtextResource resource, int offset)
    {
        EObject semanticObject = offsetHelper.resolveElementAt(resource, offset);
        return EcoreUtil2.getContainerOfType(semanticObject, Method.class);
    }

    /**
     * @param issue
     */
    public MethodConverter(Issue issue, ConversionDirection direction)
    {
        mIssue = issue;
        mDirection = direction;
    }

    @Override
    public void apply(IModificationContext context) throws Exception
    {
        var doc = context.getXtextDocument();

        Lexer l = new Lexer(doc.get());
        l.setLazyMode(true);
        ModuleASTTree tree = new ModuleASTTree(l);

        MethodNode node = tree.findMethodDefinition(mIssue.getOffset());

        if (node == null)
        {
            GracefullErrorDialog.constructAndShow("Не удалось найти описание метода",
                "Возможно ошибка в парсере BSL или функция еще не доделана");
            return;
        }

        String newBody = node.getLazySource();


        if (mDirection == ConversionDirection.ToFunction)
        {
            newBody = newBody.replaceAll("(?i)Процедура ", "Функция "); //$NON-NLS-1$ //$NON-NLS-2$
            newBody =
                newBody.replaceAll("(?i)КонецПроцедуры.*", String.format("КонецФункции // %s", node.getMethodName())); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
            newBody = newBody.replaceAll("(?i)Функция ", "Процедура "); //$NON-NLS-1$ //$NON-NLS-2$
            newBody =
                newBody.replaceAll("(?i)КонецФункции.*", String.format("КонецПроцедуры // %s", node.getMethodName())); //$NON-NLS-1$ //$NON-NLS-2$
        }

        doc.replace(node.getStartingOffset(), node.getLength(), newBody);

    }
}