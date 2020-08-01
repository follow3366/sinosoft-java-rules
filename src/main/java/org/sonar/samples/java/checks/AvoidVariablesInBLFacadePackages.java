package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;


/**
 * com.sinosoft.claim.bl.facade 包下类不能定义任何变量。
 */
@Rule(key = "AvoidVariablesInBLFacadePackages")
public class AvoidVariablesInBLFacadePackages extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    Boolean isFacade;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        if (tree.packageDeclaration() != null) {
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");
            isFacade = name.equals("com.sinosoft.claim.bl.facade");//true
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (isFacade) {
            for (Tree member : tree.members()) {
                if (member.is(Tree.Kind.VARIABLE)) {
                    if (!"Logger".equals(((VariableTree) member).type().toString())) {
                        context.reportIssue(this, ((VariableTree) member).type(), "com.sinosoft.claim.bl.facade 包下类不能定义任何变量。");
                    }
                }
            }
        }
    }
}
