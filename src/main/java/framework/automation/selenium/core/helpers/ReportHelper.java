package framework.automation.selenium.core.helpers;

import java.io.File;
import java.io.IOException;

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

	public ReportHelper(WebDriver driver) {
		this.driver = driver;
	}

	public void captureScreenshot()  {

		TakesScreenshot scrShot = ((TakesScreenshot) this.driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(SrcFile, new File(PropertyCache.getProperty("ScreenshotPath").toString()+"/"+PropertyCache.getProperty("TestName")+"/"+System.currentTimeMillis()+".png"));
		} catch (IOException e) {
		}
	}

	/**
	 * @purpose
	 * @date 31-Mar-2021
	 */
	public void prepareReport() {

	}

}
