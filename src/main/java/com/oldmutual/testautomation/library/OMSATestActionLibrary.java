package com.oldmutual.testautomation.library;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class OMSATestActionLibrary {
	//private final WebDriver driver;
	private final Logger logger = LogManager.getLogger(this.getClass());

	public OMSATestActionLibrary(WebDriver driver) {
		logger.traceEntry("with driver {}", driver);
		//this.driver = driver;
		logger.traceExit();
	}
	public void uploadFile(String fileUrl) throws AWTException {
		Robot rb=new Robot();
		// copying File path to Clipboard
	    StringSelection str = new StringSelection(fileUrl);
	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
	     // press Contol+V for pasting
	     rb.keyPress(KeyEvent.VK_CONTROL);
	     rb.keyPress(KeyEvent.VK_V);
	 
	    // release Contol+V for pasting
	    rb.keyRelease(KeyEvent.VK_CONTROL);
	    rb.keyRelease(KeyEvent.VK_V);
	 
	    // for pressing and releasing Enter
	    rb.keyPress(KeyEvent.VK_ENTER);
	    rb.keyRelease(KeyEvent.VK_ENTER);
		

	}

	

}
