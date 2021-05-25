package framework.automation.selenium.core.exceptions;

/**
 * @author Souvik Sarkar
 * @createdOn 28-Mar-2021
 * @purpose 
 */
public class NoSuchTestFoundException extends Exception{

private static final long serialVersionUID = 1L;
	
	public NoSuchTestFoundException(String message) {
		super(message);
	}
}
