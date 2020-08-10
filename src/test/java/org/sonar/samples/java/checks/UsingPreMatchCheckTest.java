package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class UsingPreMatchCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/UsingPreMatchCheck.java")
                .withCheck(new UsingPreMatch())
                .verifyIssues();
    }
}
