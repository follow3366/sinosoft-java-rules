/**
 * author:zhl
 * date:20200405
 * 规则描述：
 * dao层和service层不应捕获异常，应在controler层捕获
 * 因此，检测以dao或service结尾的类中，是否有catch语句，有则抛出异常
 */

package org.sonar.samples.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

@Rule(key = "DaoAndServiceCatchChecks")
public class DaoAndServiceCatchChecks extends BaseTreeVisitor implements JavaFileScanner {
	private JavaFileScannerContext context;

	@Override
	public void scanFile(JavaFileScannerContext context) {
		this.context = context;

		// The call to the scan method on the root of the tree triggers the visit of the
		// AST by this visitor
		scan(context.getTree());

		// For debugging purpose, you can print out the entire AST of the analyzed file
//	    System.out.println(PrinterVisitor.print(context.getTree()));
	}

	/**
	 * Overriding the visitor method to implement the logic of the rule.
	 * 
	 * @param tree AST of the visited method.
	 */
	@Override
	public void visitClass(ClassTree tree) {
//		System.out.println("类名:" + tree.symbol().name());
		if (tree.symbol().name().endsWith("Dao") || tree.symbol().name().endsWith("Service")) {
			List<Tree> ct = tree.members();
			for (Tree tempTree : ct) {
				if (tempTree.kind().name().equals("METHOD")) {
					if(((MethodTree)tempTree).throwsToken().toString() != null){
						for ( Tree catchTree :((MethodTree)tempTree).cfg().entryBlock().elements()) {
							if(catchTree.kind().name().equals("TRY_STATEMENT")){
//								在含有throws的方法中，只要找到catch语句，则报问题
//								System.out.println("检测到try语句，需要报问题");
								context.reportIssue(this, tempTree, "Exception should catch in controller");
							}
							
						}
					}
				}
			}
		}

		// The call to the super implementation allows to continue the visit of the AST.
		// Be careful to always call this method to visit every node of the tree.
		super.visitClass(tree);

		// All the code located after the call to the overridden method is executed when
		// leaving the node

	}
}
