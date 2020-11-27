package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.ast.parser.ArgumentListTreeImpl;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.cfg.ControlFlowGraph;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 在查询和翻页查询的方法中，必须使用
 * request.setAttribute("fm", new AbstractForm(pageRecord));
 * 把PageRecord 对象存放到 javax.servlet.http.HttpServletRequest 对象中。
 */
@Rule(key = "RequestSetAttributeCheck")
public class RequestSetAttributeCheck extends BaseTreeVisitor implements JavaFileScanner {
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
//        System.out.println(PrinterVisitor.print(context.getTree()));
    }
    // 先判断是否翻页查询方法
    // 判断一个翻页查询方法吗：
    //  1、是 UIxxxxFacade 类
    //  2、类里面有 PageRecord 对象
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
        // 如果是WEB层，则进入，不是则退出
        if (isWebLayer) {
            Boolean isPagingQuery = false;
            List<? extends ControlFlowGraph.Block> tcbs = tree.cfg().blocks();
            // 判断包含 PageRecord 类型的变量，则为分页查询的方法
            for (ControlFlowGraph.Block tcb : tcbs) {
                List<Tree> vtl = tcb.elements();
                for (Tree vt : vtl) {
                    // 判断为 Variable 类型 并且 变量类型为 PageRecord 类型
                    if (vt.is(Tree.Kind.VARIABLE) && ("PageRecord".equals(((VariableTree) vt).type().toString()))) {

                        // 再来循环方法 进行编码规则校验
                        for (ControlFlowGraph.Block tcbi : tcbs) {
                            List<Tree> vtli = tcbi.elements();
                            for (Tree vti : vtli) {
                                // 如果碰到 HttpServletRequest.setAttribute() 方法，则校验一下括号内参数类型是否为 PageRecord
                                if (vti.is(Tree.Kind.METHOD_INVOCATION)) {
                                    ExpressionTree mset = ((MethodInvocationTree) vti).methodSelect();
                                    Arguments arguments =((MethodInvocationTree) vti).arguments();

                                    // 判断有 setAttribute() 方法
                                    if (((MemberSelectExpressionTree) mset).expression().symbolType().fullyQualifiedName().equals("HttpServletRequest")
                                            && "setAttribute".equals(((MemberSelectExpressionTree) mset).identifier().toString())) {
                                        for (ExpressionTree argument : arguments){
                                            if ("AbstractForm".equals(argument.symbolType().fullyQualifiedName())){
                                                NewClassTree nargs = (NewClassTree)argument;
                                                for (ExpressionTree narg :nargs.arguments()){
                                                    if (!"PageRecord".equals(narg.symbolType().toString())){
                                                        context.reportIssue(this,((MemberSelectExpressionTree) mset).expression(),"在查询和翻页查询的方法中，必须使用 request.setAttribute(\"fm\", new AbstractForm(pageRecord));把PageRecord 对象存放到 javax.servlet.http.HttpServletRequest 对象中。");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        isWebLayer = false;
    }
}
