package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class ForbiddenSaveBusinessDataToSessionCheckTest {
    @Test
    public void test() {
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/ForbiddenSaveBusinessDataToSessionCheck.java")
                .withCheck(new ForbiddenSaveBusinessDataToSession())
                .verifyIssues();
    }
}
