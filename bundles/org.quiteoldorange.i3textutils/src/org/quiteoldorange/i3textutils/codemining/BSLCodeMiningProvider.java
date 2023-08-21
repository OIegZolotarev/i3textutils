package org.quiteoldorange.i3textutils.codemining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.codemining.ICodeMining;
import org.eclipse.jface.text.codemining.ICodeMiningProvider;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.quiteoldorange.i3textutils.bsl.ModuleASTTree;
import org.quiteoldorange.i3textutils.bsl.parser.AbsractBSLElementNode;
import org.quiteoldorange.i3textutils.bsl.parser.AbstractIfElseStatement;
import org.quiteoldorange.i3textutils.bsl.parser.AbstractIfElseStatement.ConditionNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.ExpressionNode;
import org.quiteoldorange.i3textutils.bsl.parser.expressions.MethodCallNode;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.preferences.PreferenceConstants;
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.BinaryExpression;
import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.FeatureEntry;
import com._1c.g5.v8.dt.bsl.model.ForEachStatement;
import com._1c.g5.v8.dt.bsl.model.ForToStatement;
import com._1c.g5.v8.dt.bsl.model.IfStatement;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.OperatorStyleCreator;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.SourceObjectLinkProvider;
import com._1c.g5.v8.dt.bsl.model.Statement;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.TryExceptStatement;
import com._1c.g5.v8.dt.bsl.model.WhileStatement;
import com._1c.g5.v8.dt.mcore.Ctor;
import com._1c.g5.v8.dt.mcore.Type;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

