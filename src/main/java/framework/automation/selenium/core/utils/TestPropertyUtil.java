package framework.automation.selenium.core.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Souvik Sarkar
 * @createdOn 25-Mar-2021
 * @purpose The main purpose of this class to read/write properties needed for test execution.
 */
public class TestPropertyUtil {
	
    private static final Logger LOG = LogManager.getLogger(TestPropertyUtil.class);

	/**
	 * @purpose  This method will load properties to system from the specified properties file.
	 * @date 27-Mar-2021
	 * @param fileName
	 * @throws IOException
	 */
	public static void loadFromFile(String filePath) throws IOException {
		LOG.traceEntry("Loading property file {}.", filePath);  
		FileReader reader=new FileReader(filePath);  
	    Properties prop=new Properties();
		prop.load(reader);
		for(Object key : prop.keySet()) {
	    	System.setProperty((String) key, prop.getProperty((String) key));
			LOG.debug("Property {} loaded with value {}.", (String) key ,System.getProperty((String) key));
	    }
	    LOG.traceExit("Property file {} loaded.",filePath);
	}
	/**
	 * @purpose This method will release properties from system which were loaded from the specified properties file
	 * @date 27-Mar-2021
	 * @param fileName
	 * @throws IOException
	 */
	public static void releaseFromSystem(String fileName) throws IOException {
		LOG.traceEntry("Releasing system properties which were loaded from {} file.",fileName);
		FileReader reader=new FileReader(fileName+".properties");  
	    Properties prop=new Properties();  
	    prop.load(reader);
	    for(Object key : prop.keySet()) {
	    	System.clearProperty((String) key);
			LOG.info("Property {} released.", (String) key);

	    }
	    LOG.traceExit("System properties released for {} file.",fileName);
	}

}
