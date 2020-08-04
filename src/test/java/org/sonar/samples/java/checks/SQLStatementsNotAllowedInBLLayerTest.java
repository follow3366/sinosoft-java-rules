package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class SQLStatementsNotAllowedInBLLayerTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/SQLStatementsNotAllowedInBLLayerCheck.java")
                .withCheck(new SQLStatementsNotAllowedInBLLayer())
                .verifyIssues();
    }
}
