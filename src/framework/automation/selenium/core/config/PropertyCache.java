package framework.automation.selenium.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.utils.XMLUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 29-Mar-2021
 * @purpose 
 */
public class PropertyCache {
	static {
		System.setProperty("log4j.configurationFile",PropertyCache.class.getResource("log4j2.xml").toString());
		//System.setProperty("log4j.configurationFile",PropertyCache.class.getPackageName().replace(".", "/")+"/log4j2.xml");
	}

	private static XMLUtil prop;
	private static Map<String,Object> runtime=new HashMap<String,Object>();
    private static final Logger logger = LogManager.getLogger(PropertyCache.class);

	/**
	 * @param filePath (XML)
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public PropertyCache(String filePath) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		logger.always();
		PropertyCache.prop=new XMLUtil(filePath);
		
	}

	/**
	 * @purpose 
	 * @date 29-Mar-2021
	 * @param string
	 * @return
	 */
	public static Object getProperty(String key) {
		Object value=null;
		NodeList node=PropertyCache.prop.findNodeListByTags("property", "name", key, "value");
		value=PropertyCache.runtime.get(key.toLowerCase());
		if(node!=null && value==null) {
			value=node.item(0).getTextContent();
		}
		return value;
	}
	
	public static Object[] getProperties(String key) {
		List<Object> obj=new ArrayList<Object>();
		NodeList node=PropertyCache.prop.findNodeListByTags("property", "name", key, "value");
		if(node==null) return null;
		for(int i=0;i<node.getLength();i++) {
			obj.add(node.item(i).getTextContent());
		}
		return obj.toArray(new Object[obj.size()]);
	}
	
	public static void setProperty(String key,Object value) {
			PropertyCache.runtime.put(key.toLowerCase(), value);
	}

}
