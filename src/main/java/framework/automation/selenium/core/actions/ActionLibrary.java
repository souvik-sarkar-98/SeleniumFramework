package framework.automation.selenium.core.actions;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.javafaker.Faker;

import framework.automation.selenium.core.config.PropertyCache;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public class ActionLibrary {
	private final WebDriver driver;
	private final Logger logger = LogManager.getLogger(this.getClass());
	private Actions actions;

	public ActionLibrary(WebDriver driver) {
		logger.traceEntry("with driver {}", driver);
		this.driver = driver;
		this.actions=new Actions(driver);
		logger.traceExit();
	}
	
	/*
	 * URL Actions
	 */

	public void openUrl(String url) {
		logger.traceEntry("with {}", url);
		this.driver.get(url);
		logger.traceExit();
	}
	
	
	/*
	 * Click Actions
	 */

	public void click(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		WebDriverWait wait = new WebDriverWait(driver,
				Integer.parseInt(PropertyCache.getProperty("ExplicitWait").toString()));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		//this.actions.clickAndHold(element).build().perform();
		logger.traceExit();

	}

	public void jsClick(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
		logger.traceExit();
	}
	
	
	
	/*
	 * Set Actions
	 */

	public void set(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		element.sendKeys(data);
		logger.traceExit();
	}
	
	public void setNew(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		element.clear();
		element.sendKeys(data);
		logger.traceExit();
	}
	
	public void setRandom(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		element.clear();
		element.sendKeys(getRandomValue(data));
		logger.traceExit();
	}
	
	
	/*
	 * Select Actions
	 */

	public void select(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		Select select = new Select(element);
		if (select.isMultiple()) {
			logger.debug("Multiple selection applicable.");
			String[] inpdata = data.split(",");
			for (String inp : inpdata) {
				int index = 0;
				for (WebElement option : select.getOptions()) {
					if (option.getText().trim().replace("\\s", "").equalsIgnoreCase(inp.trim().replace("\\s", ""))) {
						logger.debug("Selected value '{}' from dropdown at index '{}'", option.getText(), index);
						break;
					}
					index++;
				}
				select.selectByIndex(index);
			}
		} else {
			int index = 0;
			for (WebElement option : select.getOptions()) {
				if (option.getText().trim().replace("\\s", "").equalsIgnoreCase(data.trim().replace("\\s", ""))) {
					logger.debug("Selected value '{}' from dropdown at index '{}'", option.getText(), index);
					break;
				}
				index++;
			}
			select.selectByIndex(index);
		}
		logger.traceExit();
	}

	public void selectByVisibleText(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		Select select = new Select(element);
		if (select.isMultiple()) {
			String[] options = data.split(",");
			for (String option : options) {
				select.selectByVisibleText(option);
				logger.debug("Selected value  by visible text '{}' from dropdown", option);
			}
		} else {
			select.selectByVisibleText(data);
			logger.debug("Selected value by visible text '{}' from dropdown", data);
		}
		logger.traceExit();
	}

	public void selectByIndex(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
//		WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(PropertyCache.getProperty("ExplicitWait").toString()));
//		 wait.until(ExpectedConditions.elementToBeSelected(element));
		Select select = new Select(element);
		if (select.isMultiple()) {
			String[] options = data.split(",");
			for (String option : options) {
				select.selectByIndex(Integer.parseInt(option));
				logger.debug("Selected value from dropdown at index '{}'", option);
			}
		} else {
			select.selectByIndex(Integer.parseInt(data));
			logger.debug("Selected value from dropdown at index '{}'", data);
		}
		logger.traceExit();
	}

	public void selectByValue(WebElement element, String data) {
		logger.traceEntry("with {},{}", element.toString(), data);
		// WebDriverWait wait = new WebDriverWait(driver,
		// Integer.parseInt(PropertyCache.getProperty("ExplicitWait").toString()));
		// wait.until(ExpectedConditions.elementToBeSelected(element));
		Select select = new Select(element);
		if (select.isMultiple()) {
			String[] options = data.split(",");
			for (String option : options) {
				select.selectByValue(option);
				logger.debug("Selected value from dropdown by Value '{}'", option);
			}
		} else {
			select.selectByValue(data);
			logger.debug("Selected value from dropdown by Value '{}'", data);
		}
		logger.traceExit();
	}

	public void jsSelect(WebElement element, String data) {
		data = data.replaceAll("\\s+", "").toUpperCase();
		((JavascriptExecutor) driver).executeScript(
				"var select = arguments[0]; for(var i = 0; i < select.options.length; i++){ if(select.options[i].text.replace(/\\s+/g, '').toUpperCase() === arguments[1]){ select.options[i].selected = true; } }",
				element, data);
	}
	
	/*
	 * Misc Actions
	 */
	
	public void clear(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		element.clear();
		logger.traceExit();
	}

	public void submit(WebElement element) {
		logger.traceEntry("with {}", element.toString());
		element.submit();
		logger.traceExit();
	}

	public void wait(String wait) {
		logger.traceEntry("with {}", wait);
		try {
			Thread.sleep(Integer.parseInt(wait) * 1000);
		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
		}
		logger.traceExit();

	}
	
	public void wait(int wait) {
		logger.traceEntry("with {}", wait);
		try {
			Thread.sleep(wait * 1000);
		} catch ( InterruptedException e) {
			e.printStackTrace();
		}
		logger.traceExit();

	}

	public void switchWindow() {
		logger.traceEntry();
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		//wait(2);
		driver.close();
		//wait(2);
		driver.switchTo().window(tabs2.get(tabs2.size() - 1));
		//driver.switchTo().frame(tabs2.get(tabs2.size() - 1));
		//wait(2);		driver.manage().window().maximize();
		
		// driver.switchTo().window(tabs2.get(0)).close();;
		logger.traceExit();

	}
	
	public void check(WebElement element) {
		logger.traceEntry("with {}", element);
		actions.moveToElement(element).build().perform();
		wait(1);
		logger.traceExit();

	}
	
	public String storeInnerText(WebElement element,String key) {
		logger.traceEntry();
		PropertyCache.setProperty(key, element.getText());
		logger.traceExit();
		return element.getText();

	}
	
	/*
	 * Helpers methods
	 */
	
	private String getRandomValue(String type) {
		String[] args=type.toLowerCase().trim().split(",");
		Faker faker=new Faker();
		switch(args[0]) {
		case "randomtitle":
			return faker.name().prefix();
		case "randomfirstname":
			return faker.name().firstName();
		case "randommiddlename":
			String[] name=faker.name().nameWithMiddle().split(" ");
			return name.length > 1 ? name[1] : faker.name().firstName();
		case "randomlastname":
			return faker.name().lastName();
		case "randomfullname":
			return faker.name().fullName();
		case "randomemail":
			if(args.length>1) {
				return faker.bothify(/*"??????????##@"+*/args[1]);
			}
			return faker.internet().emailAddress();
		case "randomphonenumber":
			if(args.length>1) {
				return faker.number().digits(Integer.parseInt(args[1]));
			}
			return faker.phoneNumber().phoneNumber();
		case "randomstring":
			if(args.length>1) {
				return faker.lorem().fixedString(Integer.parseInt(args[1]));
			}
			return faker.letterify("?????????????");
		case "randomcity":
			return faker.address().cityName();
		case "randomsentence":
			return faker.lorem().sentence();
		default:
			return type;
		}
	}
	
	

}
