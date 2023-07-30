package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.refactoring.Utils;

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

        Lexer lex = new Lexer(doc.get());
        ModuleASTTree tree = new ModuleASTTree(lex);


        return null;
    }

}
