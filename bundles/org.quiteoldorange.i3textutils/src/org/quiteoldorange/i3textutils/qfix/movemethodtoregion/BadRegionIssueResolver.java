/**
 *
 */
package org.quiteoldorange.i3textutils.qfix.movemethodtoregion;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.validation.Issue;
import org.quiteoldorange.i3textutils.StringUtils;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleReformatterContext;
import org.quiteoldorange.i3textutils.refactoring.MethodSourceInfo;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
final class BadRegionIssueResolver
    implements IModification
{
    /**
     *
     */
    private final Issue mIssue;
    private List<String> mIssueSuggestedRegions;
    private List<String> mInvalidRegionsForSuggestion;
    private String mSuggestedRegion = null;

    /**
     * @param issue
     */
    BadRegionIssueResolver(Issue issue, List<String> issueSugestedRegions, List<String> invalidRegionsForSuggestion)
    {
        mIssue = issue;
        mIssueSuggestedRegions = issueSugestedRegions;
        mInvalidRegionsForSuggestion = invalidRegionsForSuggestion;
    }

    BadRegionIssueResolver(Issue issue, String suggestedRegion)
    {
        mIssue = issue;
        mSuggestedRegion = suggestedRegion;
    }

    @Override
    public void apply(IModificationContext context) throws Exception
    {
        var doc = context.getXtextDocument();
        var module = Utils.getModuleFromXTextDocument(doc);

        String methodName = StringUtils.parseMethodFromURIToProblem(mIssue.getUriToProblem().toString());

        CandidateRegion regionDesc = null;

        if (mSuggestedRegion == null)
        {

            List<RegionPreprocessor> existingRegions = BslUtil.getAllRegionPreprocessors(module);
            List<CandidateRegion> candidates = makeCandidatesList(existingRegions, doc);

            // Для длинных сообщений об ошибках выводим обобощенное сообщение
            // т.к. оригинальное некрасиво растягивает окно.
            String message = null;
            if (mIssueSuggestedRegions.size() > 1)
            {
                message = Messages.BadRegionIssueResolver_ChooseRegionIntoWhichToMoveMethod;
            }
            else
            {
                message = mIssue.getMessage();
            }

            var dlg = new RegionChooserDialog(candidates, methodName, message);
            dlg.open();

            regionDesc = dlg.getSelectedRegion();
        }
        else
        {
            RegionPreprocessor bslRegion = Utils.findModuleRegion(mSuggestedRegion, module);

            if (bslRegion == null)
                regionDesc = new CandidateRegion(mSuggestedRegion, false);
            else
                regionDesc = new CandidateRegion(bslRegion, doc);
        }

        if (regionDesc == null)
        {
            return;
        }

        moveMethodToNewRegion(doc, module, methodName, regionDesc);

        // Eclipse после применения метода принудительно перемещает выделение в место где был Issue
        // Поэтому сфокусироваться на новом расположении метода не выйдет =\
/*        var page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
var part = page.getActiveEditor();
BslXtextEditor target = part.getAdapter(BslXtextEditor.class);

IXtextDocument docEdited = target.getDocument();

if (docEdited == doc)
{
    int methodOffset = doc.getLineOffset(10);


    target.selectAndReveal(methodOffset, 32);

    IWorkbenchPage page2 = target.getSite().getPage();
    page2.activate(target);

}*/
    }

    /**
     * @param doc
     * @param module
     * @param methodName
     * @param regionDesc
     * @throws BadLocationException
     */
    private int moveMethodToNewRegion(IXtextDocument doc, Module module, String methodName, CandidateRegion regionDesc)
        throws BadLocationException
    {
        Method method = Utils.findModuleMethod(methodName, module);
        MethodSourceInfo info = Utils.getMethodSourceInfo(method, doc);

        ScriptVariant variant = Utils.getDocScriptVariant(doc);

        int targetOffset = 0;
        int replaceLength = 0;

        if (!regionDesc.isExists())
        {
            StringBuilder builder = new StringBuilder();

            if (variant == ScriptVariant.RUSSIAN)
            {
                builder.append(String.format("#Область %s\n", regionDesc.getName())); //$NON-NLS-1$
                builder.append("\n" + info.getSourceText().trim() + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
                builder.append(String.format("#КонецОбласти // %s\n\n", regionDesc.getName())); //$NON-NLS-1$
            }
            else
            {
                builder.append(String.format("#Region %s\n", regionDesc.getName())); //$NON-NLS-1$
                builder.append("\n" + info.getSourceText().trim() + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
                builder.append(String.format("#EndRegion // %s\n\n", regionDesc.getName())); //$NON-NLS-1$
            }

            doc.replace(info.getStartOffset(), info.getLength(), ""); //$NON-NLS-1$
            doc.replace(0, 0, builder.toString());
            return 0;
        }
        else
        {
            targetOffset = regionDesc.getOffset();
            replaceLength = 0;
        }

        // Если исходное тело модуля до области - надо сначала убрать тело модуля
        // затем поместить его в область, и соответственно наоборот, иначе смещения будут неактуальные
        // и все поплывет :(
        if (info.getStartOffset() > targetOffset)
        {
            doc.replace(info.getStartOffset(), info.getLength(), ""); //$NON-NLS-1$
            moveMethodBodyToTarget(doc, info, targetOffset, replaceLength);
        }
        else
        {
            moveMethodBodyToTarget(doc, info, targetOffset, replaceLength);
            doc.replace(info.getStartOffset(), info.getLength(), ""); //$NON-NLS-1$
        }

        // Причесываем пустые линии
        String newSource = ModuleReformatterContext.cleanupConsecutiveBlankLines(doc.get());
        doc.replace(0, doc.getLength(), newSource);

        return targetOffset;
    }

    private String getDocLine(IXtextDocument doc, int line)
    {
        try
        {
            var lineInfo = doc.getLineInformation(line);
            return doc.get(lineInfo.getOffset(), lineInfo.getLength());
        }
        catch (BadLocationException exception)
        {
            return " "; //$NON-NLS-1$
        }
    }

    /**
     * @param doc
     * @param info
     * @param targetOffset
     * @param replaceLength
     * @throws BadLocationException
     */
    private void moveMethodBodyToTarget(IXtextDocument doc, MethodSourceInfo info, int targetOffset, int replaceLength)
        throws BadLocationException
    {
        String targetText = "\n" + info.getSourceText().trim() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

        if (replaceLength > 0)
        {
            doc.replace(targetOffset, replaceLength, targetText);
        }
        else
        {
            int line = doc.getLineOfOffset(targetOffset);

            String postLine = getDocLine(doc, line + 1);

            if (!postLine.isEmpty())
            {
                targetText = targetText + "\n"; //$NON-NLS-1$
            }

            doc.replace(targetOffset, replaceLength, targetText);
        }
    }

    private boolean isInvalidRegionForSuggestion(String region)
    {
        if (mInvalidRegionsForSuggestion == null)
        {
            return false;
        }

        if (mInvalidRegionsForSuggestion.stream().anyMatch(s -> s.equals(region)))
        {
            return true;
        }

        return false;
    }

    private List<CandidateRegion> makeCandidatesList(List<RegionPreprocessor> items, IXtextDocument doc)
    {

        List<CandidateRegion> result = new LinkedList<>();

        for (var existingItem : items)
        {
            if (isInvalidRegionForSuggestion(existingItem.getName()))
            {
                continue;
            }

            result.add(new CandidateRegion(existingItem, doc));
        }

        for (var newItem : mIssueSuggestedRegions)
        {
            if (isInvalidRegionForSuggestion(newItem) || hasRegionInList(items, newItem))
            {
                continue;
            }

            result.add(new CandidateRegion(newItem, false));
        }


        return result;
    }

    private boolean hasRegionInList(List<RegionPreprocessor> items, String name)
    {
        for (var item : items)
        {
            if (item.getName().equals(name))
            {
                return true;
            }
        }

        return false;
    }

}
