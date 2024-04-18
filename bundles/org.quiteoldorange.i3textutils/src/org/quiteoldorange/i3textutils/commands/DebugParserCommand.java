package org.quiteoldorange.i3textutils.commands;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.Log;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.FeatureEntry;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.impl.InvocationImpl;
import com._1c.g5.v8.dt.bsl.model.impl.SimpleStatementImpl;
import com._1c.g5.v8.dt.bsl.ui.BslDocumentationProvider;
import com._1c.g5.v8.dt.mcore.Method;


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

        Lexer lex = new Lexer(doc.get());
        @SuppressWarnings("unused")
        ModuleASTTree tree = new ModuleASTTree(lex);

        return null;
    }

    /**
     * @param m
     * @param docum
     */
    @SuppressWarnings("unused")
    private void debug(Module m, BslDocumentationProvider docum)
    {
        try
        {
            String d = docum.getDocFromUrl(new URL(
                "platform:/resource/Заказы/src/Documents/ЗаказКлиента/Forms/ФормаВыбора/Module.bsl#/_method/Тест/0")); //$NON-NLS-1$

            var method = m.allMethods().get(1);
            var st = method.allStatements();
            var a = st.get(0);

            if (a instanceof SimpleStatementImpl)
            {
                SimpleStatementImpl ssi = (SimpleStatementImpl)a;

                InvocationImpl iimp = (InvocationImpl)ssi.getLeft();
                var ma = (StaticFeatureAccess)iimp.getMethodAccess();

                var fea = ma.getFeatureEntries();

                for (FeatureEntry entry : fea)
                {
                    EObject feature = entry.getFeature();
                    Method methodImp = (Method)feature;

                    var p = methodImp.actualParamSet(1);
                    var param = p.getParams().get(0);
                    String paramName = param.getName();
                    paramName.toString();

                    param = p.getParams().get(1);
                    paramName = param.getName();
                    paramName.toString();


                }

            }

            Log.Debug(d);
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
