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
 * 检测规则：com.sinosoft.claim.dto.custom 包下类必须以 Dto 结尾。
 */

@Rule(key="DTOLayerNamingRule")
public class DTOLayerNamingRule extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;
    private Boolean isDto = Boolean.FALSE;
    private static final String DEFAULT_FORMAT = "^[A-Z][a-zA-Z0-9]*Dto$";

    @RuleProperty(
            description = "com.sinosoft.claim.dto.custom 包下类必须以 Dto 结尾。",
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
            String name = PackageUtils.packageName(tree.packageDeclaration(), ".");
            isDto = name.equals("com.sinosoft.claim.dto.custom");//true
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        String className = tree.simpleName().toString();
        if (isDto && !pattern.matcher(className).matches()){
            context.reportIssue(this, tree.simpleName(), "重命名此类名以匹配 '" + format + "' 正则表达式");
        }
        super.visitClass(tree);
    }
}