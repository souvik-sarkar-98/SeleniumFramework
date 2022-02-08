package framework.automation.selenium.core.tools;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.BrowserNotFoundException;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Souvik Sarkar
 * @createdOn 25-Mar-2021
 * @purpose :
 */
public class Browser {
    private final Logger logger = LogManager.getLogger(this.getClass());
	private String[] supportedBrowsers= {"Chrome","Firefox","Opera","Edge","IE"};
	private String browserName=null;
	private boolean isHeadless=false;
	private boolean isIncognito=false;
	protected WebDriver driver;


	public  WebDriver open() throws BrowserNotFoundException {
		logger.traceEntry();
		
		if("Chrome".equalsIgnoreCase(this.browserName)) {
			driver= this.chrome();
		}else if("FireFox".equalsIgnoreCase(this.browserName)) {
			driver= this.fireFox();
		}else if("Opera".equalsIgnoreCase(this.browserName)) {
			driver= this.opera();
		}else if("Edge".equalsIgnoreCase(this.browserName)) {
			driver= this.edge();
		}else if(this.browserName==null || "IE".equalsIgnoreCase(this.browserName)){
			driver= this.internetExplorer();
		}else {
			throw new BrowserNotFoundException("The browser '"+this.browserName+"' is not supportable. Please use "+String.join(" or ",this.supportedBrowsers));
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		//System.err.println(PropertyCache.getProperty("ImplicitWait").toString());
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(PropertyCache.getProperty("ImplicitWait").toString()), TimeUnit.SECONDS);
		logger.traceExit("returning {}",driver);
		return driver;

	}
	
	public  void close()  {
		logger.traceEntry();
		
		driver.close();
		logger.traceExit();

	}


	
	private WebDriver chrome() {
		logger.traceEntry();
		/*
		if(PropertyCache.getProperty("ChromeDriverPath")==null) {
			WebDriverManager.chromedriver().setup();
		}else{
			System.setProperty("webdriver.chrome.driver", PropertyCache.getProperty("ChromeDriverPath").toString());
		}
		*/
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(this.isHeadless);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);
		options.setCapability("", isHeadless);
		if(this.isIncognito) {
			options.addArguments("incognito");
		}
		ChromeDriver cd=new ChromeDriver(options);
		logger.traceExit("returning {}",cd);
		return cd;
	}
	
	private WebDriver fireFox() {
		logger.traceEntry();
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		if(this.isIncognito) {
			options.addArguments("-private");
		}
		options.setHeadless(this.isHeadless);
		FirefoxDriver fd= new FirefoxDriver(options);
		logger.traceExit("returning {}",fd);
		return fd;
	}
	
	private WebDriver opera() {
		logger.traceEntry();
		WebDriverManager.operadriver().setup();
		OperaOptions options= new OperaOptions();
		if(this.isIncognito) {
			options.addArguments("-private");
		}
		if(this.isHeadless) {
			System.err.println("[ERROR] Headless mode is not available for Opera browser");
		}
		OperaDriver od=new OperaDriver(options);
		logger.traceExit("returning {}",od);
		return od;
	}
	
	private WebDriver edge() {
		logger.traceEntry();

		WebDriverManager.edgedriver().setup();
		EdgeOptions options= new EdgeOptions();
		if(this.isIncognito) {
			options.setCapability("InPrivate", true);
		}
		EdgeDriver ed=new EdgeDriver(options);
		if(this.isHeadless) {
			System.err.println("[ERROR] Headless mode is not available for Edge browser");
		}
		logger.traceExit("returning {}",ed);
		return ed;
	}
	
	private WebDriver internetExplorer() {
		logger.traceEntry();
		/*
		if(PropertyCache.getProperty("IEDriverPath")==null) {
			WebDriverManager.iedriver().arch32().setup();
		}else{
			System.setProperty("webdriver.ie.driver", PropertyCache.getProperty("IEDriverPath").toString());
		}
		*/
		WebDriverManager.iedriver().arch32().setup();
		InternetExplorerOptions  options= new InternetExplorerOptions();
		options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		if(this.isIncognito) {
			options.setCapability("InPrivate",true);
		}
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		if(this.isHeadless) {
			System.err.println("[ERROR] Headless mode is not available for IE browser");
		}
		InternetExplorerDriver ied=new InternetExplorerDriver(options);
		logger.traceExit("returning {}",ied);
		return ied;
	}



	/**
	 * @param browserName the browserName to set
	 */
	public void setBrowserName(String browserName) {
		logger.traceEntry();
		this.browserName = browserName;
		logger.traceExit();
	}



	/**
	 * @param isHeadless the isHeadless to set
	 * @remember Headless browsing is only supported by Chrome and firefox
	 */
	public void setHeadless(boolean isHeadless) {
		logger.traceEntry();
		this.isHeadless = isHeadless;
		logger.traceExit();
	}



	/**
	 * @param isIncognito the isIncognito to set
	 */
	public void setIncognito(boolean isIncognito) {
		logger.traceEntry();
		this.isIncognito = isIncognito;
		logger.traceExit();
	}

}
