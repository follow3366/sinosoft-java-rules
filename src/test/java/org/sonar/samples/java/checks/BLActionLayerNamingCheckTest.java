package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class BLActionLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/BLActionLayerNamingCheck.java")
                .withCheck(new BLActionLayerNamingRule())
                .verifyIssues();
    }
}
