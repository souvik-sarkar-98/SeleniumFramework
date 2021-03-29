package framework.automation.selenium.core.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.openqa.selenium.By;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.InvalidLocatorTypeException;
import framework.automation.selenium.core.exceptions.WebObjectIdentifierNotFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.utils.XMLUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class WebObjectHelper {

	private XMLUtil mappingUtil;
	private XMLUtil objectUtil;
	private String mappingXml;
	private String objectXML;

	public WebObjectHelper() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		this.mappingXml = PropertyCache.getProperty("ObjectMappingFile").toString();
		this.mappingUtil = new XMLUtil(mappingXml);
	}

	/**
	 * @param repoName
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * @throws XMLElementNotFoundException
	 * @throws DOMException
	 * @purpose
	 * @date 29-Mar-2021
	 */
	public void loadRepository(String repoName) throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException, DOMException, XMLElementNotFoundException {
		NodeList mappingNode = this.mappingUtil.findNodeListByTags("repository", "name", repoName, "path");
		if (mappingNode == null) {
			throw new XMLElementNotFoundException(
					"No such object repository named '" + repoName + "' defined in " + this.mappingXml);
		}
		this.objectXML = mappingNode.item(0).getTextContent();
		this.objectUtil = new XMLUtil(this.objectXML);
	}

	/**
	 * @purpose
	 * @date 29-Mar-2021
	 * @param elementName
	 * @return
	 * @throws XMLElementNotFoundException
	 * @throws WebObjectIdentifierNotFoundException
	 */
	public By[] getLocators(String elementName)
			throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		
		List<By> locators = new ArrayList<By>();
		
		NodeList nodes = this.objectUtil.findNodeListByTags("object", "name", elementName, "identifier");
		
		if (nodes == null) {
			throw new XMLElementNotFoundException(
					"No such object element '" + elementName + "' defined in " + this.objectXML);
		}
		
		for (int i = 0; i < nodes.getLength(); i++) {
		
			Element elem = (Element) nodes.item(i);
			
			try {
			
				locators.add(this.createByObject(elem.getAttribute("locator"), elem.getAttribute("value")));
			
			} catch (InvalidLocatorTypeException e) {
				// log it
				e.printStackTrace();
			}
			
		}
		
		if (locators.size() == 0) {
			throw new WebObjectIdentifierNotFoundException("Either no identifier defined for '" + elementName + "' in "
					+ this.objectUtil + "\nOr defined locator is invalid.");
		}
		for(By abc: locators) {
			System.out.println(abc);
		}
		return locators.toArray(new By[locators.size()]);
	}
	
	

	private By createByObject(String locator, String value) throws InvalidLocatorTypeException {

		switch (locator.toLowerCase()) {
		
		case "id":
			return By.id(value);
		case "name":
			return By.name(value);
		case "classname":
			return By.className(value);
		case "cssselector":
			return By.cssSelector(value);
		case "linktext":
			return By.linkText(value);
		case "partiallinktext":
			return By.partialLinkText(value);
		case "tagname":
			return By.tagName(value);
		case "xpath":
			return By.xpath(value);
		default:
			throw new InvalidLocatorTypeException("No such locator '"+locator+"' available. Try with id/name/className/cssSelector/linktext/partiallinktext/tagname/xpath");
		}

	}

}