public class BSLCodeMiningProvider
    implements ICodeMiningProvider
{

    boolean mCodeminingsEnabled = true;
    boolean mShowWhenContainsSubstring = false;
    boolean mShowWhenOneParameter = false;

    public BSLCodeMiningProvider()
    {
        var store = i3TextUtilsPlugin.getDefault().getPreferenceStore();
        mCodeminingsEnabled = store.getBoolean(PreferenceConstants.CODEMININGS_ENABLED);
        mShowWhenContainsSubstring =
            store.getBoolean(PreferenceConstants.CODEMININGS_SHOW_WHEN_INPUT_CONTAINS_PARAMETER_NAME);
        mShowWhenOneParameter = store.getBoolean(PreferenceConstants.CODEMININGS_SHOW_WHEN_ONE_PARAMETER);

    }

    private void addCtorParametersHint(OperatorStyleCreator op, Ctor constructor, List<ICodeMining> result,
        ScriptVariant variant)
    {
        var formalParams = constructor.getParams();
        var opParams = op.getParams();

        int index = 0;

        for (var opParam : opParams)
        {
            if (index >= formalParams.size())
                break;

            var fp = formalParams.get(index);

            String paramName = ""; //$NON-NLS-1$

            switch (variant)
            {
            case ENGLISH:
                paramName = fp.getName();
                break;
            case RUSSIAN:
                paramName = fp.getNameRu();
                break;
            default:
                paramName = fp.getNameRu();
                break;
            }

            var node = NodeModelUtils.findActualNodeFor(opParam);

            if (!mShowWhenContainsSubstring)
            {

                String s = node.getText().toUpperCase();

                if (s.indexOf(paramName.toUpperCase()) > -1)
                {
                    return;
                }
            }

            Position position = new Position(node.getOffset(), 1);

            ArgumentsNameHintCodeMining e = new ArgumentsNameHintCodeMining(position, this, paramName);
            result.add(e);

            index++;
        }

    }

    /**
     * @param left
     */
    private void addInvocationParametersHint(Invocation inv, List<ICodeMining> result, ScriptVariant variant)
    {
        var featureAccess = inv.getMethodAccess();

        EList<FeatureEntry> featureEntries = null;

        if (featureAccess instanceof StaticFeatureAccess)
        {
            featureEntries = ((StaticFeatureAccess)featureAccess).getFeatureEntries();
        }
        else if (featureAccess instanceof DynamicFeatureAccess)
        {
            featureEntries = ((DynamicFeatureAccess)featureAccess).getFeatureEntries();
        }

        if (featureEntries != null)
        {
            for (FeatureEntry entry : featureEntries)
            {
                EObject feature = entry.getFeature();
                List<String> names = new LinkedList<>();

                if (!dumpParametersNameFromMethod(inv, feature, names, inv, variant))
                    continue;

                makeParametersHints(inv.getParams(), names, result);

                for (var param : inv.getParams())
                {
                    if (param instanceof Invocation)
                        addInvocationParametersHint((Invocation)param, result, variant);
                    else if (param instanceof DynamicFeatureAccess)
                    {
                        DynamicFeatureAccess dfa = (DynamicFeatureAccess)param;
                        var source = dfa.getSource();

                        if (source instanceof Invocation)
                            addInvocationParametersHint((Invocation)source, result, variant);
                    }
                    else if (param instanceof OperatorStyleCreator)
                        traverseOperatorStyleCreate(param, result, variant);

                }

            }
        }

    }

    private boolean dumpParametersNameFromMethod(EObject context, EObject feature, List<String> names, Invocation inv,
        ScriptVariant variant)
    {
        if (feature instanceof Method)
        {
            Method method = (Method)feature;

            for (var item : method.getFormalParams())
            {
                names.add(item.getName());
            }

            return true;

        }

        else if (feature instanceof com._1c.g5.v8.dt.mcore.Method)
        {
            if (feature instanceof SourceObjectLinkProvider)
            {
                SourceObjectLinkProvider solp = (SourceObjectLinkProvider)feature;

                InternalEObject source = (InternalEObject)EcoreFactory.eINSTANCE.createEObject();
                source.eSetProxyURI(solp.getSourceUri());

                var resolvedObject = EcoreUtil.resolve(source, context);

                if (resolvedObject instanceof Method)
                {
                    Method m = (Method)resolvedObject;

                    for (var item : m.getFormalParams())
                    {
                        names.add(item.getName());
                    }
                }
            }

            else
            {
                com._1c.g5.v8.dt.mcore.Method methodImp = (com._1c.g5.v8.dt.mcore.Method)feature;

                var paramSet = methodImp.actualParamSet(inv.getParams().size());

                if (paramSet == null)
                    return false;

                for (var item : paramSet.getParams())
                {
                    if (variant == ScriptVariant.RUSSIAN)
                        names.add(item.getNameRu());
                    else
                        names.add(item.getName());
                }
            }
            return true;
        }

        return false;
    }

    private Ctor findBestCtor(Type type, OperatorStyleCreator op)
    {
        int numParams = op.getParams().size();
        Ctor best = type.getCtors().get(0);

        for (Ctor ctor : type.getCtors())
        {
            if (numParams >= ctor.getMinParams() && (numParams <= ctor.getMaxParams() || ctor.getMaxParams() == -1))
            {
                int minParams = ctor.getMinParams();
                if (minParams == numParams)
                    return ctor;
            }

            best = ctor;

        }

        return best;
    }

    private void makeParametersHints(EList<Expression> invocationParams, List<String> paramsNames,
        List<ICodeMining> result)
    {
        if (paramsNames.size() < 2 && !mShowWhenOneParameter)
            return;

        int index = 0;
        for (var item : paramsNames)
        {

            if (index >= invocationParams.size())
                break;

            var node = NodeModelUtils.findActualNodeFor(invocationParams.get(index));

            if (!mShowWhenContainsSubstring)
            {

                String s = node.getText().toUpperCase();

                if (s.indexOf(item.toUpperCase()) > -1)
                {
                    return;
                }
            }

            Position position = new Position(node.getOffset(), 1);

            ArgumentsNameHintCodeMining e = new ArgumentsNameHintCodeMining(position, this, item);
            result.add(e);

            index++;
        }
    }

    @Override
    public CompletableFuture<List<? extends ICodeMining>> provideCodeMinings(ITextViewer viewer,
        IProgressMonitor monitor)
    {
        //addAnnotationPainter((ISourceViewer)viewer);

        // TODO Auto-generated method stub
        return CompletableFuture.supplyAsync(() -> {

            if (!mCodeminingsEnabled)
                return Collections.emptyList();

            IXtextDocument document = (IXtextDocument)viewer.getDocument();

            List<ICodeMining> result = new ArrayList<>();

            //ModuleASTTree tree = new ModuleASTTree(document.get());
            //traversei3Tree(tree, tree, result);

            var module = Utils.getModuleFromXTextDocument(document);
            var scriptVariant = Utils.getDocScriptVariant(document);

            traverseEDTModule(module, result, scriptVariant);

            return result;
        });
    }

    private void traverseEDTModule(Module module, List<ICodeMining> result, ScriptVariant scriptVariant)
    {
        for (Method method : module.allMethods())
        {
            for (var statement : method.allStatements())
            {
                traverseStatement(statement, result, scriptVariant);
            }
        }
    }

    private void traverseExpression(Expression exp, List<ICodeMining> result, ScriptVariant variant)
    {
        if (exp == null)
            return;

        if (exp instanceof Invocation)
        {
            addInvocationParametersHint((Invocation)exp, result, variant);
        }
        else if (exp instanceof BinaryExpression)
        {
            BinaryExpression bExp = (BinaryExpression)exp;
            traverseExpression(bExp.getLeft(), result, variant);
            traverseExpression(bExp.getRight(), result, variant);
        }
        else if (exp instanceof OperatorStyleCreator)
        {
            traverseOperatorStyleCreate(exp, result, variant);
        }
    }

    /**
     * @param tree
     * @param result
     */
    @SuppressWarnings("unused")
    private void traversei3Tree(ModuleASTTree module, AbsractBSLElementNode tree, List<ICodeMining> result)
    {

        for (var node : tree.getChildren())
        {
            traversei3Tree(module, node, result);

            if (node instanceof MethodCallNode)
            {
                ArgumentsNameHintCodeMining.makeNew(this, (MethodCallNode)node, module, result);
            }

            if (node instanceof ConditionNode)
            {
                AbstractIfElseStatement.ConditionNode conditionNode = (AbstractIfElseStatement.ConditionNode)node;
                ExpressionNode conditionalExpression = conditionNode.getConditionalExpression();

                if (conditionalExpression != null)
                {
                    traversei3Tree(module, conditionalExpression, result);
                }
            }

        }

    }

    /**
     * @param exp
     * @param result
     * @param variant
     */
    private void traverseOperatorStyleCreate(Expression exp, List<ICodeMining> result, ScriptVariant variant)
    {
        OperatorStyleCreator op = (OperatorStyleCreator)exp;
        var type = op.getType();

        Ctor ctor = findBestCtor(type, op);

        addCtorParametersHint(op, ctor, result, variant);
    }

    private void traverseStatement(Statement statement, List<ICodeMining> result, ScriptVariant variant)
    {
        if (statement instanceof SimpleStatement)
        {
            SimpleStatement ss = (SimpleStatement)statement;

            var node = NodeModelUtils.findActualNodeFor(ss);
            String s = node.getText();
            s.toString();

            traverseExpression(ss.getLeft(), result, variant);
            traverseExpression(ss.getRight(), result, variant);
        }
        else if (statement instanceof IfStatement)
        {
            IfStatement ifStatement = (IfStatement)statement;

            var ifPart = ifStatement.getIfPart();

            if (ifPart != null)
            {
                traverseExpression(ifPart.getPredicate(), result, variant);

                for (var item : ifPart.getStatements())
                    traverseStatement(item, result, variant);
            }

            var elseifParts = ifStatement.getElsIfParts();

            for (var part : elseifParts)
            {
                traverseExpression(part.getPredicate(), result, variant);

                for (var item : part.getStatements())
                    traverseStatement(item, result, variant);
            }

            for (var item : ifStatement.getElseStatements())
                traverseStatement(item, result, variant);

        }
        else if (statement instanceof TryExceptStatement)
        {
            TryExceptStatement te = (TryExceptStatement)statement;

            for (var stmt : te.getTryStatements())
            {
                traverseStatement(stmt, result, variant);
            }

            for (var stmt : te.getExceptStatements())
            {
                traverseStatement(stmt, result, variant);
            }
        }
        else if (statement instanceof WhileStatement)
        {
            WhileStatement ls = (WhileStatement)statement;

            for (var stmt : ls.getStatements())
            {
                traverseStatement(stmt, result, variant);
            }

            traverseExpression(ls.getPredicate(), result, variant);

        }
        else if (statement instanceof ForToStatement)
        {
            ForToStatement ls = (ForToStatement)statement;

            for (var stmt : ls.getStatements())
            {
                traverseStatement(stmt, result, variant);
            }

            traverseExpression(ls.getInitializer(), result, variant);
            traverseExpression(ls.getBound(), result, variant);

        }
        else if (statement instanceof ForEachStatement)
        {
            ForEachStatement ls = (ForEachStatement)statement;

            for (var stmt : ls.getStatements())
            {
                traverseStatement(stmt, result, variant);
            }

            traverseExpression(ls.getCollection(), result, variant);
        }

        // TODO: traverse unary expressions
        // TODO: исключение для "вставить"

    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

}
