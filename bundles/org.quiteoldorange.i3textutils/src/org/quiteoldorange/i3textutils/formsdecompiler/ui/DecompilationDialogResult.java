/**
 *
 */
package org.quiteoldorange.i3textutils.formsdecompiler.ui;

import java.util.LinkedList;
import java.util.List;

import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.Attribute;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormCommandUnit;
import org.quiteoldorange.i3textutils.formsdecompiler.decompilationunit.FormItemUnit;

/**
 * @author ozolotarev
 *
 */
public class DecompilationDialogResult
{
    List<Attribute> mSelectedAttributes = new LinkedList<>();
    List<FormCommandUnit> mSelectedCommands = new LinkedList<>();
    List<FormItemUnit> mSelectedFormItems = new LinkedList<>();

    public DecompilationDialogResult()
    {

    }

    /**
     * @return the selectedAttributes
     */
    public List<Attribute> getSelectedAttributes()
    {
        return mSelectedAttributes;
    }

    /**
     * @return the selectedCommands
     */
    public List<FormCommandUnit> getSelectedCommands()
    {
        return mSelectedCommands;
    }

    /**
     * @return the selectedFormItems
     */
    public List<FormItemUnit> getSelectedFormItems()
    {
        return mSelectedFormItems;
    }
}
