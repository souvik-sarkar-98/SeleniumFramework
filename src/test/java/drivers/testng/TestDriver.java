package drivers.testng;

import org.testng.annotations.Test;

import framework.automation.selenium.core.TestEngine;
import framework.automation.selenium.core.helpers.TestActionListener;

import java.nio.file.Paths;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class TestDriver {

	private TestEngine engine;

	@BeforeTest
	public void setUpTest() throws Exception {
		try {
			TestEngine.setPropertyFile(Paths.get(this.getClass().getResource("property.xml").toURI()).toString());
		} catch (NullPointerException e) {
			throw new RuntimeException("No 'property.xml' file found at '"+this.getClass().getPackage().getName().replace(".", "\\")+"' under test resources folder. To use custom path override setUpTest() method with TestNG @BeforeTest annotation and set property file using 'TestEngine.setPropertyFile(filePath)'.");
		}
	}

	@Test(priority = 0)
	public final void startTest(Class<?> class1) throws Exception {
		this.engine = new TestEngine(class1);
		this.engine.run(new TestActionListener() {
			
			@Override
			public void uploadEvidence(String fileName) {
				System.err.println("uploading file ");
				
			}
			
			@Override
			public void passTestCase(String testCaseName, String message) {
				System.err.println(testCaseName+" is "+message);
				
			}
			
			@Override
			public void failTestCase(String testCaseName, String message, Exception e) {
				System.err.println(testCaseName+" is "+message+" due to "+ e);
				
			}
		});
	}


	

	@AfterTest
	public void endTest() throws Exception {
		this.engine.stop();
	}
	
	

}
