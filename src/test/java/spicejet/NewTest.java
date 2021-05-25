package spicejet;

import org.testng.annotations.BeforeTest;

import drivers.testng.TestDriver;
import framework.automation.selenium.core.TestEngine;

public class NewTest extends TestDriver {
	@BeforeTest
	public void setUpTest() throws Exception {
		System.out.println(NewTest.class.getResource("property.xml"));
		TestEngine.setPropertyFile("property.xml");
		TestEngine.setProperty("BrowserName", "Chrome");
	}
}
