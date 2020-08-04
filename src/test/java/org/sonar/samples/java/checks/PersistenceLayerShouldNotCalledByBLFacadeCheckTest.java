package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class PersistenceLayerShouldNotCalledByBLFacadeCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/PersistenceLayerShouldNotCalledByBLFacadeCheck.java")
                .withCheck(new PersistenceLayerShouldNotCalledByBLFacade())
                .verifyIssues();
    }
}
