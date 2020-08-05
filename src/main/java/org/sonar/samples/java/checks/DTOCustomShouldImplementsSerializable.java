package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

/**
 * com.sinosoft.claim.dto.custom 包下的类必须实现java.io.Serializable 接口。
 */
@Rule(key = "DTOCustomShouldImplementsSerializable")
public class DTOCustomShouldImplementsSerializable extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    Boolean isDTOCustom = Boolean.FALSE;


    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    // 判断是否是“com.sinosoft.claim.dto.custom”这个包
    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        if (PackageUtils.packageName(tree.packageDeclaration(),".").equals("com.sinosoft.claim.dto.custom")){
            isDTOCustom = Boolean.TRUE;
        }
        super.visitCompilationUnit(tree);
    }

    // 判断是否 实现了Serializable接口，没实现就报问题
    @Override
    public void visitClass(ClassTree tree) {
        if (isDTOCustom && !tree.symbol().type().isSubtypeOf("java.io.Serializable")){
            context.reportIssue(this,tree.simpleName(),"com.sinosoft.claim.dto.custom 包下的类必须实现java.io.Serializable 接口。");
        }
        super.visitClass(tree);
    }
}
