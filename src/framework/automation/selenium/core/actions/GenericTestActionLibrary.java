package framework.automation.selenium.core.actions;

import java.util.Arrays;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
	
	public void set(WebElement element,String data) {
		element.sendKeys(data);;
	}
	
	public void clear(WebElement element) {
		element.clear();
		
	}
	
	public void submit(WebElement element) {
		element.submit();
		
	}
	public void select(WebElement element,String data) {
		Select select = new Select(element);
		if(select.isMultiple()) {
			String[] inpdata=data.split(",");
			for(String inp:inpdata) {
				int index = 0;
			    for (WebElement option : select.getOptions()) {
			        if (option.getText().trim().replace("\\s", "").equalsIgnoreCase(inp.trim().replace("\\s", "")))
			            break;
			        index++;
			    }
			    select.selectByIndex(index);
			}
		}else {
			int index = 0;
		    for (WebElement option : select.getOptions()) {
		        if (option.getText().trim().replace("\\s", "").equalsIgnoreCase(data.trim().replace("\\s", "")))
		            break;
		        index++;
		    }
		    select.selectByIndex(index);
		}
	}
	
	public void selectByVisibleText(WebElement element,String data) {
		Select select = new Select(element);
		if(select.isMultiple()) {
			String[] options=data.split(",");
			for(String option:options) {
				select.selectByVisibleText(option);
			}
		}else {
			select.selectByVisibleText(data);
		}
	}
	public void selectByIndex(WebElement element,String data) {
		Select select = new Select(element);
		if(select.isMultiple()) {
			String[] options=data.split(",");
			for(String option:options) {
				select.selectByVisibleText(option);
			}
		}else {
			select.selectByVisibleText(data);
		}
	}
	public void selectByValue(WebElement element,String data) {
		Select select = new Select(element);
		if(select.isMultiple()) {
			String[] options=data.split(",");
			for(String option:options) {
				select.selectByValue(option);
			}
		}else {
			select.selectByValue(data);
		}
	}
	
}
