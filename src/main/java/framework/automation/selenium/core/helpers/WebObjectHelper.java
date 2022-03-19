package framework.automation.selenium.core.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.InvalidLocatorTypeException;
import framework.automation.selenium.core.exceptions.WebObjectIdentifierNotFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.utils.MiscUtils;
import framework.automation.selenium.core.utils.XMLUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public final class WebObjectHelper {
    private final Logger logger = LogManager.getLogger(this.getClass());
	private XMLUtil mappingUtil;
	private XMLUtil objectUtil;
	private String mappingXml;
	private List<String> objXMLFilePaths;
	//private Class<?> testClass;

	public WebObjectHelper(/*Class<?> testClass*/) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		logger.traceEntry();
		//this.testClass=testClass;
		//commenting this
		//this.mappingXml = MiscUtils.getFilePath(this.testClass, PropertyCache.getProperty("ObjectMappingFile").toString());
		this.mappingXml = MiscUtils.getFilePath(String.valueOf(PropertyCache.getProperty("ObjectMappingFile")));
		this.mappingUtil = new XMLUtil(mappingXml);
		logger.traceExit();
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
		logger.traceEntry("with {}",repoName);
		NodeList mappingNode = this.mappingUtil.findNodeListByTags("repository", "name", repoName, "path");
		if (mappingNode == null) {
			XMLElementNotFoundException e=new XMLElementNotFoundException(
					"No such object repository named '" + repoName + "' defined in " + this.mappingXml);
			logger.error(e);
			 throw e;
		}
		//this.objectXML = mappingNode.item(0).getTextContent();
		this.objXMLFilePaths=new ArrayList<>();
		for(int i=0;i<mappingNode.getLength();i++) {
			this.objXMLFilePaths.add(MiscUtils.getFilePath(mappingNode.item(i).getTextContent()));
		}
		this.objectUtil = new XMLUtil(this.objXMLFilePaths.toArray(new String[this.objXMLFilePaths.size()]));
		logger.traceExit();
	}
	
	public By[] getLocators(String elementName)
			throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException{
		return getLocators(elementName,false);
	}

	/**
	 * @purpose
	 * @date 29-Mar-2021
	 * @param elementName
	 * @return
	 * @throws XMLElementNotFoundException
	 * @throws WebObjectIdentifierNotFoundException
	 */
	public By[] getLocators(String elementName,boolean ignoreError)
			throws XMLElementNotFoundException, WebObjectIdentifierNotFoundException {
		logger.traceEntry("with {}",elementName);
		List<By> locators = new ArrayList<By>();
		NodeList nodes = this.objectUtil.findNodeListByTags("object", "name", elementName, "identifier");
		if (nodes == null && ignoreError == false) {
			XMLElementNotFoundException e= new XMLElementNotFoundException(
					"No such object element '" + elementName + "' defined in " + this.objXMLFilePaths);
			logger.error(e);
			throw e;
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			try {
				By obj=this.createByObject(elem.getAttribute("locator"), elem.getAttribute("value"));
				logger.debug(obj.toString());
				locators.add(obj);
			} catch (InvalidLocatorTypeException e) {
				logger.error(e);
			}
			
		}
		if (locators.size() == 0 && ignoreError == false) {
			WebObjectIdentifierNotFoundException e= new WebObjectIdentifierNotFoundException("Either no identifier defined for '" + elementName + "' in "
					+ this.objectUtil + "\nOr defined locator is invalid.");
			logger.error(e);
			throw e;
		}
		logger.traceExit("returning {}",locators);

		return locators.toArray(new By[locators.size()]);
	}
	
	

	private By createByObject(String locator, String value) throws InvalidLocatorTypeException {
		By ret=null;
		switch (locator.toLowerCase()) {
		case "id":
			ret= By.id(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "name":
			ret= By.name(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "classname":
			ret= By.className(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "cssselector":
			ret= By.cssSelector(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "linktext":
			ret= By.linkText(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "partiallinktext":
			ret= By.partialLinkText(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "tagname":
			ret= By.tagName(value);
			logger.traceExit("returning {}",ret);
			return ret;
		case "xpath":
			ret= By.xpath(value);
			logger.traceExit("returning {}",ret);
			return ret;
		default:
			throw new InvalidLocatorTypeException("No such locator '"+locator+"' available. Try with id/name/className/cssSelector/linktext/partiallinktext/tagname/xpath");
		}
	}

}
