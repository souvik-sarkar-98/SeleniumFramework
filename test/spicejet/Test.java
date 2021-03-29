package spicejet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.InvalidKeywordDataFormatException;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.exceptions.XMLElementNotFoundException;
import framework.automation.selenium.core.helpers.TestDataHelper;
import framework.automation.selenium.core.utils.ExcelUtils;
import framework.automation.selenium.core.utils.XMLUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 28-Mar-2021
 * @purpose 
 */
public class Test {

	/**
	 * @purpose 
	 * @date 28-Mar-2021
	 * @param args
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws FileNotFoundException 
	 * @throws NoSuchTestFoundException 
	 * @throws InvalidKeywordDataFormatException 
	 */
	public static void main(String[] args) throws Exception{
		abc("abc");
	}
	static void abc(String... a) {
		System.out.println(a[0]);
		System.out.println(a[1]);
		System.out.println(a[2]);
		
	}
	static void prop() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		 new PropertyCache("test-resources/spicejet/login/property.xml");  
		 PropertyCache.setProperty("TestName","Souvik");
		 PropertyCache.setProperty("TestId",56566);
		;
		 System.out.println( PropertyCache.getProperties("Defaultbrowser"));
		 System.out.println( PropertyCache.getProperty("DefaultWait"));
		 System.out.println( PropertyCache.getProperty("Testname"));
	}
	
	static void reflectionTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = By.class.getMethod("ID", String.class);
		Object o = method.invoke(null, "hugdksahd");
		System.out.println(o);
	}
	
	
	static void XMLUtilTest() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, XMLElementNotFoundException {
		XMLUtil u=new XMLUtil("test-resources/spicejet/login/property.xml");
		//NodeList nodes=u.findNodeListByTags("repository","name","loginPage","path");
		NodeList nodes=u.findNodeListByXpath("//properties", "value");
		//XMLUtil p=new XMLUtil(nodes.item(0).getTextContent());
		//nodes=p.findNodeListByTags("object","name","username","identifier");
		for(int i=0;i<nodes.getLength();i++) {
			Element elem=(Element)nodes.item(i);
			//System.out.println(elem.getAttribute("locator"));;
			//System.out.println(elem.getAttribute("value"));;
			System.out.println(nodes.item(i).getTextContent());
		}
	}
	
	
	static void ExcelUtilTest() throws FileNotFoundException, InvalidFormatException, IOException {
		ExcelUtils u=new ExcelUtils("C:\\Users\\Souvik Sarkar\\Desktop\\a.xlsx");
		u.setSheet("TestData");
//		Object[] o=u.getEntireRowValues("TestCase_0055", 1, 0);
//		System.out.println(o);
//		Object[] o1=u.getEntireColumnValues("OM5P", 1, 0);
//		System.out.println(Arrays.asList(o1));
		System.out.println(u.getCellValue("username", "2"));
	}
	
	static void TestDataHelperTest() throws FileNotFoundException, InvalidFormatException, IOException, NoSuchTestFoundException, InvalidKeywordDataFormatException {
		System.setProperty("test.current.datasource","test-resources/data/DefaultTestSuite.xlsx");
		System.setProperty("test.current.datasource.keywordsheet.name","Keywords");
		System.setProperty("test.current.datasource.datasheet.name","TestData");
		TestDataHelper t=new  TestDataHelper();
		//System.out.println(Arrays.asList(t.getKeywords("testcase_001")));
		System.out.println(t.getTestData("URL-2"));
		
		;
	}

}
