/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ModuleReformatterContext
{
    IXtextDocument mDoc;
    IProject mProject;
    Module mModule;

    // Стратегия реформата модуля
    // 1) Взять эталонную структуру модуля и распарсить её
    // 2) Распарсить нашу структуру модуля
    // 3) Определить каких областей не хватает до эталона - добавить их и упорядочить
    // 4) Определить какие методы в какую область идут, сохраняя произвольные родительские области
    //    Например метод "ПриветМир() Экспорт" в области "Хуета" попадет "ПрограммныйИнтерфейс -> Хуета -> ПриветМир"
    // 5) Для модулей без директив компиляции - добавить на "на сервере", для модулей не упоминающих реквизиты формы
    //        - "на сервере без контекста"

    public ModuleReformatterContext(IProject project, Module model, IXtextDocument doc)
    {
        mDoc = doc;
        mModule = model;
        mProject = project;
    }

    public void run()
    {
        //RequiredRegionsCalculator regionsCalculator = new RequiredRegionsCalculator(mProject, mModule);
        //List<AddRegionTask> missingRegions = regionsCalculator.calculateMissingRegions();

        Lexer lex = new Lexer(mDoc.get());

        lex.setLazyMode(true);
        ModuleASTTree mModuleStructure = new ModuleASTTree(lex);

        String s = mModuleStructure.serialize(ScriptVariant.RUSSIAN);

        s = cleanupConsecutiveBlankLines(s);

        try
        {
            mDoc.replace(0, mDoc.getLength(), s.toString());
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String cleanupConsecutiveBlankLines(String source)
    {
        String lines[] = source.split("\n"); //$NON-NLS-1$

        StringBuilder builder = new StringBuilder();

        boolean hasEmptyLine = false;

        for (var line : lines)
        {
            if (line.isBlank() && hasEmptyLine)
                continue;
            else if (line.isBlank() && !hasEmptyLine)
            {
                builder.append("\n"); //$NON-NLS-1$
                hasEmptyLine = true;
            }
            else
            {
                hasEmptyLine = false;
                builder.append(line + "\n");
            }
        }

        return builder.toString();
    }

}
