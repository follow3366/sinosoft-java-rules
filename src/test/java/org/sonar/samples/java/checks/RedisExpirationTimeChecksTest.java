package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RedisExpirationTimeChecksTest {

    @Test
    public void test(){
        JavaCheckVerifier.verify("src/test/files/RedisExpirationTimeChecks.java",new RedisExpirationTimeChecks());
    }
}
