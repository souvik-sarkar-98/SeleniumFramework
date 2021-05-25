package app.test.nabarun;


import framework.automation.selenium.core.TestEngine;

import org.testng.annotations.BeforeTest;

import drivers.testng.TestDriver;

public class REGISTRATION_TEST_001 extends TestDriver{
 
  @BeforeTest
  public void setUpTest() throws Exception {
	  TestEngine.setPropertyFile("test-resources/nabarun/property.xml");
	  TestEngine.setProperty("BrowserName", "Chrome");
  }

}
