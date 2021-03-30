package framework.automation.selenium.core;

import org.openqa.selenium.WebDriver;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.exceptions.PropertyNotConfiguredException;
import framework.automation.selenium.core.helpers.TestDataHelper;
import framework.automation.selenium.core.tools.Browser;
import framework.automation.selenium.core.tools.KeywordProcessor;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose 
 */
public final class TestEngine {
	
	private static PropertyCache prop;
	private WebDriver driver; 
	private Browser browser; 
	private KeywordProcessor interpretor;
	private TestDataHelper dataHelper;
	
	public TestEngine(String testName) throws Exception  {
		if(TestEngine.prop==null) {
			throw new PropertyNotConfiguredException("Property file not set for this test. use 'TestEngine.setPropertyFile(filepath)' before creating TestEngine instance.  ");
		}
		PropertyCache.setProperty("TestName", testName);
		this.browser =new Browser();
		this.dataHelper=new TestDataHelper();
	}
	
	public static final void setPropertyFile(String filePath) throws Exception {
		TestEngine.prop=new PropertyCache(filePath);
		//Configure Log4j from here
	}
	
	
	
	/**
	 * @param browserName 
	 * @throws Exception  
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void start()  throws Exception {
		this.browser.setBrowserName(PropertyCache.getProperty("BrowserName")==null?PropertyCache.getProperty("DefaultBrowser").toString():PropertyCache.getProperty("BrowserName").toString());
		this.browser.setHeadless(PropertyCache.getProperty("IsHeadless")==null?false: (boolean) PropertyCache.getProperty("IsHeadless"));
		this.browser.setIncognito(PropertyCache.getProperty("IsIncognito")==null?false: (boolean) PropertyCache.getProperty("IsIncognito"));
		this.driver=this.browser.open();
		this.interpretor=new KeywordProcessor(this.driver);
	}

	/**
	 * @param keyword 
	 * @throws Exception  
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void execute(String keyword) throws Exception {
		this.interpretor.interpretAndProcess(keyword);
		Thread.sleep(Integer.parseInt(PropertyCache.getProperty("DefaultWait").toString()) );
		
	}

	/**
	 * @purpose 
	 * @date 27-Mar-2021
	 */
	public final void stop() {
		this.driver.close();
		//prepare report here
	}
	
	public final Object[] getKeywords()  throws Exception {
		return this.dataHelper.getKeywords(PropertyCache.getProperty("TestName").toString());
	}
	
	public final void setBrowser(String browserName,boolean isHeadless,boolean isIncognito) throws NoSuchTestFoundException {
		PropertyCache.setProperty("BrowserName", browserName);
		PropertyCache.setProperty("IsHeadless", isHeadless);
		PropertyCache.setProperty("IsIncognito", isIncognito);
	}
	
	public static final void setProperty(String key, Object value) throws NoSuchTestFoundException {
		PropertyCache.setProperty(key, value);
	}
	

}
