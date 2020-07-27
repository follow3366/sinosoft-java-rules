package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class UIControlFacadeLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier().onFile("src/test/files/UIControlFacadeLayerNamingCheck.java")
                .withCheck(new UIControlFacadeLayerNamingRule())
                .verifyIssues();
    }
}
