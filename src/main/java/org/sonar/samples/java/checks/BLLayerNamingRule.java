package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

/**
 * 检测规则：业务层的类必须以 BL 开头。
 */

@Rule(key="BLLayerNamingRule")
public class BLLayerNamingRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;
    private Boolean isBL = Boolean.FALSE;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        isBL = Boolean.FALSE;
        if (null != tree.packageDeclaration()) {
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");
            isBL = name.contains(".bl.");//true
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (isBL && (null != tree.simpleName()) && !tree.simpleName().toString().startsWith("BL")){
            context.reportIssue(this, tree.simpleName(), "业务层的类必须以 BL 开头。");
        }
        super.visitClass(tree);
    }
}