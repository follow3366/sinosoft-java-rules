package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.java.model.expression.LiteralTreeImpl;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

/**
 * 在业务层类中不允许出现拼写 SQL 的语句。
 */
@Rule(key = "SQLStatementsNotAllowedInBLLayer")
public class SQLStatementsNotAllowedInBLLayer extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private Boolean isBLLayer = Boolean.FALSE;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        if (tree.packageDeclaration() != null) {
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");
            isBLLayer = name.contains(".bl.");//true
        }
        super.visitCompilationUnit(tree);
    }

    // 扫描 方法树 ，
    @Override
    public void visitMethod(MethodTree tree) {
        if (isBLLayer){
            List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
            for (ControlFlowGraph.Block tcb : tcbs){
                List<Tree> vtl = tcb.elements();
                for (Tree vt : vtl){
                    if (vt.is(Tree.Kind.ASSIGNMENT)){
                        AssignmentExpressionTree aet = (AssignmentExpressionTree)vt;
                        if (aet.variable().toString().contains("conditions") && !((LiteralTreeImpl)aet.expression()).value().replace(" ","").equals("\"1=1\"")){
                            context.reportIssue(this,((AssignmentExpressionTree)vt).variable(),"在业务层类中不允许出现conditions拼写 SQL 的语句。");
                        }
                    } else if (vt.is(Tree.Kind.VARIABLE) && ((VariableTree)vt).simpleName().toString().contains("conditions")){
                        context.reportIssue(this,((VariableTree)vt).simpleName(),"在业务层类中不允许出现conditions拼写 SQL 的语句。");
                    }
                }
            }
        }
        super.visitMethod(tree);
    }
}