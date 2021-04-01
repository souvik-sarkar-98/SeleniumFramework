package framework.automation.selenium.core;

import java.util.Arrays;

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
	
	
	/**
	 * @throws Exception  
	 * @purpose To Create the instance of TestEngine class
	 * @date 27-Mar-2021
	 */
	public TestEngine(String testName) throws Exception  {
		logger.traceEntry("with parameter {}",testName);
		if(TestEngine.prop==null) {
			Exception e= new PropertyNotConfiguredException("Property file not set for this test. use 'TestEngine.setPropertyFile(filepath)' before creating TestEngine instance.  ");
			logger.error("Property not configured. Throwing Exception ",e);
			throw e;
		}
		PropertyCache.setProperty("TestName", testName);
		this.browser =new Browser();
		this.dataHelper=new TestDataHelper();
		logger.traceExit();
	}
	
	/**
	 * @throws Exception  
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public static final void setPropertyFile(String filePath) throws Exception {
		TestEngine.prop=new PropertyCache(filePath);
	}
	
	
	
	/**
	 * @throws Exception  
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void start()  throws Exception {
		logger.traceEntry("Starting Test");
		this.browser.setBrowserName(PropertyCache.getProperty("BrowserName")==null?PropertyCache.getProperty("DefaultBrowser").toString():PropertyCache.getProperty("BrowserName").toString());
		this.browser.setHeadless(PropertyCache.getProperty("IsHeadless")==null?false: (boolean) PropertyCache.getProperty("IsHeadless"));
		this.browser.setIncognito(PropertyCache.getProperty("IsIncognito")==null?false: (boolean) PropertyCache.getProperty("IsIncognito"));
		this.driver=this.browser.open();
		this.interpretor=new KeywordProcessor(this.driver,this.dataHelper);
		this.reporter=   new ReportHelper(this.driver);
		logger.traceExit();

	}

	/**
	 * @param keyword 
	 * @throws Exception  
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void execute(String keyword) throws Exception {
		logger.traceEntry("Executing Keyword");
		this.interpretor.interpretAndProcess(keyword);
		Thread.sleep(Integer.parseInt(PropertyCache.getProperty("DefaultWait").toString()) );
		this.reporter.captureScreenshot();
		logger.traceExit();

	}

	/**
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void stop() {
		logger.traceEntry("Ending Test");
		this.driver.close();
		logger.traceExit();
	}
	
	public final void generateReport() {
		logger.traceEntry();
		this.dataHelper.close();
		//this.dataHelper.writeProblems();
		this.reporter.prepareReport();
		
		//prepare report here create new Instance for report
		logger.traceExit();
	}
	
	public final Object[] getKeywords()  throws Exception {
		logger.traceEntry("Fetching keywords for test");
		Object[] keywords= this.dataHelper.getKeywords(PropertyCache.getProperty("TestName").toString());
		logger.traceExit(Arrays.asList(keywords));
		return keywords;

	}
	
	public final void setBrowser(String browserName,boolean isHeadless,boolean isIncognito) throws NoSuchTestFoundException {
		logger.traceEntry("Setting Test browser : browserName={},isHeadless={},isIncognito={}",browserName,isHeadless,isIncognito);
		PropertyCache.setProperty("BrowserName", browserName);
		PropertyCache.setProperty("IsHeadless", isHeadless);
		PropertyCache.setProperty("IsIncognito", isIncognito);
		logger.traceExit();
	}
	
	public static final void setProperty(String key, Object value) throws NoSuchTestFoundException {
		PropertyCache.setProperty(key, value);
	}
	

}
