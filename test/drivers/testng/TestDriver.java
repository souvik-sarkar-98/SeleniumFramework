package drivers.testng;

import org.testng.annotations.Test;

import framework.automation.selenium.core.TestEngine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeTest;

public class TestDriver {
	
	
	private TestEngine engine;

	@BeforeTest
	public void setUpTest() throws Exception {
		TestEngine.setPropertyFile("test-resources/spicejet/login/property.xml");
		//TestEngine.setProperty("BrowserName", "Chrome");
	}

	@Test(priority = 0)
	public final void startTest() throws Exception {
		this.engine=new TestEngine(this.getClass().getSimpleName());
		this.engine.start();
	}

	@Test(priority = 1, dataProvider = "fetchKeywords")
	public final void executeKeyword(String keyword) throws Exception {
		this.engine.execute(keyword);

	}

	@Test(priority = 2)
	public final void endTest() {
		this.engine.stop();
	}

	@DataProvider
	public final Object[] fetchKeywords() throws Exception {
		return this.engine.getKeywords();
	}

}
