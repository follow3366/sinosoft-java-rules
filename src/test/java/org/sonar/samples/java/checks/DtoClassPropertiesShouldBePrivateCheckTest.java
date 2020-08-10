package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DtoClassPropertiesShouldBePrivateCheckTest {
    @Test
    public void  test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/DtoClassPropertiesShouldBePrivateCheck.java")
                .withCheck(new DtoClassPropertiesShouldBePrivate())
                .verifyIssues();
    }
}
