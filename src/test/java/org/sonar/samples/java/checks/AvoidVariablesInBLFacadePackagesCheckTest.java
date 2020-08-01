package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;
import org.sonar.plugins.java.api.JavaFileScanner;

public class AvoidVariablesInBLFacadePackagesCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/AvoidVariablesInBLFacadePackagesCheck.java")
                .withCheck(new AvoidVariablesInBLFacadePackages())
                .verifyIssues();
    }
}
