package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

/**
 * 所有的 Dto 类的属性必须是 JAVA 对象，不允许使用原始数据类型。
 */
@Rule(key = "DtoClassPropertiesShouldBeJavaObject")
public class DtoClassPropertiesShouldBeJavaObject extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private Boolean isDto = Boolean.FALSE;
    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        isDto = PackageUtils.packageName(tree.packageDeclaration(),".").contains(".dto.");
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (isDto){
            List<Tree> cts =  tree.members();
            for (Tree ct : cts){
                if (ct.is(Tree.Kind.VARIABLE) && ((VariableTree)ct).type().symbolType().isPrimitive()){
                    context.reportIssue(this,((VariableTree)ct).type(),"所有的 Dto 类的属性必须是 JAVA 对象，不允许使用原始数据类型。");
                }
            }
        }
        super.visitClass(tree);
    }
}