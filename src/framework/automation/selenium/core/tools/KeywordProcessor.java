package framework.automation.selenium.core.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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

	protected final WebDriver driver;
	protected final ActionPerformer reflect;
	private final TestDataHelper dataHelper;
	private ObjectLocator locator;

	/**
	 * @param driver
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws FileNotFoundException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public KeywordProcessor(final WebDriver driver) throws ClassNotFoundException, FileNotFoundException, InvalidFormatException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		this.driver = driver;
		this.reflect = new ActionPerformer(this.driver);
		this.dataHelper=new TestDataHelper();
		this.locator= new ObjectLocator(this.driver);
	}

	public void interpretAndProcess(String keyword)
			throws NoSuchMethodException, SecurityException, KeywordParameterMismatchException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidKeywordDataFormatException, XPathExpressionException, DOMException, ParserConfigurationException, SAXException, IOException, XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		String[] items = keyword.split("_");
		String action = null, data = null;
		WebElement object = null;

		action = items[0];
		if(action.equalsIgnoreCase("NavigateTo")) {
			if(items.length==2) {
				this.locator.setRepository(items[1]);
			}else {
				throw new KeywordParameterMismatchException("The action '" + action + "' written in keyword '" + keyword
						+ "' requires 1 parameter but "+(items.length-1)+" given.");
			}
		}else {
			Class<?>[] paramClasses = this.reflect.inspectParameters(action);

			if (paramClasses.length != (items.length - 1)) {

				throw new KeywordParameterMismatchException("The action '" + action + "' written in keyword '" + keyword
						+ "' requires " + paramClasses.length + " argument " + Arrays.asList(paramClasses) + " but only "
						+ (items.length - 1) + " arguments are given.");

			} else if (items.length == 1) {
				this.reflect.perform(action, object);
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

	}

}
