package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.regex.Pattern;

/**
 * 代码中select *中的*号要用具体字段名替换。
 */
@Rule(key = "AvoidUsingWildcardInSelectStatement")
public class AvoidUsingWildcardInSelectStatement extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private Boolean isDBLayer = Boolean.FALSE;
    private static final String format = "SELECT(\\*|[A-Za-z0-9_]+\\.\\*)";
    private Pattern pattern = null;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        pattern = Pattern.compile(format, Pattern.DOTALL);
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {

        isDBLayer= PackageUtils.packageName(tree.packageDeclaration(), ".").contains(".dtofactory.");
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitLiteral(LiteralTree tree) {
        if (isDBLayer){
            if(tree.is(Tree.Kind.STRING_LITERAL) && pattern.matcher(tree.value().replace(" ","").toUpperCase()).find()){
                context.reportIssue(this,tree,"代码中select *中的*号要用具体字段名替换。");
            }
        }
        super.visitLiteral(tree);
    }
}
