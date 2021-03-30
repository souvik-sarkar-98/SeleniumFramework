package spicejet;

import org.testng.annotations.BeforeTest;

import drivers.testng.TestDriver;
import framework.automation.selenium.core.TestEngine;

public class NewTest extends TestDriver {
	@BeforeTest
	public void setUpTest() throws Exception {
		TestEngine.setPropertyFile("test-resources/property.xml");
		//TestEngine.setProperty("BrowserName", "Chrome");
	}
}
