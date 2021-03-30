package framework.automation.selenium.core.tools;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
	
	private String[] supportedBrowsers= {"Chrome","Firefox","Opera","Edge","IE"};
	private String browserName=null;
	private boolean isHeadless=false;
	private boolean isIncognito=false;
	

	public WebDriver open() throws BrowserNotFoundException {
		WebDriver driver;
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
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(PropertyCache.getProperty("ImplicitWait").toString()), TimeUnit.SECONDS);
		return driver;

	}


	
	private WebDriver chrome() {
		if(PropertyCache.getProperty("ChromeDriverPath")==null) {
			WebDriverManager.chromedriver().setup();
		}else{
			System.setProperty("webdriver.chrome.driver", PropertyCache.getProperty("ChromeDriverPath").toString());
		}
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(this.isHeadless);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);

		if(this.isIncognito) {
			options.addArguments("incognito");
		}
			
		return new ChromeDriver(options);
	}
	
	private WebDriver fireFox() {
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		if(this.isIncognito) {
			options.addArguments("-private");
		}
		options.setHeadless(this.isHeadless);
		return new FirefoxDriver(options);
	}
	
	private WebDriver opera() {
		WebDriverManager.operadriver().setup();
		OperaOptions options= new OperaOptions();
		if(this.isIncognito) {
			options.addArguments("-private");
		}
		return new OperaDriver(options);
	}
	
	private WebDriver edge() {
		WebDriverManager.edgedriver().setup();
		EdgeOptions options= new EdgeOptions();
		if(this.isIncognito) {
			options.setCapability("InPrivate", true);
		}

		return new EdgeDriver();
	}
	
	private WebDriver internetExplorer() {
		if(PropertyCache.getProperty("IEDriverPath")==null) {
			WebDriverManager.iedriver().arch32().setup();
		}else{
			System.setProperty("webdriver.ie.driver", PropertyCache.getProperty("IEDriverPath").toString());
		}
		InternetExplorerOptions  options= new InternetExplorerOptions();
		if(this.isIncognito) {
			options.setCapability("InPrivate",true);
		}
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		return new InternetExplorerDriver(options);
	}



	/**
	 * @param browserName the browserName to set
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}



	/**
	 * @param isHeadless the isHeadless to set
	 * @remember Headless browsing is only supported by Chrome and firefox
	 */
	public void setHeadless(boolean isHeadless) {
		this.isHeadless = isHeadless;
	}



	/**
	 * @param isIncognito the isIncognito to set
	 */
	public void setIncognito(boolean isIncognito) {
		this.isIncognito = isIncognito;
	}

}
