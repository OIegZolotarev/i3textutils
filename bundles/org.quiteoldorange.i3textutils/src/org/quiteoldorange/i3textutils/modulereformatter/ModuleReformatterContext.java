/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.bsl.parser.BSLRegionNode;
import org.quiteoldorange.i3textutils.refactoring.Utils;

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

        reorderTopRegions(mModuleStructure);

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
                builder.append(line);
            }
        }

        return builder.toString();
    }

    /**
     * @param mModuleStructure
     */
    private void reorderTopRegions(ModuleASTTree mModuleStructure)
    {
        ModuleASTTree idealTree = getStandardModuleStructure();

        int order = 0;

        List<BSLRegionNode> nodesOrderedList = new LinkedList<>();

        for (var item : idealTree.getChildren())
        {
            if (!(item instanceof BSLRegionNode))
                continue;

            BSLRegionNode idealRegion = (BSLRegionNode)item;
            BSLRegionNode moduleNode = mModuleStructure.findRegion(idealRegion.getName());

            if (moduleNode != null)
            {
                moduleNode.setIdealOrder(order);
                nodesOrderedList.add(moduleNode);
            }

            order++;


        }

        var mModuleItems = mModuleStructure.getChildren();
        var iterator = mModuleItems.iterator();

        for (var idealNode : nodesOrderedList)
        {
            var item = iterator.next();

            while (!(item instanceof BSLRegionNode))
                item = iterator.next();

            int itemIndex = mModuleItems.indexOf(item);
            int oldIndex = mModuleItems.indexOf(idealNode);

            BSLRegionNode oldNode = (BSLRegionNode)mModuleItems.set(itemIndex, idealNode);
            mModuleItems.set(oldIndex, oldNode);

            // Log.Debug("Placing at %s %s -> %s", String.format("%d", itemIndex), oldNode.getName(), idealNode.getName());
        }

        mModuleStructure.setChildren(mModuleItems);
    }


    public ModuleASTTree getStandardModuleStructure()
    {
        IFile templatePath = mProject.getFile(Utils.getFileTemplatePathForModuleType(mModule.getModuleType()));
        File f = templatePath.getLocation().toFile();

        try
        {
            String templateSource = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));

            Lexer lex = new Lexer(templateSource);
            ModuleASTTree tree = new ModuleASTTree(lex);

            return tree;
        }
        catch (IOException e)
        {
            return null;
        }

    }
}
