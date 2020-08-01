package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RequestSetAttributeCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/RequestSetAttributeChecks.java")
                .withCheck(new RequestSetAttributeCheck())
                .verifyIssues();
    }
}
