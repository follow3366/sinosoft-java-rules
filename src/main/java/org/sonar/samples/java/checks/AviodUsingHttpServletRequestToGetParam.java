package org.sonar.samples.java.checks;


import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 不允许使用HttpServletRequest 对象的方法获取参数值。
 */
@Rule(key = "AviodUsingHttpServletRequestToGetParam")
public class AviodUsingHttpServletRequestToGetParam extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private static String format = "UI[A-Z][a-zA-Z0-9]*Facade$";
    private Pattern pattern = null;
    private Boolean isWebLayer = Boolean.FALSE;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        if (pattern == null) {
            pattern = Pattern.compile(format, Pattern.DOTALL);
        }
        this.context = context;
        scan(context.getTree());
    }

    //  是 UIxxxxFacade 类
    @Override
    public void visitClass(ClassTree tree) {

        String className = tree.simpleName().toString();
        // 如果类名匹配
        if (pattern.matcher(className).matches()){
            // 循环 ClassTree
            isWebLayer = true;
        }
        super.visitClass(tree);
    }

    @Override
    public void visitMethod(MethodTree tree) {
        // 如果是WEB层，则进入
        if (isWebLayer && null != tree &&  null != tree.cfg()) {
            List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
            // 再来循环方法 进行编码规则校验
            for (ControlFlowGraph.Block tcb : tcbs) {
                List<Tree> vtl = tcb.elements();
                for (Tree vt : vtl) {
                    // 如果碰到 HttpServletRequest.getAttribute() 方法，则报问题
                    if (vt.is(Tree.Kind.METHOD_INVOCATION)) {
                        ExpressionTree mset = ((MethodInvocationTree) vt).methodSelect();
                        // 判断有 getAttribute() 方法
                        if (mset.is(Tree.Kind.MEMBER_SELECT) && ((MemberSelectExpressionTree) mset).expression().symbolType().fullyQualifiedName().equals("HttpServletRequest")
                                && "getAttribute".equals(((MemberSelectExpressionTree) mset).identifier().toString())) {
                            context.reportIssue(this,((MemberSelectExpressionTree) mset).expression(),"不允许使用HttpServletRequest 对象的方法获取参数值。");
                        }
                    }
                }
            }
        }
        isWebLayer = Boolean.FALSE;
    }
}
