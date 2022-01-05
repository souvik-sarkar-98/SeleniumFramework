package framework.automation.selenium.core;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.exceptions.PropertyNotConfiguredException;
import framework.automation.selenium.core.helpers.ReportHelper;
import framework.automation.selenium.core.helpers.TestDataHelper;
import framework.automation.selenium.core.tools.Browser;
import framework.automation.selenium.core.tools.KeywordProcessor;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public final class TestEngine {
	private final Logger logger = LogManager.getLogger(this.getClass());

	private static PropertyCache prop;
	private WebDriver driver;
	private Browser browser;
	private KeywordProcessor interpretor;
	private TestDataHelper dataHelper;

	private ReportHelper reporter;

	private Class<?> testClass;

	/**
	 * @throws Exception
	 * @purpose To Create the instance of TestEngine class
	 * @date 27-Mar-2021
	 */
	public TestEngine(Class<?> testClass) throws Exception {
		this.testClass = testClass;
		logger.traceEntry("with parameter {}", this.testClass.getSimpleName());
		if (TestEngine.prop == null) {

			Exception e = new PropertyNotConfiguredException(
					"Property file not set for this test. use 'TestEngine.setPropertyFile(filepath)' before creating TestEngine instance.  ");
			logger.error("Property not configured. Throwing Exception ", e);
			throw e;
		}
		PropertyCache.setProperty("TestName", this.testClass.getSimpleName());
		this.browser = new Browser(this.testClass);
		this.dataHelper = new TestDataHelper(this.testClass);
		logger.traceExit();
	}

	/**
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	public static final void setPropertyFile(String filePath) throws Exception {
		TestEngine.prop = new PropertyCache(filePath);
	}

	/**
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	public final void run() throws Exception {
		logger.traceEntry("Starting Test");
		this.browser.setBrowserName(PropertyCache.getProperty("BrowserName") == null
				? PropertyCache.getProperty("DefaultBrowser").toString()
				: PropertyCache.getProperty("BrowserName").toString());
		this.browser.setHeadless(PropertyCache.getProperty("IsHeadless") == null ? false
				: (boolean) PropertyCache.getProperty("IsHeadless"));
		this.browser.setIncognito(PropertyCache.getProperty("IsIncognito") == null ? false
				: (boolean) PropertyCache.getProperty("IsIncognito"));
		//this.validate();
		this.driver = this.browser.open();
		this.interpretor = new KeywordProcessor(this.testClass, this.driver, this.dataHelper);
		this.reporter = new ReportHelper(this.driver);
		this.execute();
		logger.traceExit();

	}

	/**
	 * @param keyword
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	private final void execute() throws Exception {
		logger.traceEntry("Executing Keyword");
		Object[] keywords = this.dataHelper.getKeywords(PropertyCache.getProperty("TestName").toString());
		for (Object keyword : keywords) {
			if (!keyword.equals("")) {
				try {
				logger.info(keyword);
				this.interpretor.interpretAndProcess(keyword.toString());
				this.reporter.captureScreenshot();
				Thread.sleep(Integer.parseInt(PropertyCache.getProperty("DefaultWait").toString()) * 1000);
				}catch(Exception e) {
					logger.error(e);
					JFrame jf=new JFrame();
					jf.setAlwaysOnTop(true);
					int response=JOptionPane.showConfirmDialog(jf,
							 "<html><body><p style='width: 500px;'>"+
							"Execution of '"+keyword+"' keyword has failed due to error '"+e.getMessage()
							+"' If you want to continue the test, Perform the step manually and click on 'YES' else click on 'NO'"
							+"</p></body></html>",
			                "Warning", 
			                JOptionPane.YES_NO_OPTION,
			                JOptionPane.QUESTION_MESSAGE); 
					//System.err.println(response);
					if(response != 0) {
						throw e;
					}
				}
			}
		}

		logger.traceExit();

	}

	/*
	private final void validate() throws Exception {
		logger.traceEntry("Validating Keyword");
		Map<String, String> list = new HashMap<String, String>();
		Object[] keywords = this.dataHelper.getKeywords(PropertyCache.getProperty("TestName").toString());
		for (Object keyword : keywords) {
			if (!keyword.equals("")) {
				String error = KeywordProcessor.validateKeyword(keyword.toString(), this.dataHelper);
				if (error != null) {
					list.put(keyword.toString(), error);
					logger.error(keywords.toString() + " " + error);
				}
				// System.err.println(keyword +"--"+error );
			}
		}
		
		logger.traceExit();

	}*/

	/**
	 * @purpose
	 * @date 27-Mar-2021
	 */
	public final void stop() {
		logger.traceEntry("Ending Test");
		String closeBrowser = PropertyCache.getProperty("CloseBrowserAfterTest") == null
				? PropertyCache.getProperty("AutoCloseBrowser").toString()
				: PropertyCache.getProperty("CloseBrowserAfterTest").toString();

		if ("TRUE".equalsIgnoreCase(closeBrowser)) {
			this.driver.close();
		}
		logger.traceExit();
	}

	public final void generateReport() {
		logger.traceEntry();
		// this.dataHelper.close();
		// this.dataHelper.writeProblems();
		// this.reporter.prepareReport();

		// prepare report here create new Instance for report
		logger.traceExit();
	}

	public final void setBrowser(String browserName, boolean isHeadless, boolean isIncognito)
			throws NoSuchTestFoundException {
		logger.traceEntry("Setting Test browser : browserName={},isHeadless={},isIncognito={}", browserName, isHeadless,
				isIncognito);
		PropertyCache.setProperty("BrowserName", browserName);
		PropertyCache.setProperty("IsHeadless", isHeadless);
		PropertyCache.setProperty("IsIncognito", isIncognito);
		logger.traceExit();
	}

	public final void autoCloseBrowser(boolean isAutoCloseBrowser) throws NoSuchTestFoundException {
		// this.autoCloseBrowser=isAutoCloseBrowser;
		PropertyCache.setProperty("CloseBrowserAfterTest", isAutoCloseBrowser);
	}

	public static final void setProperty(String key, Object value) throws NoSuchTestFoundException {
		PropertyCache.setProperty(key, value);
	}

}
