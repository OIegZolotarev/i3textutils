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
    public static String ToolTipRepresentation;
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
    public static String TitleTextColor;
    public static String TitleFont;
    public static String Group;
    public static String ShowTitle;
    public static String Behavior;
    public static String TitleDataPath;

    public static String VerticalAlign;
    public static String HorizontalAlign;

    public static String TitleLocation;
    public static String DataPath;
    public static String FooterDataPath;
    public static String FooterTextColor;
    public static String TitleBackColor;
    public static String FooterBackColor;
    public static String FooterFont;

    public static String ChoiceButton;
    public static String ChoiceListButton;
    public static String OpenButton;
    public static String ClearButton;
    public static String SpinButton;
    public static String CreateButton;


    public static void Init(boolean isRussian)
    {
        Commands = isRussian ? "Команды" : "Commands"; //$NON-NLS-1$//$NON-NLS-2$
        Add = isRussian ? "Добавить" : "Add"; //$NON-NLS-1$ //$NON-NLS-2$
        Handler = isRussian ? "Действие" : "Handler"; //$NON-NLS-1$//$NON-NLS-2$
        Caption = isRussian ? "Заголовок" : "Title"; //$NON-NLS-1$//$NON-NLS-2$
        ModifiesStoredData = isRussian ? "ИзменяетСохраняемыеДанные" : "ModifiesStoredData"; //$NON-NLS-1$//$NON-NLS-2$
        Representation = isRussian ? "Отображение" : "Representation"; //$NON-NLS-1$//$NON-NLS-2$
        ToolTip = isRussian ? "Подсказка" : "ToolTip"; //$NON-NLS-1$//$NON-NLS-2$
        ToolTipRepresentation = isRussian ? "ОтображениеПодсказки" : "ToolTipRepresentation"; //$NON-NLS-1$//$NON-NLS-2$
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

        TitleTextColor = isRussian ? "ЦветТекстаЗаголовка" : "TitleTextColor"; //$NON-NLS-1$//$NON-NLS-2$
        TitleFont = isRussian ? "ШрифтЗаголовка" : "TitleFont"; //$NON-NLS-1$//$NON-NLS-2$
        Group = isRussian ? "Группировка" : "Group"; //$NON-NLS-1$//$NON-NLS-2$
        ShowTitle = isRussian ? "ОтображатьЗаголовок" : "ShowTitle"; //$NON-NLS-1$//$NON-NLS-2$

        Behavior = isRussian ? "Поведение" : "Behavior"; //$NON-NLS-1$//$NON-NLS-2$
        TitleDataPath = isRussian ? "ПутьКДаннымЗаголовка" : "TitleDataPath"; //$NON-NLS-1$//$NON-NLS-2$

        VerticalAlign = isRussian ? "ВертикальноеПоложение" : "VerticalAlign"; //$NON-NLS-1$//$NON-NLS-2$
        HorizontalAlign = isRussian ? "ГоризонтальноеПоложение" : "HorizontalAlign"; //$NON-NLS-1$//$NON-NLS-2$

        TitleLocation = isRussian ? "ПоложениеЗаголовка" : "TitleLocation"; //$NON-NLS-1$//$NON-NLS-2$
        DataPath = isRussian ? "ПутьКДанным" : "DataPath"; //$NON-NLS-1$//$NON-NLS-2$
        FooterDataPath = isRussian ? "ПутьКДаннымПодвала" : "FooterDataPath"; //$NON-NLS-1$//$NON-NLS-2$
        FooterTextColor = isRussian ? "ЦветТекстаПодвала" : "FooterTextColor"; //$NON-NLS-1$//$NON-NLS-2$
        TitleBackColor = isRussian ? "ЦветФонаЗаголовка" : "TitleBackColor"; //$NON-NLS-1$//$NON-NLS-2$
        FooterBackColor = isRussian ? "ЦветФонаПодвала" : "FooterBackColor"; //$NON-NLS-1$//$NON-NLS-2$
        FooterFont = isRussian ? "ШрифтПодвала" : "FooterFont"; //$NON-NLS-1$//$NON-NLS-2$

        ChoiceButton = isRussian ? "КнопкаВыбора" : "ChoiceButton"; //$NON-NLS-1$//$NON-NLS-2$
        ChoiceListButton = isRussian ? "КнопкаВыбораИзСписка" : "ChoiceListButton"; //$NON-NLS-1$//$NON-NLS-2$
        OpenButton = isRussian ? "КнопкаОткрытия" : "OpenButton"; //$NON-NLS-1$//$NON-NLS-2$
        ClearButton = isRussian ? "КнопкаОчистки" : "ClearButton"; //$NON-NLS-1$//$NON-NLS-2$
        SpinButton = isRussian ? "КнопкаРегулирования" : "SpinButton"; //$NON-NLS-1$//$NON-NLS-2$
        CreateButton = isRussian ? "КнопкаСоздания" : "CreateButton"; //$NON-NLS-1$//$NON-NLS-2$
    }
}
