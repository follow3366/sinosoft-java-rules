package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class PageRecordNamingCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/PageRecordNamingCheck.java")
                .withCheck(new PageRecordNamingRule())
                .verifyIssues();
    }
}
