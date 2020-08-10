package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.regex.Pattern;

/**
 * like子句尽量前端匹配，前端匹配可以使用索引，其他不能使用索引。
 */
@Rule(key = "UsingPreMatch")
public class UsingPreMatch extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private Boolean isDBLayer = Boolean.FALSE;
    private static final String format = "LIKE'%";
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
//            if(tree.is(Tree.Kind.STRING_LITERAL) ){
//                System.out.println(tree.value().replace(" ","").toUpperCase());
                context.reportIssue(this,tree,"like子句尽量前端匹配，前端匹配可以使用索引，其他不能使用索引。");
            }
        }
        super.visitLiteral(tree);
    }
}
