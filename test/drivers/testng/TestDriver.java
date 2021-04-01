package drivers.testng;

import org.testng.annotations.Test;

import framework.automation.selenium.core.TestEngine;

import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class TestDriver {
	
	
	private TestEngine engine;

	@BeforeTest
	public void setUpTest() throws Exception {

	}

	@Test(priority = 0)
	public final void startTest() throws Exception {
		this.engine=new TestEngine(this.getClass().getSimpleName());
		this.engine.start();
	}

	@Test(priority = 1, dataProvider = "fetchKeywords",dependsOnMethods = {"startTest"})
	public final void executeKeyword(String keyword) throws Exception {
		this.engine.execute(keyword);

	}

	@Test(priority = 2,dependsOnMethods = {"startTest"})
	public final void endTest() {
		this.engine.stop();
	}

	@DataProvider
	public final Object[] fetchKeywords() throws Exception {
		return this.engine.getKeywords();
	}
	
	@AfterTest
	public void generateTestReport() throws Exception {
		this.engine.generateReport();
	}

}
