package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.source.ISourceViewerExtension5;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.ui.editor.BslXtextEditor;

public class DebugParserCommand
    extends AbstractHandler
    implements IHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
            return null;

        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        XtextEditor target = part.getAdapter(XtextEditor.class);

        BslXtextEditor ed = (BslXtextEditor)target;
        var ex = (ISourceViewerExtension5)ed.getInternalSourceViewer();
        ex.updateCodeMinings();

        Lexer lex = new Lexer(doc.get());
        ModuleASTTree tree = new ModuleASTTree(lex);


        return null;
    }

}
