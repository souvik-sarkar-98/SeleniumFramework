package framework.automation.selenium.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;

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
			return Paths.get(res.toURI()).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
}
