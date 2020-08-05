package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DTOCustomShouldImplementsSerializableCheckTest {
    @Test
    public void test(){
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/DTOCustomShouldImplementsSerializableCheck.java")
                .withCheck(new DTOCustomShouldImplementsSerializable())
                .verifyIssues();
    }
}
