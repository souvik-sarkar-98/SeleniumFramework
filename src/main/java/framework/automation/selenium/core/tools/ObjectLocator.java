package framework.automation.selenium.core.tools;

import java.io.IOException;
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
	private Class<?> testClass;

	/**
	 * @param testClass
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * 
	 */
	public ObjectLocator(Class<?> testClass, WebDriver driver)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		logger.traceEntry("with {}", driver.toString());
		this.testClass = testClass;
		this.objHelper = new WebObjectHelper(this.testClass);
		this.driver = driver;
		logger.traceExit();
	}

	/**
	 * @purpose
	 * @date 29-Mar-2021
	 * @param string
	 * @return
	 * @throws WebObjectIdentifierNotFoundException
	 * @throws XMLElementNotFoundException
	 */
	public WebElement getWebElement(String elementName)
			throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		logger.traceEntry("with {}", elementName.toString());
		WebElement element = null;
		By[] locators = this.objHelper.getLocators(elementName);
		for (By locator : locators) {
			try {
				WebDriverWait wait = new WebDriverWait(driver,
						Integer.parseInt(PropertyCache.getProperty("ExplicitWait").toString()));
				try {
					By[] loading = this.objHelper
							.getLocators(String.valueOf(PropertyCache.getProperty("LoadingObjectName")), true);
					//logger.info("Started");
					wait.until(ExpectedConditions.invisibilityOfElementLocated(loading[0]));
					//logger.info("End");
				} catch (Exception e) {
					logger.warn("Loading object not found. Hence skipping loading object check."
							+ "\nIf you want to check this please make sure that your header object is named '"
							+ PropertyCache.getProperty("LoadingObjectName") + "'");
				}

//				wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

				// element=
				List<WebElement> list = driver.findElements(locator);
				if (!list.isEmpty()) {
					element = list.get(0);
					//wait.until(ExpectedConditions.elementToBeClickable(element));
				}
				this.highlightElement(element);
				break;
			} catch (Exception e) {
				logger.error(e);

			}
		}
		if (element == null) {
			NoSuchElementException e = new NoSuchElementException("No such element named '" + elementName + "' in page "
					+ driver.getTitle() + " url " + driver.getCurrentUrl());
			throw e;
		}
		logger.traceExit("returning {}", element.toString());
		return element;
	}

	public void setRepository(String repoName) throws XPathExpressionException, DOMException,
			ParserConfigurationException, SAXException, IOException, XMLElementNotFoundException {
		logger.traceEntry("with {}", repoName.toString());
		this.objHelper.loadRepository(repoName);
		try {
			getWebElement(String.valueOf(PropertyCache.getProperty("HeaderObjectName")));
		} catch (Exception e) {
			logger.warn("Header object not found. Hence skipping header object check. "
					+ "\nIf you want to check this please make sure that your header object is named '"
					+ PropertyCache.getProperty("HeaderObjectName") + "'");
		}
		logger.traceExit();
	}

	private void highlightElement(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String style=String.valueOf(PropertyCache.getProperty("HighlightStyle"));
		js.executeScript("arguments[0].setAttribute('style', '"+style+"');", element);
		logger.traceExit();
	}

}
