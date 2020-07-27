package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DTOLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/DTOLayerNamingCheck.java")
                .withCheck(new DTOLayerNamingRule())
                .verifyIssues();
    }
}
