package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.java.model.PackageUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.regex.Pattern;

/**
 * BLfacade 层不能调用持久层的类
 */
@Rule(key = "PersistenceLayerShouldNotCalledByBLFacade")
public class PersistenceLayerShouldNotCalledByBLFacade extends BaseTreeVisitor implements JavaFileScanner {
    JavaFileScannerContext context;
    Boolean isBLFacadeLayer = Boolean.FALSE;
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

        isBLFacadeLayer= PackageUtils.packageName(tree.packageDeclaration(), ".").contains(".bl.facade");
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitNewClass(NewClassTree tree) {
        if (isBLFacadeLayer){
            TypeTree tt = tree.identifier();
            if (!tt.toString().equals("DBManager") && pattern.matcher(tt.toString()).matches()){
                context.reportIssue(this,tt,"BLfacade 层不能调用持久层的类");
            }
        }
        super.visitNewClass(tree);
    }
}
