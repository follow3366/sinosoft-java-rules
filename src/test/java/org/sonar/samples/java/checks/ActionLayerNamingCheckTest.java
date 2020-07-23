package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class ActionLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier().onFile("src/test/files/ActionLayerNamingCheck.java")
                .withCheck(new ActionLayerNamingRule())
                .verifyIssues();
    }
}
