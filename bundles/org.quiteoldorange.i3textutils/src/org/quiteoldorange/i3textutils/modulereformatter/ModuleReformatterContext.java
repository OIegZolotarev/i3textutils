/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.modulereformatter.tasks.AddRegionTask;

import com._1c.g5.v8.dt.bsl.model.Module;

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
        RequiredRegionsCalculator regionsCalculator = new RequiredRegionsCalculator(mProject, mModule);
        List<AddRegionTask> missingRegions = regionsCalculator.calculateMissingRegions();

        Lexer lex = new Lexer(mDoc.get());

        lex.setLazyMode(true);
        ModuleASTTree mModuleStructure = new ModuleASTTree(lex);

    }
}
