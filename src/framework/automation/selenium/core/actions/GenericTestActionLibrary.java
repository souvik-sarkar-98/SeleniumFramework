package framework.automation.selenium.core.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose 
 */
public class GenericTestActionLibrary {
	protected final WebDriver driver;
	
	public GenericTestActionLibrary(WebDriver driver) {
		this.driver=driver;
	}
	
	public void openUrl(String url) {
		this.driver.get(url);
		
	}
	
	public void click(WebElement element) {
		element.click();
	}
	
	public void jsClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
	}
	
}
