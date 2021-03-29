package framework.automation.selenium.core.exceptions;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose 
 */
public class BrowserNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public BrowserNotFoundException(String message) {
		super(message);
	}

}
