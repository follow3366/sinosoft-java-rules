package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class HttpConnectionTimeoutCheckTest {

    @Test
    public void test() {
//        JavaCheckVerifier.verify("src/test/files/HttpConnectionTimeoutChecks.java", new HttpConnectionTimeoutChecks());
        JavaCheckVerifier.newVerifier().onFile("src/test/files/HttpConnectionTimeoutChecks.java")
                .withCheck(new HttpConnectionTimeoutChecks())
                .verifyIssues();
    }
}
