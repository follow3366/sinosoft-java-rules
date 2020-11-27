package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

import static org.sonar.plugins.java.api.tree.Tree.Kind.*;

@Rule(key = "RedisExpirationTimeChecks")
public class RedisExpirationTimeChecks extends BaseTreeVisitor implements JavaFileScanner {
    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    /**
     * Overriding the visitor method to implement the logic of the rule.
     *
     * @param tree AST of the visited method.
     */
//主方法
    @Override
    public void visitMethod(MethodTree tree) {
        Boolean setTimeout = false;
        List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
        for (ControlFlowGraph.Block tcb : tcbs) {
            List<Tree> vtl = tcb.elements();
            Boolean isVariable = false;
            String isname ;
            for (Tree vt : vtl) {
                // 判断是否为 Variable 类型
                isVariable = vt.is(VARIABLE);
                // 判断变量类型是否为 Jedis 类型
                if (isVariable && ("Jedis".equals(((VariableTree) vt).type().toString()))) {
                    for (Tree tst : vtl) {
                        if (tst.is(TRY_STATEMENT)){
                            String mtname = tst.kind().name();
                            List<StatementTree> les = ((TryStatementTree)tst).block().body();
                            for (Tree le : les){
                                // 判断 le 是否为 ExpressionTree
                                if (le.is(EXPRESSION_STATEMENT) && ((ExpressionStatementTree)le).expression().is(METHOD_INVOCATION)) {
                                    ExpressionTree mset = ((MethodInvocationTree)((ExpressionStatementTree) le).expression()).methodSelect();
                                    if (mset.is(MEMBER_SELECT)
                                            &&((MemberSelectExpressionTree) mset).expression().toString().equals(((VariableTree) vt).simpleName().toString())
                                            && "expire".equals(((MemberSelectExpressionTree) mset).identifier().toString())) {
                                        setTimeout = true;
                                    }
                                }
                            }
                        }
                        if (tst.is(METHOD_INVOCATION)) {
                            ExpressionTree mset = ((MethodInvocationTree) tst).methodSelect();
                            if (mset.is(MEMBER_SELECT)
                                    && ((MemberSelectExpressionTree) mset).expression().toString().equals(((VariableTree) vt).simpleName().toString())
                                    && "expire".equals(((MemberSelectExpressionTree) mset).identifier().toString())) {
                                setTimeout = true;
                            }
                        }
                    }
                    if (!setTimeout) {
                        context.reportIssue(this, vt, "Jedis should set the expiration time");
                        setTimeout = false;
                    }
                }
            }
            super.visitMethod(tree);
        }
    }

}
