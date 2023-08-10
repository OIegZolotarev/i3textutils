package org.quiteoldorange.i3textutils.codemining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.codemining.ICodeMining;
import org.eclipse.jface.text.codemining.ICodeMiningProvider;
import org.eclipse.jface.text.codemining.LineContentCodeMining;

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
            int numberOfLines = document.getNumberOfLines();
            List<ICodeMining> result = new ArrayList<>();
            for (int i = 0; i < numberOfLines; i++)
            {
                try
                {
                    IRegion lineInformation = document.getLineInformation(i);
                    Position position = new Position(lineInformation.getOffset() + lineInformation.getLength(), 1);
                    LineContentCodeMining e = new LineContentCodeMining(position, this)
                    {
                        @Override
                        protected CompletableFuture<Void> doResolve(ITextViewer viewer, IProgressMonitor monitor)
                        {
                            return CompletableFuture.runAsync(() -> {
                                try
                                {
                                    this.setLabel(
                                        document.get(lineInformation.getOffset(), lineInformation.getLength()));
                                }
                                catch (BadLocationException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            });
                        }

                    };
                    result.add(e);
                }
                catch (org.eclipse.jface.text.BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
            return result;
        });
    }


    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

}
