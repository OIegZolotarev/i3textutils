/**
 *
 */
package org.quiteoldorange.i3textutils.codemining;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.codemining.ICodeMining;
import org.eclipse.jface.text.codemining.ICodeMiningProvider;
import org.eclipse.jface.text.codemining.LineContentCodeMining;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.MethodCallNode;

/**
 * @author ozolotarev
 *
 */
public class ArgumentsNameHintCodeMining
    extends LineContentCodeMining
{

    @Override
    public Point draw(GC gc, StyledText textWidget, Color color, int x, int y)
    {
        var ext = gc.stringExtent(getLabel());

        // Фон
        gc.setBackground(new Color(new RGB(10, 10, 10)));
        gc.fillRoundRectangle(x, y, ext.x, ext.y, 8, 8);
        // Фон


        // Текст
        gc.setForeground(new Color(new RGB(0, 0, 0)));
        gc.setForeground(new Color(new RGB(255, 255, 255)));

        gc.drawString(getLabel(), x + 4, y, true);
        // Текст


        return ext;
    }

    String mArgName;

    @Override
    protected CompletableFuture<Void> doResolve(ITextViewer viewer, IProgressMonitor monitor)
    {
        return CompletableFuture.runAsync(() -> {
            this.setLabel(mArgName + ": "); //$NON-NLS-1$

        });
    }

    /**
     * @param position
     * @param provider
     */
    public ArgumentsNameHintCodeMining(Position position, ICodeMiningProvider provider, String argName)
    {
        super(position, provider);
        mArgName = argName;

    }

    @Override
    public String toString()
    {
        return "ArgumentsNameHintCodeMining [mArgName=" + mArgName + ", getPosition()=" + getPosition() + "]";
    }

    public static void makeNew(ICodeMiningProvider provider, MethodCallNode methodCallNode, ModuleASTTree module,
        List<ICodeMining> result)
    {
        MethodNode methodDef = module.findMethodDefinition(methodCallNode);



        if (methodDef == null)
            return;

        List<MethodNode.ArgumentDefinition> argsDef = methodDef.getArguments();

        ExpressionNode callArgs = methodCallNode.getArgumentsExpression();

        int index = 0;
        for (var arg : callArgs.getChildren())
        {
            if (arg.getStartingOffset() < 0)
            {
                //int a = 1;
            }
            Position position = new Position(arg.getStartingOffset(), 1);
            String argName = argsDef.get(index).getName();

            ArgumentsNameHintCodeMining e = new ArgumentsNameHintCodeMining(position, provider, argName);
            result.add(e);

            index++;
        }
    }

}
