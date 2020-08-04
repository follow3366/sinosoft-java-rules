package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class PersistenceLayerNamingRuleCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/PersistenceLayerNamingRuleCheck.java")
                .withCheck(new PersistenceLayerNamingRule())
                .verifyIssues();
    }
}
