package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

/**
 * Dto类属性需用private修饰并初始化为null。
 */
@Rule(key = "DtoClassPropertiesShouldBePrivate")
public class DtoClassPropertiesShouldBePrivate extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    private Boolean isDto = Boolean.FALSE;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
//        System.out.println(PrinterVisitor.print(context.getTree()));
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
                if (ct.is(Tree.Kind.VARIABLE) ){
                    List<ModifierKeywordTree> lmt = ((VariableTree)ct).modifiers().modifiers();
                    ExpressionTree expressionTree = ((VariableTree) ct).initializer();
                    for (ModifierKeywordTree lm :lmt){
                        if (!"PRIVATE".equals(lm.modifier().name())){
                            context.reportIssue(this,ct,"Dto类属性需用private修饰并初始化为null。");
                        } else if (expressionTree != null){
                            context.reportIssue(this,ct,"Dto类属性需用private修饰并初始化为null。");
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

}
