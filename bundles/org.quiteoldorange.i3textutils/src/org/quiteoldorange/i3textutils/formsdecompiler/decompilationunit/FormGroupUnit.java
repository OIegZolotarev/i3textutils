/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.eclipse.emf.common.util.EMap;
import org.quiteoldorange.i3textutils.formsdecompiler.CodeGenerator;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.FormGroup;
import com._1c.g5.v8.dt.form.model.FormItem;
import com._1c.g5.v8.dt.form.model.GroupExtInfo;
import com._1c.g5.v8.dt.form.model.ManagedFormGroupType;
import com._1c.g5.v8.dt.form.model.TooltipRepresentation;
import com._1c.g5.v8.dt.form.model.UsualGroupExtInfo;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.Font;

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


    @Override
    public void decompile(CodeGenerator b)
    {
        super.decompile(b);

        b.writeProperty(P.Type, mGroupType);
        b.writeProperty(P.Visible, mVisible);
        b.writeProperty(P.Height, mHeight);
        b.writeProperty(P.Width, mWidth);
        b.writeProperty(P.Enabled, mEnabled);

        b.writeMultilangString(P.Caption, mTitles);
        b.writeMultilangString(P.ToolTip, mToolTip);

        b.writeProperty(P.ToolTipRepresentation, mToolTipRepresentation);

        b.writeProperty(P.EnableContentChange, mEnableContentChange);
        b.writeProperty(P.VerticalStretch, mVerticalStretch);
        b.writeProperty(P.HorizontalStretch, mHorizontalStretch);
        b.writeProperty(P.Readonly, mReadonly);

        b.writeProperty(P.TitleTextColor, mTitleTextColor);
        b.writeProperty(P.TitleFont, mTitleFont);

        // TODO: реализовать остальные варианты групп
        if (mExtInfo instanceof UsualGroupExtInfo)
        {
            UsualGroupExtInfo info = (UsualGroupExtInfo)mExtInfo;

            b.writeProperty(P.Group, info.getGroup());
            b.writeProperty(P.ShowTitle, info.isShowTitle());
            b.writeProperty(P.Representation, info.getRepresentation());
            b.writeProperty(P.Behavior, info.getBehavior());
            b.writeProperty(P.TitleDataPath, info.getTitleDataPath());
        }

    }

    @Override
    public String toString()
    {
        return String.format("Группа формы: %s", getName()); //$NON-NLS-1$
    }

}
