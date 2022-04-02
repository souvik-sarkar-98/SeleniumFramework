package framework.automation.selenium.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.BrowserNotFoundException;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.exceptions.PropertyNotConfiguredException;
import framework.automation.selenium.core.exceptions.ResourceConfigurationException;
import framework.automation.selenium.core.helpers.ReportHelper;
import framework.automation.selenium.core.helpers.TestDataHelper;
import framework.automation.selenium.core.tools.Browser;
import framework.automation.selenium.core.tools.KeywordProcessor;
import framework.automation.selenium.core.utils.MiscUtils;

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
		// for log4j config
		// System.setProperty("TestCaseName", testClass.getSimpleName());
		this.testClass = testClass;
		// Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
		logger.traceEntry("with parameter {}", this.testClass.getSimpleName());

		if (TestEngine.prop == null) {
			try {
				String.valueOf(Paths.get(testClass.getResource("property.xml").toURI()));
			} catch (NullPointerException e) {
				logger.error(e);
				throw new PropertyNotConfiguredException("No 'property.xml' file found at default location'"
						+ testClass.getPackage().getName().replace(".", "\\") + "'" + " under test resources folder.\n"
						+ "Use 'TestEngine.setPropertyFile(filepath)' before creating TestEngine instance.  ");

			} catch (Exception e) {
				logger.error(e);
				throw e;
			}
		}
		PropertyCache.setProperty("TestName", this.testClass.getSimpleName());

		// PropertyCache.setProperty("BrowserName", this.testClass.getSimpleName());
		try {
			this.browser = new Browser();
			this.dataHelper = new TestDataHelper(this.testClass);
		} catch (InvalidFormatException | IOException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		logger.traceExit();
	}

	public final void run() throws Exception {
		run(null);
	}

	/**
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	public final void run(TestActionListener tl) throws Exception {
		logger.traceEntry("Starting Test");
		this.browser.setBrowserName(PropertyCache.getProperty("BrowserName") == null
				? PropertyCache.getProperty("DefaultBrowser").toString()
				: PropertyCache.getProperty("BrowserName").toString());
		this.browser.setHeadless(PropertyCache.getProperty("IsHeadless") == null ? false
				: "TRUE".equalsIgnoreCase(String.valueOf(PropertyCache.getProperty("IsHeadless"))));
		this.browser.setIncognito(PropertyCache.getProperty("IsIncognito") == null ? false
				: "TRUE".equalsIgnoreCase(String.valueOf(PropertyCache.getProperty("IsIncognito"))));
		// this.validate();

		try {
			String[] keywords= this.dataHelper.getKeywords(PropertyCache.getProperty("TestName").toString());
			this.driver = this.browser.open();
			this.interpretor = new KeywordProcessor(/* this.testClass, */ this.driver, this.dataHelper);
			this.reporter = new ReportHelper(this.driver);
			if (tl != null) {
				this.reporter.addTestActionListener(tl);
			}
			//String[] keywords = Arrays.copyOf(keywordsObj, keywordsObj.length, String[].class);
			for (String keyword : keywords) {
				if ("".equals(keyword.trim())) {
					continue;
				}
				this.execute(keyword);
			}

		} catch (ClassNotFoundException | InvalidFormatException | XPathExpressionException | IOException
				| ParserConfigurationException | SAXException e) {
			logger.error(e);
			throw new ResourceConfigurationException(e);
		} catch (BrowserNotFoundException | NoSuchTestFoundException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

		logger.traceExit();

	}

	/**
	 * @param keyword
	 * @throws NoSuchTestFoundException
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	private final void execute(String keyword) throws Exception {
		logger.traceEntry("Executing Keyword");
		try {

			logger.info(keyword);
			this.interpretor.interpretAndProcess(keyword);
			String img=this.reporter.captureScreenshot(keyword);
			this.reporter.testCasePassed(keyword, "Working as expected",img);
			Thread.sleep(Integer.parseInt(PropertyCache.getProperty("DefaultWait").toString()) * 1000);
		} catch (Exception e) {
			logger.error(e);
			String img=this.reporter.captureScreenshot(keyword);
			this.reporter.testCaseFailed(keyword, "Failed to execute", e,img);
			if ("TRUE".equalsIgnoreCase(String.valueOf(PropertyCache.getProperty("IsHeadless")))) {
				throw e;
			}
			int response =showPopupError(keyword,e);
			
			if (response == 1) {
				execute(keyword);
			} else if (response == 2) {
				throw e;
			}
		}
		logger.traceExit();

	}

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
			this.browser.close();
		}

		try {
			this.reporter.saveScreenshot();
		} catch (InvalidFormatException | IOException e) {
			logger.error("Failed fo save screenshot", e);
		}
		logger.traceExit();
	}

	public static final void setPropertyFile(File file) throws ResourceConfigurationException {
		setPropertyFile(file.getAbsolutePath());
	}

	/**
	 * @throws ResourceConfigurationException
	 * @throws Exception
	 * @purpose
	 * @date 27-Mar-2021
	 */
	public static final void setPropertyFile(String filePath) throws ResourceConfigurationException {
		try {
			TestEngine.prop = new PropertyCache(filePath);
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			throw new ResourceConfigurationException(e);
		}
	}

	public final void setBrowser(String browserName, boolean isHeadless, boolean isIncognito) {
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
	
	
	private static int showPopupError(String keyword,Exception e1) {
		
		JFrame jf = new JFrame();
		jf.setAlwaysOnTop(true);
		String message = new StringBuilder().append("<html><body><div style='width: 500px;'>")
				.append("<p style='font-weight: bold;'>Execution of '" + keyword
						+ "' keyword has failed due to an error.</p>")
				.append("<p style='font-weight: bold;'>⚫ If you want to continue the test, Perform the step manually and click on 'Continue'."
						+ "<br>⚫ Click on 'Retry' to perform this keyword again."
						+ "<br>⚫ Click on 'End' to end the test.</p>")
				.append("<br>")
				.append("➼ Error Message : <p style='color:blue;'>"
						+ (e1.getMessage() == null ? "Not available" : e1.getMessage()) + "</p>")
				.append("<br>")
				.append("➼ Error Cause : <div style='color:red;'>" + (e1.getCause() == null ? " Not available"
						: MiscUtils.addLinebreaks(e1.getCause().getMessage(), 300)) + "</div>")
				.append("</div></body></html>").toString();

		message = message.replaceAll("[\\t\\n\\r]+", "<br>");
		String[] options = { "Continue", "Retry", "End" };
		Timer timer = new Timer(5000, (e) -> {
			jf.dispose();
			JOptionPane.getRootFrame().dispose();
		});
		timer.start();
		int response = JOptionPane.showOptionDialog(jf, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, // no custom icon
				options, // button titles
				options[0] // default button
		);
		jf.dispose();
		timer.stop();
		return response;
	}

}
