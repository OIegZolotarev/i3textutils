/**
 * Copyright (C) 2020, 1C-Soft LLC
 */
package org.quiteoldrange.i3textutils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

import com._1c.g5.v8.dt.bsl.model.Module;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

/**
 * Команда создания обработчика проведения в объектном модуле документа, с
 * которым связан регистр накопления. Данная команда регистрируется при помощи
 * точки разширения с именем "org.eclipse.ui.commands", которая присваивает
 * данной команде уникальный идентификатор, по которому можно производит вызов
 * данной команды из различных мест, например, из контекстного меню текстового
 * редактора.
 * <p>
 * Данная команда предназначена для работы только с текстовым редактором Xtext
 * {@link XtextEditor} и содержит в себе логику для проверки корректности
 * добавления обработчика проведения в данный модуль, а так же для определения
 * позиции вставки обработчика
 */
public class DataProcessingHandler
    extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IXtextDocument doc = ModuleRefactoringUtils.getXTextDocumentFromEvent(event);

        if (doc == null)
        {
            return null;
        }

        Module moduleModel = ModuleRefactoringUtils.getModuleModel(doc);

        if (moduleModel == null)
        {
            return null;
        }

        var methods = moduleModel.allMethods();
        for (int i = methods.size() - 1; i >= 0; i--)
        {
            var method = methods.get(i);

            if (method.getPragmas().isEmpty())
            {
                var node = NodeModelUtils.findActualNodeFor(method);



                try
                {
                    doc.replace(node.getOffset(), 0, "&НаСервере\n");
                }
                catch (BadLocationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return null;

    }



    private static String readContents(CharSource source, String path)
    {
        try (Reader reader = source.openBufferedStream())
        {
            return CharStreams.toString(reader);
        }
        catch (IOException | NullPointerException e)
        {
            return ""; //$NON-NLS-1$
        }
    }

    private static CharSource getFileInputSupplier(String partName)
    {
        return Resources.asCharSource(DataProcessingHandler.class.getResource("/resources/" + partName), //$NON-NLS-1$
            StandardCharsets.UTF_8);
    }
}
