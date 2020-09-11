package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

import java.util.regex.Pattern;

/**
 * 持久层的类必须以 DB 开头；
 */
@Rule(key = "PersistenceLayerNamingRule")
public class PersistenceLayerNamingRule extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    Boolean isDBLayer = Boolean.FALSE;
    public String format = "^DB[A-Z][a-zA-Z0-9]*";
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
    public void visitClass(ClassTree tree) {
        if (isDBLayer && (null != tree.simpleName()) && !pattern.matcher(tree.simpleName().toString()).matches()){
            context.reportIssue(this,tree.simpleName(),"持久层的类名必须以 DB 开头");
        }
        super.visitClass(tree);
    }
}
