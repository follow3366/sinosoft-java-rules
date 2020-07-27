package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class BLFacadeLayerNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/BLFacadeLayerNamingCheck.java")
                .withCheck(new BLFacadeLayerNamingRule())
                .verifyIssues();
    }
}
