/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler;

/**
 * @author ozolotarev
 *
 */
public class P
{
    public static String Commands;
    public static String Add;
    public static String Handler;
    public static String Caption;
    public static String ModifiesStoredData;
    public static String Representation;
    public static String ToolTip;
    public static String Type;
    public static String Visible;
    public static String Height;
    public static String Width;
    public static String EnableContentChange;
    public static String Enabled;
    public static String VerticalStretch;
    public static String HorizontalStretch;
    public static String Readonly;
    public static String Items;
    public static String Insert;

    public static String Undefined;
    public static String TypeFunction;

    public static String FormButton;
    public static String FormDecoration;
    public static String FormField;
    public static String FormGroup;
    public static String FormTable;

    public static void Init(boolean isRussian)
    {
        Commands = isRussian ? "Команды" : "Commands"; //$NON-NLS-1$//$NON-NLS-2$
        Add = isRussian ? "Добавить" : "Add"; //$NON-NLS-1$ //$NON-NLS-2$
        Handler = isRussian ? "Действие" : "Handler"; //$NON-NLS-1$//$NON-NLS-2$
        Caption = isRussian ? "Заголовок" : "Title"; //$NON-NLS-1$//$NON-NLS-2$
        ModifiesStoredData = isRussian ? "ИзменяетСохраняемыеДанные" : "ModifiesStoredData"; //$NON-NLS-1$//$NON-NLS-2$
        Representation = isRussian ? "Отображение" : "Representation"; //$NON-NLS-1$//$NON-NLS-2$
        ToolTip = isRussian ? "Подсказка" : "ToolTip"; //$NON-NLS-1$//$NON-NLS-2$
        Enabled = isRussian ? "Доступность" : "Enabled"; //$NON-NLS-1$//$NON-NLS-2$
        Visible = isRussian ? "Видимость" : "Visible"; //$NON-NLS-1$//$NON-NLS-2$
        Height = isRussian ? "Высота" : "Height"; //$NON-NLS-1$//$NON-NLS-2$
        Width = isRussian ? "Ширина" : "Ширина"; //$NON-NLS-1$//$NON-NLS-2$
        EnableContentChange = isRussian ? "РазрешитьИзменениеСостава" : "EnableContentChange"; //$NON-NLS-1$//$NON-NLS-2$

        Type = isRussian ? "Вид" : "Type"; //$NON-NLS-1$//$NON-NLS-2$
        Readonly = isRussian ? "ТолькоПросмотр" : "ReadOnly"; //$NON-NLS-1$//$NON-NLS-2$
        VerticalStretch = isRussian ? "РастягиватьПоВертикали" : "VerticalStretch"; //$NON-NLS-1$//$NON-NLS-2$
        HorizontalStretch = isRussian ? "РастягиватьПоГоризонтали" : "HorizontalStretch"; //$NON-NLS-1$//$NON-NLS-2$

        Items = isRussian ? "Элементы" : "Items"; //$NON-NLS-1$//$NON-NLS-2$
        Insert = isRussian ? "Вставить" : "Insert"; //$NON-NLS-1$//$NON-NLS-2$
        Undefined = isRussian ? "Неопределено" : "Undefined"; //$NON-NLS-1$//$NON-NLS-2$

        TypeFunction = isRussian ? "Тип" : "Type"; //$NON-NLS-1$//$NON-NLS-2$

        FormDecoration = isRussian ? "ДекорацияФормы" : "FormDecoration"; //$NON-NLS-1$//$NON-NLS-2$
        FormGroup = isRussian ? "ГруппаФормы" : "FormGroup"; //$NON-NLS-1$//$NON-NLS-2$
        FormButton = isRussian ? "КнопкаФормы" : "FormButton"; //$NON-NLS-1$//$NON-NLS-2$
        FormTable = isRussian ? "ТаблицаФормы" : "FormTable"; //$NON-NLS-1$//$NON-NLS-2$
        FormField = isRussian ? "ПолеФормы" : "FormField"; //$NON-NLS-1$//$NON-NLS-2$

    }
}
