package framework.automation.selenium.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class XMLUtil {

	private List<Document> documents=new ArrayList<>();
	private XPath xPath;

	public XMLUtil(String... filePaths)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		for(String filePath:filePaths) {
			Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
			doc.getDocumentElement().normalize();
			documents.add(doc);
		}
		this.xPath = XPathFactory.newInstance().newXPath();
	}

	public NodeList findNodeListByTags(String nodeTagName, String attributeName,String attributvalue, String expectedNodes)
			 {
		for(Document document:documents) {
			NodeList nodeList = document.getElementsByTagName(nodeTagName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (element.getAttribute(attributeName).equalsIgnoreCase(attributvalue)) {
						return element.getElementsByTagName(expectedNodes);
					}
				}
			}
		}
		return null;
	}

	public NodeList findNodeListByXpath(String xPath, String expectedNodes)
			throws XPathExpressionException, XMLElementNotFoundException {
		for(Document document:documents) {
			NodeList nodeList = (NodeList) this.xPath.compile(xPath).evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					return ((Element) node).getElementsByTagName(expectedNodes);
				}
			}
		}
		return null;
	}

}
