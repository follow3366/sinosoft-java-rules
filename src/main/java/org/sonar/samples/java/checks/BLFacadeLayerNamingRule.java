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
 * 检测规则：com.sinosoft.claim.bl.facade 包下的类全部都要以“Facade”结尾。
 */

@Rule(key="BLFacadeLayerNamingRule")
public class BLFacadeLayerNamingRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;
    private Boolean isFacade = Boolean.FALSE;
    private static final String DEFAULT_FORMAT = "^[A-Z][a-zA-Z0-9]*Facade$";

    @RuleProperty(
            description = "com.sinosoft.claim.bl.facade 包下类必须以 Facade 结尾（Base 类除外）。",
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
        isFacade = Boolean.FALSE;
        if (null != tree.packageDeclaration()) {
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");//com.sinosoft.claim.ui.control.facade
            isFacade = name.equals("com.sinosoft.claim.bl.facade");//true
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (null != tree.simpleName()) {
            String className = tree.simpleName().toString();
            int len = className.length();
            if (len >= 4 && !"Base".equals(className.substring(len - 5, len)) && isFacade && !pattern.matcher(className).matches()) {
                context.reportIssue(this, tree.simpleName(), "重命名此类名以匹配 '" + format + "' 正则表达式");
            }
            super.visitClass(tree);
        }
    }
}