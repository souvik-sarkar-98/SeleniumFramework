package app.test.nabarun;


import drivers.testng.TestDriver;
import framework.automation.selenium.core.TestEngine;

import org.testng.annotations.BeforeTest;

public class REGISTRATION_TEST_001 extends TestDriver{
 
  @BeforeTest
  public void setUpTest() throws Exception {
	  TestEngine.setPropertyFile("test-resources/nabarun/property.xml");
	  TestEngine.setProperty("BrowserName", "Chrome");
  }

}
