/**
 *
 */
package org.quiteoldorange.i3textutils.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.ServicesAdapter;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.modulereformatter.ModuleReformatterContext;
import org.quiteoldorange.i3textutils.refactoring.ModuleElement;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

/**
 * @author ozolotarev
 *
 */
public class ReformatModule
    extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IXtextDocument doc = Utils.getXTextDocumentFromEvent(event);

        if (doc == null)
            return null;

        var model = Utils.getModuleFromXTextDocument(doc);
        var project = Utils.getProjectFromEvent(event);

        ModuleReformatterContext cont = new ModuleReformatterContext(project, model, doc);
        cont.run();


        //debugParser(doc, event);
        //legacy(doc);

        return null;

    }

    /**
     * @param doc
     */
    private void legacy(IXtextDocument doc)
    {
        var elements = ModuleElement.collectFromModule(doc);

        elements.sort((ModuleElement a, ModuleElement b) -> {
            return a.getName().compareTo(b.getName());
        });

        StringBuilder newDocString = new StringBuilder();

        for (ModuleElement el : elements)
        {
            newDocString.append(el.getSourceText() + "\n\n"); //$NON-NLS-1$
        }

        try
        {
            doc.replace(0, doc.getLength(), newDocString.toString());
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param doc
     */
    private void debugParser(IXtextDocument doc, ExecutionEvent event)
    {
        Lexer l = new Lexer(doc.get());
        l.setLazyMode(true);
        ModuleASTTree tree = new ModuleASTTree(l);

        var model = Utils.getModuleFromXTextDocument(doc);

        // Shit - Доставание маркеров от v8 codestyle !!!!
        var project = Utils.getProjectFromEvent(event);

        var markerManager = ServicesAdapter.instance().getMarkerManager();

        String id = EcoreUtil.getURI(model).trimFragment().toPlatformString(true);

        var m = markerManager.getMarkers(project, id);

        // Shit

        var s = tree.serialize(ScriptVariant.RUSSIAN);
    }

}
