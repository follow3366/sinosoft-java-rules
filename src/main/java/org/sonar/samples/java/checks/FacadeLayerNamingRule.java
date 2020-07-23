package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

import java.util.regex.Pattern;

/**
 * 检测规则：com.sinosoft.claim.ui.control.facade 包下的类全部都要以“Facade”结尾,以 UI 开头。
 */

@Rule(key="FacadeLayerNamingRule")
public class FacadeLayerNamingRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;
    private Boolean isFacade = Boolean.FALSE;
    private static final String DEFAULT_FORMAT = "^UI[A-Z][a-zA-Z0-9]*Facade$";

    @RuleProperty(
            description = "com.sinosoft.claim.ui.control.facade 包下的类全部都要以“Facade”结尾,以 UI 开头",
            defaultValue = DEFAULT_FORMAT)
    public String format = DEFAULT_FORMAT;
    private Pattern pattern = null;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        if (pattern == null) {
            pattern = Pattern.compile(format, Pattern.DOTALL);
        }
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        if (tree.packageDeclaration() != null) {
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");//com.sinosoft.claim.ui.control.facade
            isFacade = name.equals("com.sinosoft.claim.ui.control.facade");//true
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        String className = tree.simpleName().toString();
        if (isFacade && !pattern.matcher(className).matches()){
            context.reportIssue(this, tree.simpleName(), "重命名此类名以匹配 '" + format + "' 正则表达式");
        }
    }
}