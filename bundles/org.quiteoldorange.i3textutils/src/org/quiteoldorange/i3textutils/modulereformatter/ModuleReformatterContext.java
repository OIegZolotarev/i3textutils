/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;

/**
 * @author ozolotarev
 *
 */
public class ModuleReformatterContext
{
    IXtextDocument mDoc;

    // Стратегия реформата модуля
    // 1) Взять эталонную структуру модуля и распарсить её
    // 2) Распарсить нашу структуру модуля
    // 3) Определить каких областей не хватает до эталона - добавить их и упорядочить
    // 4) Определить какие методы в какую область идут, сохраняя произвольные родительские области
    //    Например метод "ПриветМир() Экспорт" в области "Хуета" попадет "ПрограммныйИнтерфейс -> Хуета -> ПриветМир"
    // 5) Для модулей без директив компиляции - добавить на "на сервере", для модулей не упоминающих реквизиты формы
    //        - "на сервере без контекста"

    public ModuleReformatterContext(IXtextDocument doc)
    {
        mDoc = doc;
    }
}
