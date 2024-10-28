/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit;

import org.quiteoldorange.i3textutils.formsdecompiler.CodeGenerator;
import org.quiteoldorange.i3textutils.formsdecompiler.P;

import com._1c.g5.v8.dt.form.model.Button;
import com._1c.g5.v8.dt.form.model.ManagedFormButtonType;
import com._1c.g5.v8.dt.form.model.TooltipRepresentation;
import com._1c.g5.v8.dt.mcore.ButtonRepresentation;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.Command;
import com._1c.g5.v8.dt.mcore.Font;

/**
 * @author ozolotarev
 *
 */
public class FormButtonUnit
    extends FormItemUnit
{

    private Font mFont;
    private int mWidth;
    private Color mBackColor;
    private Color mTextColor;
    private Color mBorderColor;
    private Boolean mOnlyInAllActions;
    private Boolean mSkipOnInput;
    private boolean mCheck;

    private boolean mIsDefaultButton;

    private boolean mEnabled;
    private int mTitleHeight;
    private int mHeight;
    private boolean mVisible;
    private boolean mDefaultItem;

    private TooltipRepresentation mTooltipRepresentation;

    // TODO: implement
    private ManagedFormButtonType mButtonType;
    private ButtonRepresentation mRepresentation;
    private Command mCommand;

    @Override
    public void decompile(CodeGenerator b)
    {

        super.decompile(b);

        b.writeProperty(P.Type, mButtonType);
        b.writeProperty(P.Font, mFont);

        b.writeProperty(P.Width, mWidth);
        b.writeProperty(P.BackColor, mBackColor);
        b.writeProperty(P.TextColor, mTextColor);
        b.writeProperty(P.BorderColor, mBorderColor);
        b.writeProperty(P.OnlyInAllActions, mOnlyInAllActions);
        b.writeProperty(P.SkipOnInput, mSkipOnInput);
        b.writeProperty(P.Check, mCheck);
        b.writeProperty(P.IsDefaultButton, mIsDefaultButton);
        b.writeProperty(P.Command, mCommand);
        b.writeProperty(P.Enabled, mEnabled);
        b.writeProperty(P.TitleHeight, mTitleHeight);
        b.writeProperty(P.Height, mHeight);
        b.writeProperty(P.Visible, mVisible);
        b.writeProperty(P.DefaultItem, mDefaultItem);
        b.writeProperty(P.ToolTipRepresentation, mTooltipRepresentation);
        b.writeProperty(P.Representation, mRepresentation);


    }

    /**
     * @param formgroup
     * @param item
     */
    FormButtonUnit(Button item)
    {
        super(ItemTypes.FormButton, item);

        mButtonType = item.getType();
        mDefaultItem = item.isDefaultItem();
        mVisible = item.isVisible();
        mHeight = item.getHeight();
        mTitleHeight = item.getTitleHeight();
        mEnabled = item.isEnabled();
        mTitles = item.getTitle();
        mCommand = item.getCommandName();
        mIsDefaultButton = item.isDefaultButton();
        mRepresentation = item.getRepresentation();
        mTooltipRepresentation = item.getToolTipRepresentation();
        mCheck = item.isCheck();
        mSkipOnInput = item.getSkipOnInput();
        mOnlyInAllActions = item.getOnlyInAllActions();
        mBorderColor = item.getBorderColor();
        mTextColor = item.getTextColor();
        mBackColor = item.getBackColor();
        mWidth = item.getWidth();
        mFont = item.getFont();

    }

}
