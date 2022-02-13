package framework.automation.selenium.core.helpers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import framework.automation.selenium.core.TestActionListener;
import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.utils.TestReportUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 31-Mar-2021
 * @purpose
 */
public class ReportHelper {

	private WebDriver driver;
	private List<TestActionListener> listeners = new ArrayList<TestActionListener>();
	private TestReportUtil reportUtil;
	private final String screenshotFolder=PropertyCache.getProperty("ScreenshotPath").toString() + "/"
			+ PropertyCache.getProperty("TestName");

	public ReportHelper(WebDriver driver) {
		this.driver = driver;
		this.reportUtil = new TestReportUtil();
		new File(screenshotFolder+"/temp").mkdirs();
	}

	public void addTestActionListener(TestActionListener tl) {
		listeners.add(tl);
	}

	public void captureScreenshot(String keyword) {

		TakesScreenshot scrShot = ((TakesScreenshot) this.driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File destFile = new File(screenshotFolder+"/temp/" + keyword + ".png");
		
		try {
			FileUtils.copyFile(SrcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
			}
	}

	public void testCasePassed(String testCaseName, String message) {
		for (TestActionListener el : listeners) {
			el.passTestCase(testCaseName, message);
		}
	}

	public void testCaseFailed(String testCaseName, String message, Exception e) {
		for (TestActionListener el : listeners) {
			el.failTestCase(testCaseName, message, e);
		}
	}

	public void saveScreenshot() throws InvalidFormatException, IOException {
//		String fileExtension = String.valueOf(PropertyCache.getProperty("EvidenceFormat"));
//		fileExtension=fileExtension ==null?"docx":fileExtension;
		String fileName=PropertyCache.getProperty("TestName")+"-"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String outputFile=reportUtil.createNewWord(screenshotFolder+"/temp", screenshotFolder,fileName );
//		if(fileExtension.equalsIgnoreCase("docx")) {
//			outputFile=;
//		}
		for (TestActionListener el : listeners) {
			el.uploadEvidence(outputFile);
		}
		FileUtils.forceDelete(new File(screenshotFolder+"/temp"));
	}

}
