package org.quiteoldorange.i3textutils.codemining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.codemining.ICodeMining;
import org.eclipse.jface.text.codemining.ICodeMiningProvider;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
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

            //var m = Utils.getModuleFromXTextDocument((IXtextDocument)document);

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
                ArgumentsNameHintCodeMining.makeNew(this, (MethodCallNode)node, module, result);
            }

        }

    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

}
