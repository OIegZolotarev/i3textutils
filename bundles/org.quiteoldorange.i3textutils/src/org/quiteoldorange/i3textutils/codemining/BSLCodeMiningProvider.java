package org.quiteoldorange.i3textutils.codemining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.codemining.ICodeMining;
import org.eclipse.jface.text.codemining.ICodeMiningProvider;
import org.eclipse.jface.text.codemining.LineContentCodeMining;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.MethodNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.MethodCallNode;

public class BSLCodeMiningProvider
    implements ICodeMiningProvider
{

    public BSLCodeMiningProvider()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public CompletableFuture<List<? extends ICodeMining>> provideCodeMinings(ITextViewer viewer,
        IProgressMonitor monitor)
    {
        // TODO Auto-generated method stub
        return CompletableFuture.supplyAsync(() -> {

            IDocument document = viewer.getDocument();
            ModuleASTTree tree = new ModuleASTTree(document.get());
            List<ICodeMining> result = new ArrayList<>();

            TraverseTree(tree, tree, result);

            return result;
        });
    }

    /**
     * @param tree
     * @param result
     */
    private void TraverseTree(ModuleASTTree module, AbsractBSLElementNode tree, List<ICodeMining> result)
    {

        for (var node : tree.getChildren())
        {
            TraverseTree(module, node, result);

            if (node instanceof MethodCallNode)
            {
                MethodCallNode methodCallNode = (MethodCallNode)node;
                MethodNode methodDef = module.findMethodDefinition(methodCallNode);

                if (methodDef == null)
                    continue;

                List<MethodNode.ArgumentDefinition> argsDef = methodDef.getArguments();

                ExpressionNode callArgs = methodCallNode.getArgumentsExpression();

                int index = 0;
                for (var arg : callArgs.getChildren())
                {
                    final int idx = index;

                    Position position = new Position(arg.getStartingOffset(), 1);

                    LineContentCodeMining e = new LineContentCodeMining(position, this)
                    {
                        @Override
                        protected CompletableFuture<Void> doResolve(ITextViewer viewer, IProgressMonitor monitor)
                        {


                            return CompletableFuture.runAsync(() -> {
                                this.setLabel(argsDef.get(idx).getName() + ": "); //$NON-NLS-1$
                            });

                        }

                    };

                    result.add(e);
                    index++;
                }

            }

//            try
//            {
//                IRegion lineInformation = document.getLineInformation(i);
//
//                LineContentCodeMining e = new LineContentCodeMining(position, this)
//                {
//                    @Override
//                    protected CompletableFuture<Void> doResolve(ITextViewer viewer, IProgressMonitor monitor)
//                    {
//                        return CompletableFuture.runAsync(() -> {
//                            try
//                            {
//                                this.setLabel(document.get(lineInformation.getOffset(), lineInformation.getLength()));
//                            }
//                            catch (BadLocationException e)
//                            {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        });
//                    }
//
//                };
//                result.add(e);
//            }
//            catch (org.eclipse.jface.text.BadLocationException e)
//            {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

}
