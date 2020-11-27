package org.sonar.samples.java.checks;


import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

/**
 * Session中禁止存放业务数据或单个 String 对象。
 */
@Rule(key = "ForbiddenSaveBusinessDataToSession")
public class ForbiddenSaveBusinessDataToSession extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
        public void visitMethod(MethodTree tree) {
        if (null != tree &&  null != tree.cfg()) { // 解决扫描 interface 时，如果没有方法块，则会报空指针
            List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
            // 再来循环方法 进行编码规则校验
            for (ControlFlowGraph.Block tcb : tcbs) {
                List<Tree> vtl = tcb.elements();
                for (Tree vt : vtl) {
                    // 如果碰到 HttpServletRequest.setAttribute() 方法，则校验一下括号内参数类型是否为 PageRecord
                    if (vt.is(Tree.Kind.METHOD_INVOCATION)) {
                        ExpressionTree mset = ((MethodInvocationTree) vt).methodSelect();
                        Arguments arguments = ((MethodInvocationTree) vt).arguments();
                        // 判断 MEMBER_SELECT 类型，并且通过 HttpSession 调用 setAttribute() 方法
                        if (mset.is(Tree.Kind.MEMBER_SELECT)
                                && ((MemberSelectExpressionTree) mset).expression().symbolType().fullyQualifiedName().equals("HttpSession")
                                && "setAttribute".equals(((MemberSelectExpressionTree) mset).identifier().toString())
                                && "java.lang.String".equals(arguments.get(1).symbolType().fullyQualifiedName())) {
                            System.out.println("发现问题。");
                            context.reportIssue(this, mset, "Session中禁止存放业务数据或单个 String 对象。");
                        }
                    }
                }
            }
        }
    }
}
