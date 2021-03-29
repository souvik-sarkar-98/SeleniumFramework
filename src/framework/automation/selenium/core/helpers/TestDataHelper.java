package framework.automation.selenium.core.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.InvalidKeywordDataFormatException;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.utils.ExcelUtils;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose 
 */
public class TestDataHelper {
	private ExcelUtils excel;
	private String datasource;
	public TestDataHelper() throws FileNotFoundException, InvalidFormatException, IOException {
		this.datasource=String.valueOf(PropertyCache.getProperty("DataSource"));
		this.excel= new ExcelUtils(this.datasource);
	}
	
	public Object[] getKeywords(String testName) throws NoSuchTestFoundException {
		String sheetName=PropertyCache.getProperty("KeywordSheetName").toString();
		this.excel.setSheet(sheetName);
		Object[] keywords=this.excel.getEntireRowValues(testName, 1, 0);
		if(keywords.length==0) {
			throw new NoSuchTestFoundException("No test named '"+testName+"' found at '"+sheetName+"' sheet in '"+this.datasource);
		}
		return keywords;
	}
	
	public String getTestData(String data) throws InvalidKeywordDataFormatException {
		String sheetName=PropertyCache.getProperty("TestDataSheetName").toString();
		this.excel.setSheet(sheetName);
		data=data.trim();
		if((data.startsWith("\"") || data.startsWith("'")) && (data.endsWith("\"") || data.endsWith("'"))) {
			return data.replace("\"","").replace("'", "");
		}
		else {
			Object retVal=null;
			String[] splits=data.split("-");
			if(splits.length>1) {
				retVal=this.excel.getCellValue(splits[0], splits[1]);
			}
			if(retVal==null) {
				throw new InvalidKeywordDataFormatException("Either the format of "+data+" is invalid or the pointed cell not exists. See below: "+
			"\n\nTo pass data directly use \" or ' before and after string e.g. 'Test' or \"Test\" \n"+
			"To fetch data from datasheet use ColumnHeader-Index e.g. FirstName-5 \n");
			}
			return retVal.toString().replace("\"","").replace("'", "");
		}
	}
	
	public void close() {
		try {
			this.excel.release();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			///////////////////// add logger here
			e.printStackTrace();
		}
	}

}


