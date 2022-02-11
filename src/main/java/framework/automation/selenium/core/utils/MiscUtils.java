package framework.automation.selenium.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;

/**
 * @author Souvik Sarkar
 * @createdOn 25-May-2021
 * @purpose
 */
public class MiscUtils {
	

	public static String getFilePath(Class<?> resourceClass, String fileName) {
		URL res = resourceClass.getResource(fileName);
		
		try {
			if(res == null) {
				throw new FileNotFoundException("No such file "+fileName+" found under class path "+resourceClass);
			}
			//hello
			return Paths.get(res.toURI()).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Created due to removing class path dependency at 11/02/2022
	 */
	public static String getFilePath(String filePath) throws FileNotFoundException {
		File file =new File(filePath);
		if(!file.exists()) {
			throw new FileNotFoundException("No such file found at "+filePath);
		}
		return file.getAbsolutePath();
	}

	public static File getFile(Class<?> resourceClass, String fileName) {
		URL res = resourceClass.getResource(fileName);
		try {
			return Paths.get(res.toURI()).toFile();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static String addLinebreaks(String input, int maxLineLength) {
	    StringTokenizer tok = new StringTokenizer(input, " ");
	    StringBuilder output = new StringBuilder(input.length());
	    int lineLen = 0;
	    while (tok.hasMoreTokens()) {
	        String word = tok.nextToken();
	        word+=" ";
	        if (lineLen + word.length() > maxLineLength) {
	            output.append("\n");
	            lineLen = 0;
	        }
	        output.append(word);
	        lineLen += word.length();
	    }
	    return output.toString();
	}
	
	public static Level getLogLevel(String level) {
		switch(level.toUpperCase()){
		case "ALL":
			return Level.ALL;
		case "TRACE":
			return Level.TRACE;
		case "DEBUG":
			return Level.DEBUG;
		case "INFO":
			return Level.INFO;
		case "WARN":
			return Level.WARN;
		case "ERROR":
			return Level.ERROR;
		case "FATAL":
			return Level.FATAL;
		case "OFF":
			return Level.OFF;
		default:
			return Level.INFO;	
		}
	}
}
