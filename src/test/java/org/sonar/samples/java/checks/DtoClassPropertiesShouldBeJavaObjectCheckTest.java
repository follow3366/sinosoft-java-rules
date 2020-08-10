package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DtoClassPropertiesShouldBeJavaObjectCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/DtoClassPropertiesShouldBeJavaObjectCheck.java")
                .withCheck(new DtoClassPropertiesShouldBeJavaObject())
                .verifyIssues();
    }
}
