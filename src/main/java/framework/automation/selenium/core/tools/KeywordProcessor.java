package framework.automation.selenium.core.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.exceptions.InvalidKeywordDataFormatException;
import framework.automation.selenium.core.exceptions.KeywordParameterMismatchException;
import framework.automation.selenium.core.exceptions.WebObjectIdentifierNotFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.helpers.TestDataHelper;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public final class KeywordProcessor {
    private final Logger logger = LogManager.getLogger(this.getClass());
	protected final WebDriver driver;
	protected final ActionPerformer reflect;
	private final TestDataHelper dataHelper;
	private ObjectLocator locator;
	private Class<?> testClass;

	/**
	 * @param testClass 
	 * @param driver
	 * @param dataHelper 
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws FileNotFoundException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public KeywordProcessor(Class<?> testClass, final WebDriver driver, TestDataHelper dataHelper) throws ClassNotFoundException, FileNotFoundException, InvalidFormatException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		logger.traceEntry(" with {}",driver.toString());
		this.testClass=testClass;
		this.driver = driver;
		this.dataHelper=dataHelper;
		this.reflect = new ActionPerformer(this.driver);
		this.locator= new ObjectLocator(this.testClass,this.driver);
		logger.traceExit();
	}

	public void interpretAndProcess(String keyword)
			throws NoSuchMethodException, SecurityException, KeywordParameterMismatchException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidKeywordDataFormatException, XPathExpressionException, DOMException, ParserConfigurationException, SAXException, IOException, XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		logger.traceEntry(" with {}",keyword);
		String[] items = keyword.split("_");
		String action = null, data = null;
		WebElement object = null;
		
		action = items[0];
		if(action.equalsIgnoreCase("NavigateTo")) {
			logger.debug("Action is equals to NavigateTo");
			if(items.length==2) {
				this.locator.setRepository(items[1]);
			}else {
				this.dataHelper.reportKeywordProblems(keyword, action+" requires 1 parameter but "+(items.length-1)+" given.");
				KeywordParameterMismatchException e= new KeywordParameterMismatchException("The action '" + action + "' written in keyword '" + keyword
						+ "' requires 1 parameter but "+(items.length-1)+" given.");
				logger.error(e);
				throw e;
			}
		}else {
			Class<?>[] paramClasses = this.reflect.inspectParameters(action);
			if (paramClasses.length != (items.length - 1)) {
				this.dataHelper.reportKeywordProblems(keyword, action +" requires " + paramClasses.length + " parameter " + Arrays.asList(paramClasses) + " but only "
						+ (items.length - 1) + " are given.");
				KeywordParameterMismatchException e= new KeywordParameterMismatchException("The action '" + action + "' written in keyword '" + keyword
						+ "' requires " + paramClasses.length + " argument " + Arrays.asList(paramClasses) + " but only "
						+ (items.length - 1) + " arguments are given.");
				logger.error(e);
				throw e;
			} else if (items.length == 1) {
				this.reflect.perform(action);
			} else if (items.length == 2 && paramClasses[0].equals(String.class)) {
				
				data = dataHelper.getTestData(items[1]);
				this.reflect.perform(action, data);

			} else if (items.length == 2 && paramClasses[0].equals(WebElement.class)) {
				object = this.locator.getWebElement(items[1]);
				this.reflect.perform(action, object);
			} else if (items.length == 3) {
				object = this.locator.getWebElement(items[1]);
				data = dataHelper.getTestData(items[2]);
				this.reflect.perform(action, object, data);

			}
		}
		logger.traceExit();

	}
	
	

}
