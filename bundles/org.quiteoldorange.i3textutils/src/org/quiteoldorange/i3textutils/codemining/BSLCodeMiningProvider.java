package org.quiteoldorange.i3textutils.codemining;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
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
import org.quiteoldorange.i3textutils.refactoring.Utils;

import com._1c.g5.v8.dt.bsl.model.BinaryExpression;
import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.FeatureEntry;
import com._1c.g5.v8.dt.bsl.model.IfStatement;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.OperatorStyleCreator;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.Statement;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.metadata.mdclass.ScriptVariant;

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
            OperatorStyleCreator op = (OperatorStyleCreator)exp;
            op.getType();

        }
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

        }
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

    private void makeParametersHints(EList<Expression> invocationParams, List<String> paramsNames,
        List<ICodeMining> result)
    {
        if (paramsNames.size() < 2)
            return;

        int index = 0;
        for (var item : paramsNames)
        {

            if (index >= invocationParams.size())
                break;

            var node = NodeModelUtils.findActualNodeFor(invocationParams.get(index));
            String s = node.getText().toUpperCase();

            String upperCaseParam = item.toUpperCase();

            if (s.indexOf(upperCaseParam) > -1)
            {
                return;
            }

            Position position = new Position(node.getOffset(), 1);

            ArgumentsNameHintCodeMining e = new ArgumentsNameHintCodeMining(position, this, item);
            result.add(e);

            index++;
        }
    }

    private boolean dumpParametersNameFromMethod(EObject feature, List<String> names, Invocation inv,
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

            return true;
        }

        return false;
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

                if (!dumpParametersNameFromMethod(feature, names, inv, variant))
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
                }

            }
        }

    }

    /**
     * @param tree
     * @param result
     */
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

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

}
