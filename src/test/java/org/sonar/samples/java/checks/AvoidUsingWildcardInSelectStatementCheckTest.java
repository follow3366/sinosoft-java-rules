package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class AvoidUsingWildcardInSelectStatementCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/AvoidUsingWildcardInSelectStatementCheck.java")
                .withCheck(new AvoidUsingWildcardInSelectStatement())
                .verifyIssues();
    }
}
