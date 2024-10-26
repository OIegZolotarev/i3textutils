/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationContext;
import org.quiteoldorange.i3textutils.formsdecompiler.DecompilationSettings;
import org.quiteoldorange.i3textutils.formsdecompiler.P;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.ChildFormItemsGroupEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.ManagedGroupTypeEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.TooltipRepresentationEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.UsualGroupBehaviorEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.UsualGroupRepresentationEnum;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.V8Color;
import org.quiteoldorange.i3textutils.formsdecompiler.v8interop.V8Font;

import com._1c.g5.v8.dt.form.model.FormGroup;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.model.GroupExtInfo;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;
import com._1c.g5.v8.dt.form.model.TooltipRepresentation;
import com._1c.g5.v8.dt.form.model.UsualGroupExtInfo;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.Font;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class FormGroupUnit
    extends FormItemUnit
{
    private boolean mEnabled = true;
    private ManagedFormGroupType mGroupType;
    private int mHeight = 0;

    private boolean mReadonly = false;
    private boolean mVisible = true;

    private int mWidth = 0;
    private boolean mEnableContentChange;
    private EMap<String, String> mToolTip;
    private boolean mHorizontalStretch = false;
    private boolean mVerticalStretch = false;
    private TooltipRepresentation mToolTipRepresentation;
    private Font mTitleFont;
    private Color mTitleTextColor;
    private GroupExtInfo mExtInfo;

    /**
     * @param type
     */
    public FormGroupUnit(FormGroup group)
    {
        super(ItemTypes.FormGroup, group);
        mGroupType = group.getType();
        mTitles = group.getTitle();

        mEnabled = group.isEnabled();
        mReadonly = group.isReadOnly();
        mVisible = group.isVisible();
        mEnableContentChange = group.isEnableContentChange();

        mToolTip = group.getToolTip();
        mToolTipRepresentation = group.getToolTipRepresentation();

        mHeight = group.getHeight();
        mWidth = group.getWidth();

        mTitleFont = group.getTitleFont();
        mTitleTextColor = group.getTitleTextColor();

        // За каким-то чертом эти свойства Boolean вместо boolean
        // С одной стороны неплохо - можно понять что свойства не определены (==null)
        // С другой стороны не понятно почему с остальными так не сделали.
        if (group.getVerticalStretch() != null)
            mVerticalStretch = group.getVerticalStretch();

        if (group.getHorizontalStretch() != null)
            mHorizontalStretch = group.getHorizontalStretch();

//        UsualGroupExtInfoImpl mBehaviour = group.getExtInfo();
//        mBehaviour.getgr

        mExtInfo = group.getExtInfo();

        mToolTipRepresentation = group.getToolTipRepresentation();

        for (FormItem items : group.getItems())
        {
            addChildren(FormItemUnit.construct(items));
        }
    }


    interface StringPropertyWriter
    {
        void w(String property, String value);
    };

    interface BooleanPropertyWriter
    {
        void w(String property, boolean value);
    };

    interface IntPropertyWriter
    {
        void w(String property, int value);
    }

    @Override
    public void decompile(StringBuilder b, DecompilationContext context)
    {
        super.decompile(b, context);

        DecompilationSettings cfg = context.getDecompilationSettings();

        boolean outputDefaultValues = cfg.outputDefaultValues();
        String newItem = cfg.getNewItemTemplateName();

        StringPropertyWriter sp = (String p, String v) -> {
            String line = String.format("%s.%s = %s;\n", newItem, p, v); //$NON-NLS-1$
            b.append(line);
        };

        BooleanPropertyWriter bp = (String p, boolean v) -> {
            String line = String.format("%s.%s = %s;\n", newItem, p, cfg.serializeBoolean(v)); //$NON-NLS-1$
            b.append(line);
        };

        IntPropertyWriter ip = (String p, int v) -> {
            String line = String.format("%s.%s = %d;\n", newItem, p, v); //$NON-NLS-1$
            b.append(line);
        };

        sp.w(P.Type, serializeManagedGroupType(cfg.scriptVariant()));

        // TODO: Придумать какие-то оболочки для свойствами 1С, или оставить все как есть?
        // Чтобы не писать такой огород, а сделать более красивый код? Или пофигу?
        if (!mVisible || outputDefaultValues)
            bp.w(P.Visible, mVisible);

        if (mHeight != 0 || outputDefaultValues)
            ip.w(P.Height, mHeight);

        if (mWidth != 0 || outputDefaultValues)
            ip.w(P.Width, mWidth);

        if (!mEnabled || outputDefaultValues)
            bp.w(P.Enabled, mEnabled);


        sp.w(P.Caption, serializeMultiLangualString(mTitles, cfg));
        sp.w(P.ToolTip, serializeMultiLangualString(mToolTip, cfg));

        sp.w(P.ToolTipRepresentation,
            TooltipRepresentationEnum.Instance.serialize(mToolTipRepresentation, cfg.scriptVariant()));

        bp.w(P.EnableContentChange, mEnableContentChange);
        bp.w(P.VerticalStretch, mVerticalStretch);
        bp.w(P.HorizontalStretch, mHorizontalStretch);
        bp.w(P.Readonly, mReadonly);

        if (mTitleTextColor != null)
        {
            sp.w(P.TitleTextColor, V8Color.serialize(mTitleTextColor, cfg));
        }

        if (mTitleFont != null)
        {
            sp.w(P.TitleFont, V8Font.serialize(mTitleFont, cfg));

        }

        if (mExtInfo instanceof UsualGroupExtInfo)
        {

            UsualGroupExtInfo info = (UsualGroupExtInfo)mExtInfo;
            sp.w(P.Group, ChildFormItemsGroupEnum.Instance.serialize(info.getGroup(), cfg.scriptVariant()));
            bp.w(P.ShowTitle, info.isShowTitle());

            sp.w(P.Representation,
                UsualGroupRepresentationEnum.Instance.serialize(info.getRepresentation(), cfg.scriptVariant()));

            sp.w(P.Behavior, UsualGroupBehaviorEnum.Instance.serialize(info.getBehavior(), cfg.scriptVariant()));

            sp.w(P.TitleDataPath, cfg.serializeAbstractDataPath(info.getTitleDataPath()));

            // info.get
        }

        b.append("\n");

//
//        Если ЭлементОбразе ц.Вид = ВидГруппыФормы.ОбычнаяГруппа Тогда

//            СтруктураКопируемыхСвойств.Вставить("ПутьКДаннымЗаголовка","");  //Определяется в "ДанныеЭлементов"
//        Иначе
//            //Другие виды
//        КонецЕсли;

        // .append(serializeManagedGroupType(false));
    }

    private String serializeManagedGroupType(ScriptVariant variant)
    {
        return ManagedGroupTypeEnum.Instance.serialize(mGroupType, variant);
    }

    @Override
    public String toString()
    {
        return String.format("Группа формы: %s", getName()); //$NON-NLS-1$
    }

}
