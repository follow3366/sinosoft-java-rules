package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DaoAndServiceCatchChecksTest {

	  @Test
	  public void test() {
		  JavaCheckVerifier.verify("src/test/files/DaoAndServiceCatchChecks.java", new DaoAndServiceCatchChecks());
	  } 
}
