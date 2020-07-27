package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

@Rule(key = "PageRecordNamingRule")
public class PageRecordNamingRule extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context ;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
//        System.out.println(PrinterVisitor.print(context.getTree()));
    }

    @Override
    public void visitMethod(MethodTree tree) {
//        List<Tree> vtl = tree.cfg().entryBlock().elements();
        Boolean isVariable;
        List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
        for (ControlFlowGraph.Block tcb : tcbs){
            List<Tree> vtl = tcb.elements();
            for (Tree vt : vtl){
                isVariable = ("Variable".toUpperCase()).equals(vt.kind().name());
                if (isVariable && "PageRecord".equals(((VariableTree) vt).type().toString())) {
                    if (!"pageRecord".equals(((VariableTree) vt).simpleName().toString())) {
                        context.reportIssue(this,((VariableTree) vt).simpleName(),"PageRecord 类型的变量命名必须为 pageRecord！");
                    }
                }
            }
        }
        super.visitMethod(tree);
    }
}
