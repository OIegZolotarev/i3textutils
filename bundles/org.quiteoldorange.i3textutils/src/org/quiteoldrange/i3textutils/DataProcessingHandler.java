/**
 * Copyright (C) 2020, 1C-Soft LLC
 */
package org.quiteoldrange.i3textutils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com._1c.g5.v8.dt.bsl.model.DeclareStatement;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.ModuleType;
import com._1c.g5.v8.dt.bsl.model.RegionPreprocessor;
import com._1c.g5.v8.dt.bsl.model.util.BslUtil;
import com._1c.g5.v8.dt.metadata.mdclass.AccumulationRegister;
import com._1c.g5.v8.dt.metadata.mdclass.BasicRegister;
import com.google.common.collect.Lists;
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
    private static final String TEMPLATE_NAME = "template.txt"; //$NON-NLS-1$
    private static String templateContent;

    static
    {
        //TODO: сделать поддержку генерации английского варианта обработчика
        templateContent = readContents(getFileInputSupplier(TEMPLATE_NAME), TEMPLATE_NAME);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IXtextDocument doc = ModuleRefactoringUtils.getXTextDocumentFromEvent(event);

        if (doc == null)
        {
            return null;
        }

        // получим объект метаданных, к которому принадлежит модуль, из
        // которого была вызвана команда
        EObject moduleOwner = getModuleOwner(doc);

        Module moduleModel = (Module)getModuleModel(doc);
        var t = BslUtil.getAllRegionPreprocessors(moduleModel);

        RegionPreprocessor region = t.get(0);

        // offset = 0;

        Method method = moduleModel.allMethods().get(moduleModel.allMethods().size() - 1);

        int start = NodeModelUtils.findActualNodeFor(method).getOffset();
        int length = NodeModelUtils.findActualNodeFor(method).getLength();

        try
        {
            String text = "\n" + doc.get(start, length); //$NON-NLS-1$
            doc.replace(start, length, ""); //$NON-NLS-1$

            int offset = NodeModelUtils.findActualNodeFor(region.getItem()).getTotalEndOffset();
            doc.replace(offset, 0, text);

        }
        catch (BadLocationException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return null;

//            // проверим, что команда была вызвана из правильного модуля и у
//            // документа есть хотя бы 1 регистр
//            if (!(moduleOwner instanceof Document))
//            {
//                // Данный модуль не является объектным модулем документа,
//                // выдадим сообщение об этом и завершим работу
//                MessageBox dialog = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ERROR);
//                dialog.setText(Messages.DataProcessingHandler_Error);
//                dialog.setMessage(Messages.DataProcessingHandler_Error_not_document_object_module);
//                dialog.open();
//                return null;
//            }
//
//            Document mdDocument = (Document)moduleOwner;
//            List<BasicRegister> registers = findAccumulationRegister(mdDocument.getRegisterRecords());
//            if (registers.isEmpty())
//            {
//                // У документа нет ниодного регистра накопления, выдадим
//                // сообщение об этом и завершим работу
//                MessageBox dialog = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ERROR);
//                dialog.setText(Messages.DataProcessingHandler_Error);
//                dialog.setMessage(Messages.DataProcessingHandler_Error_no_accumulation_registers);
//                dialog.open();
//                return null;
//            }
//
//            // создадим диалог конструктора движения регистра
//            DataProcessingHandlerDialog dialog =
//                new DataProcessingHandlerDialog(Display.getCurrent().getActiveShell(), mdDocument, registers);
//            // обработаем завершение работы пользователя с диалогом
//            if (dialog.open() == Window.OK)
//            {
//                // создаем текст обработчика на основании того, что было выбрано
//                // в конструкторе движения регистра
//                // для этого воспользуемся заранее приготовленным шаблоном
//                // обработчика (файл с данным шаблоном находится в папке
//                // ресурсов плагина)
//                StringTemplate template = new StringTemplate(templateContent); // считали
//                                                                               // шаблон
//                                                                               // текста
//                                                                               // обработчика
//                                                                               // подставим в шаблон имя регистра,
//                                                                               // для которого делается
//                                                                               // обработка проведения
//                template.setAttribute("RegisterName", dialog.getRegisterName()); //$NON-NLS-1$
//                // формируем тело обработчика проведения на основе данных из
//                // диалога
//                String body = Joiner.on(System.lineSeparator()).join(
//                    Collections2.transform(dialog.getExpressions(), new Function<Pair<String, String>, String>()
//                    {
//                        @Override
//                        public String apply(Pair<String, String> item)
//                        {
//                            return "Запись." + item.getFirst() + " = " //$NON-NLS-1$ //$NON-NLS-2$
//                                + item.getSecond() + ';';
//                        }
//                    }));
//                template.setAttribute("body", body); // дозаполнили шаблон //$NON-NLS-1$
//                                                     // обработчика
//                try
//                {
//                    // определим позицию в модуле, в которую следует добавить
//                    // обработчик
//                    int insertPosition = getInsertHandlerPosition(doc);
//                    // добавляем текст обработчика в модуль
//                    doc.replace(insertPosition, insertPosition, template.toString());
//                }
//                catch (BadLocationException e)
//                {
//                    Activator.createErrorStatus(e.getMessage(), e);
//                }
//            }
//        }
//        return null;
    }

    /*
     * Метод выбирает всех регистраторов типа Регистр накопления
     */
    private List<BasicRegister> findAccumulationRegister(List<BasicRegister> registerRecords)
    {
        List<BasicRegister> registers = Lists.newArrayList();
        for (BasicRegister register : registerRecords)
        {
            if (register instanceof AccumulationRegister)
            {
                registers.add(register);
            }
        }
        return registers;
    }

    private int getInsertHandlerPosition(IXtextDocument doc)
    {
        //работа с семантической моделью встроенного языка через документ возможна только через специальный метод
        //использование других способов приведет к ошибкам
        int insertPosition = doc.readOnly(new IUnitOfWork<Integer, XtextResource>()
        {
            @Override
            public Integer exec(XtextResource res) throws Exception
            {
                //сперва проверяем, доступность семантической модели встроенного языка
                if (res.getContents() != null && !res.getContents().isEmpty())
                {
                    EObject obj = res.getContents().get(0);
                    if (obj instanceof Module) // проверили, что работаем с правильным объектом семантической модели
                    {
                        Module module = (Module)obj;
                        if (!module.allDeclareStatements().isEmpty())
                        {
                            // обработчик вставляется в начало модуля,
                            // сразу после объявления переменных, если
                            // они были
                            DeclareStatement lastDeclStatement =
                                module.allDeclareStatements().get(module.allDeclareStatements().size() - 1);
                            return NodeModelUtils.findActualNodeFor(lastDeclStatement).getTotalEndOffset();
                        }
                    }
                }
                return 0;
            }
        });
        return insertPosition;
    }

    private EObject getModuleModel(IXtextDocument doc)
    {
        return doc.readOnly(new IUnitOfWork<EObject, XtextResource>()
        {
            @Override
            public EObject exec(XtextResource res) throws Exception
            {
                //сперва проверяем, доступность семантической модели встроенного языка
                if (res.getContents() != null && !res.getContents().isEmpty())
                {
                    EObject obj = res.getContents().get(0);
                    if (obj instanceof Module) // проверили, что работаем с правильным объектом семантической модели
                    {
                        Module module = (Module)obj;
                        return module;
                    }
                }
                return null;
            }
        });
    }

    private EObject getModuleOwner(IXtextDocument doc)
    {
        //работа с семантической моделью встроенного языка через документ возможна только через специальный метод
        //использование других способов приведет к ошибкам
        return doc.readOnly(new IUnitOfWork<EObject, XtextResource>()
        {
            @Override
            public EObject exec(XtextResource res) throws Exception
            {
                //сперва проверяем, доступность семантической модели встроенного языка
                if (res.getContents() != null && !res.getContents().isEmpty())
                {
                    EObject obj = res.getContents().get(0);
                    if (obj instanceof Module) // проверили, что работаем с правильным объектом семантической модели
                    {
                        // интересуют только объектные модули
                        if (((Module)obj).getModuleType() != ModuleType.OBJECT_MODULE)
                        {
                            return null;
                        }
                        Module module = (Module)obj;
                        return EcoreUtil.resolve(module.getOwner(), module);
                    }
                }
                return null;
            }
        });
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
