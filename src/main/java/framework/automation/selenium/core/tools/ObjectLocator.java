package framework.automation.selenium.core.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.WebObjectIdentifierNotFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.helpers.WebObjectHelper;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose :
 */
public class ObjectLocator {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private WebObjectHelper objHelper;
	private WebDriver driver;
	private String repoName;
	private long explicitWait;
	private Actions actions;
	// private Class<?> testClass;

	/**
	 * @param testClass
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * 
	 */
	public ObjectLocator(/* Class<?> testClass, */ WebDriver driver)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		logger.traceEntry("with {}", driver.toString());
		// this.testClass = testClass;
		this.objHelper = new WebObjectHelper(/* this.testClass */);
		this.driver = driver;
		this.explicitWait=Integer.parseInt(String.valueOf(PropertyCache.getProperty("ExplicitWait")));
		this.actions=new Actions(driver);
		
		logger.traceExit();
		
	}

	/**
	 * @purpose
	 * @date 29-Mar-2021
	 * @param string
	 * @return
	 * @throws WebObjectIdentifierNotFoundException
	 * @throws XMLElementNotFoundException
	 * @throws NoSuchElementException
	 */
	public WebElement getWebElement(String elementName)
			throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		logger.traceEntry("with {}", elementName.toString());
		By[] locators = this.objHelper.getLocators(elementName);
		try {
			return getWebElement(locators);
		} catch (NoSuchElementException e) {
			e = new NoSuchElementException("No such element named '" + elementName + "' in page '"+repoName+"'  PageTitle: " + driver.getTitle()
					+ " URL: " + driver.getCurrentUrl());
			throw e;
		}
	}

	/**
	 * @purpose
	 * @date 29-Mar-2021
	 * @param string
	 * @return
	 */
	public WebElement getWebElement(By... locators) {
		logger.traceEntry("with {}", locators.toString());
		List<WebElement> list = getWebElements(locators);
		WebElement element;
		if (!list.isEmpty()) {
			element = list.get(0);
			this.actions.moveToElement(element).build().perform();
		} else {
			NoSuchElementException e = new NoSuchElementException("No such element found with locator '" + locators
					+ "' in page " + driver.getTitle() + " url " + driver.getCurrentUrl());
			throw e;
		}
		logger.traceExit("returning {}", element);
		return element;
	}

	public List<WebElement> getWebElements(By... locators) {
		logger.traceEntry("with {}", locators.toString());
		List<WebElement> elements = new ArrayList<>();

		for (By locator : locators) {
			try {
				
				WebDriverWait wait = new WebDriverWait(driver,explicitWait);
//				wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

				elements = driver.findElements(locator);
				for (WebElement element : elements) {
					this.highlightElement(element);
					this.actions.moveToElement(element).build().perform();

				}
				break;
			} catch (Exception e) {
				logger.error(e);

			}
		}
		logger.traceExit("returning {}", elements);
		return elements;
	}

	public void setRepository(String repoName) throws XPathExpressionException, DOMException,
			ParserConfigurationException, SAXException, IOException, XMLElementNotFoundException {
		logger.traceEntry("with {}", repoName.toString());
		this.repoName=repoName;
		this.objHelper.loadRepository(repoName);
		try {
			//getWebElement(String.valueOf(PropertyCache.getProperty("HeaderObjectName")));
			By[] locators = this.objHelper.getLocators(String.valueOf(PropertyCache.getProperty("HeaderObjectName")),true);
			getWebElements(locators);
		} catch (Exception e) {
			logger.info("Header object not found. Hence skipping header object check."
					+ "If you want to check this please make sure that your header object is named '"
					+ PropertyCache.getProperty("HeaderObjectName") + "'");
		}
		logger.traceExit();
	}

	private void highlightElement(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String style = String.valueOf(PropertyCache.getProperty("HighlightStyle"));
		js.executeScript("arguments[0].setAttribute('style', '" + style + "');", element);
		logger.traceExit();
	}

	public long getExplicitWait() {
		return explicitWait;
	}

	public void setExplicitWait(long explicitWait) {
		this.explicitWait = explicitWait;
	}

}
