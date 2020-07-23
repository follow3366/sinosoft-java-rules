package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class FacadeLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier().onFile("src/test/files/FacadeLayerNamingCheck.java")
                .withCheck(new FacadeLayerNamingRule())
                .verifyIssues();
    }
}
