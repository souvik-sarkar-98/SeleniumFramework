package framework.automation.selenium.core.tools;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.exceptions.WebObjectIdentifierNotFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.helpers.WebObjectHelper;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose :
 */
public class ObjectLocator {
	
	private WebObjectHelper objHelper;
	private WebDriver driver;

	/**
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * 
	 */
	public ObjectLocator(WebDriver driver) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		this.objHelper=new WebObjectHelper();
		this.driver=driver;
	}

	/**
	 * @purpose 
	 * @date 29-Mar-2021
	 * @param string
	 * @return
	 * @throws WebObjectIdentifierNotFoundException 
	 * @throws XMLElementNotFoundException 
	 */
	public WebElement getWebElement(String elementName) throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		WebElement element=null;
		By[] locators=this.objHelper.getLocators(elementName);
		for(By locator:locators) {
			try {
				element=driver.findElement(locator);
				this.highlightElement(element);
			    break;
			} catch (NoSuchElementException e) {
				//log it
				e.printStackTrace();
			}
		}
		if(element==null) {
			throw new NoSuchElementException("No such element named '"+elementName+"' in page "+driver.getTitle()+" url "+driver.getCurrentUrl());
		}
		return element;
	}
	
	public void setRepository(String repoName) throws XPathExpressionException, DOMException, ParserConfigurationException, SAXException, IOException, XMLElementNotFoundException {
		this.objHelper.loadRepository(repoName);
	}
	
	private void highlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid blue;');", element);
	}

}
