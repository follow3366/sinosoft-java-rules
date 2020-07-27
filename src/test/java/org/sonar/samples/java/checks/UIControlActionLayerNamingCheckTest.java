package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class UIControlActionLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier().onFile("src/test/files/UIControlActionLayerNamingCheck.java")
                .withCheck(new UIControlActionLayerNamingRule())
                .verifyIssues();
    }
}
