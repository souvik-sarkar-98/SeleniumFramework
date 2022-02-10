package framework.automation.selenium.core.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import framework.automation.selenium.core.config.PropertyCache;

/**
 * @author Souvik Sarkar
 * @createdOn 31-Mar-2021
 * @purpose
 */
public class ReportHelper {

	private WebDriver driver;
	private List<TestActionListener> listeners = new ArrayList<TestActionListener>();


	public ReportHelper(WebDriver driver) {
		this.driver = driver;
	}
	
	public void addTestActionListener(TestActionListener tl) {
		listeners.add(tl);
	}

	public void captureScreenshot()  {

		TakesScreenshot scrShot = ((TakesScreenshot) this.driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(SrcFile, new File(PropertyCache.getProperty("ScreenshotPath").toString()+"/"+PropertyCache.getProperty("TestName")+"/"+System.currentTimeMillis()+".png"));
		} catch (IOException e) {
		}
	}
	

	public void testCasePassed(String testCaseName, String message) {
		for (TestActionListener el : listeners) {
			el.passTestCase(testCaseName,message);
		}
	}
	
	public void testCaseFailed(String testCaseName, String message,Exception e) {
		for (TestActionListener el : listeners) {
			el.failTestCase(testCaseName,message,e);
		}
	}

	
	public void saveScreenshot(String extension) {
		String fileName="";
		//
		for (TestActionListener el : listeners) {
			el.uploadEvidence(fileName);
		}
	}

}
