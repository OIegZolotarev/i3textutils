/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.v8interop.enums;

import com._1c.g5.v8.dt.form.model.ManagedFormFieldType;

/**
 * @author ozolotarev
 *
 */
public class FormFieldTypeEnum
    extends BuiltInEnum<ManagedFormFieldType>
{
    /**
     *
     */
    private FormFieldTypeEnum()
    {
        super("ВидПоляФормы", "FormFieldType"); //$NON-NLS-1$ //$NON-NLS-2$

        addValue(ManagedFormFieldType.HTML_DOCUMENT_FIELD, "ПолеHTMLДокумента", "HTMLDocumentField");
        addValue(ManagedFormFieldType.PDF_DOCUMENT_FIELD, "ПолеPDFДокумента", "PDFDocumentField");
        addValue(ManagedFormFieldType.INPUT_FIELD, "ПолеВвода", "InputField");
        addValue(ManagedFormFieldType.GEOGRAPHICAL_SCHEMA_FIELD, "ПолеГеографическойСхемы", "GeographicalSchemaField");
        addValue(ManagedFormFieldType.GRAPHICAL_SCHEMA_FIELD, "ПолеГрафическойСхемы", "GraphicalSchemaField");
        addValue(ManagedFormFieldType.DENDROGRAM_FIELD, "ПолеДендрограммы", "DendrogramField");
        addValue(ManagedFormFieldType.CHART_FIELD, "ПолеДиаграммы", "ChartField");
        addValue(ManagedFormFieldType.GANTT_CHART_FIELD, "ПолеДиаграммыГанта", "GanttChartField");
        addValue(ManagedFormFieldType.PROGRESS_BAR_FIELD, "ПолеИндикатора", "ProgressBarField");
        addValue(ManagedFormFieldType.CALENDAR_FIELD, "ПолеКалендаря", "CalendarField");
        addValue(ManagedFormFieldType.PICTURE_FIELD, "ПолеКартинки", "PictureField");
        addValue(ManagedFormFieldType.LABEL_FIELD, "ПолеНадписи", "LabelField");
        addValue(ManagedFormFieldType.RADIO_BUTTON_FIELD, "ПолеПереключателя", "RadioButtonField");
        addValue(ManagedFormFieldType.PERIOD_FIELD, "ПолеПериода", "PeriodField");
        addValue(ManagedFormFieldType.PLANNER_FIELD, "ПолеПланировщика", "PlannerField");
        addValue(ManagedFormFieldType.TRACK_BAR_FIELD, "ПолеПолосыРегулирования", "TrackBarField");
        addValue(ManagedFormFieldType.SPREADSHEET_DOCUMENT_FIELD, "ПолеТабличногоДокумента",
            "SpreadsheetDocumentField");
        addValue(ManagedFormFieldType.TEXT_DOCUMENT_FIELD, "ПолеТекстовогоДокумента", "TextDocumentField");
        addValue(ManagedFormFieldType.CHECK_BOX_FIELD, "ПолеФлажка", "CheckBoxField");
        addValue(ManagedFormFieldType.FORMATTED_DOCUMENT_FIELD, "ПолеФорматированногоДокумента",
            "FormattedDocumentField");

    }

    public static FormFieldTypeEnum Instance = new FormFieldTypeEnum();
}
