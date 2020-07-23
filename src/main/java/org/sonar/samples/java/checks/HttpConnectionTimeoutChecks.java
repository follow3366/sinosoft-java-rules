package org.sonar.samples.java.checks;

/**
 * author:zhl
 * date:20200404
 * 规则描述：
 * 所有的连接类型，均需要设置超时时间
 */
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "HttpConnTimeoutCheck")
public class HttpConnectionTimeoutChecks extends BaseTreeVisitor implements JavaFileScanner {
    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
//        System.out.println(PrinterVisitor.print(context.getTree()));

        // For debugging purpose, you can print out the entire AST of the analyzed file
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
        List<Tree> vtl = tree.cfg().entryBlock().elements();
        Boolean isVariable = false;
        for (Tree vt : vtl) {
            // 判断是否为 Variable 类型
            isVariable = vt.kind().name().equals("Variable".toUpperCase());
            // 判断变量类型是否为 HttpConnectionManagerParams、HttpURLConnection 类型
            if (isVariable && ("HttpConnectionManagerParams".equals(((VariableTree) vt).type().toString())
                    || "HttpURLConnection".equals(((VariableTree) vt).type().toString()))) {

                for (Tree mt : vtl) {
                    if (mt.kind().name().equals("METHOD_INVOCATION".toUpperCase())) {
                        // 获取方法名
//                        System.out.println("MethodInvocationTree: "+((MemberSelectExpressionTree)(((MethodInvocationTree) mt).methodSelect())).expression().toString());
//                        System.out.println("Variable: "+((VariableTree) vt).simpleName().toString());
                        ExpressionTree mset = ((MethodInvocationTree) mt).methodSelect();
                        // 获取变量名用： ((VariableTree) vt).simpleName()
                        // 获取 MethodInvocation 名称： ((MemberSelectExpressionTree)(((MethodInvocationTree) mt).methodSelect())).expression().toString()
                        if (((MemberSelectExpressionTree) mset).expression().toString().equals(((VariableTree) vt).simpleName().toString())
                                && "setConnectionTimeout".equals(((MemberSelectExpressionTree) mset).identifier().toString())) {
                            setTimeout = true;
                        }
                    }
                }
                if (!setTimeout) {
                    context.reportIssue(this, vt, "Connection Variable's connection timeout not set");
                }
            }
        }
        super.visitMethod(tree);
    }
}
