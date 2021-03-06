package framework.automation.selenium.core.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import framework.automation.selenium.core.config.PropertyCache;
import framework.automation.selenium.core.exceptions.InvalidKeywordDataFormatException;
import framework.automation.selenium.core.exceptions.NoSuchTestFoundException;
import framework.automation.selenium.core.utils.ExcelUtils;
import framework.automation.selenium.core.utils.MiscUtils;

/**
 * @author Souvik Sarkar
 * @createdOn 27-Mar-2021
 * @purpose
 */
public final class TestDataHelper {
	private final Logger logger = LogManager.getLogger(this.getClass());
	private ExcelUtils excel;
	private String datasource;
	private Map<String, String> keywordProblems = new HashMap<String, String>();
	private List<String> testDataSheets;

	public TestDataHelper(Class<?> testClass) throws FileNotFoundException, InvalidFormatException, IOException {
		logger.traceEntry();
		// this.datasource=MiscUtils.getFilePath(testClass,
		// String.valueOf(PropertyCache.getProperty("DataSource")));
		this.datasource = String.valueOf(PropertyCache.getProperty("DataSource"));
		this.excel = new ExcelUtils(this.datasource);
		
		String keywordSheetName = String.valueOf(PropertyCache.getProperty("KeywordSheetName"));
		this.testDataSheets=this.excel.getSheets().stream().filter(f->!f.equalsIgnoreCase(keywordSheetName)).collect(Collectors.toList());
		logger.traceExit();

	}

	public String[] getKeywords(String testName) throws NoSuchTestFoundException {
		logger.traceEntry(" with parameter {}", testName);
		String sheetName = PropertyCache.getProperty("KeywordSheetName").toString();
		this.excel.setSheet(sheetName);
		String[] keywords = this.excel.getEntireRowValues(testName, 1, 0);
		if (keywords.length == 0) {
			NoSuchTestFoundException e = new NoSuchTestFoundException(
					"No test named '" + testName + "' found at '" + sheetName + "' sheet in '" + this.datasource);
			logger.error("Test Not Found", e);
			throw e;
		}

		logger.traceExit(Arrays.asList(keywords));
		return keywords;
	}

	public String getTestData(String data) throws InvalidKeywordDataFormatException {
		logger.traceEntry(" with parameter {}", data);
		//String[] sheetNames = PropertyCache.getProperty("TestDataSheetNames").toString().split(",");
		
		String retVal = null;
		for(String sheetName : testDataSheets) {
			//System.err.println(sheetName);
			this.excel.setSheet(sheetName);
			data = data.trim();
			String[] splits = data.split("-");
			logger.debug("Splitting data ... {}", Arrays.asList(splits));
			logger.debug("Splitted Data lenght "+splits.length);
			if(splits.length == 1) {
				retVal= MiscUtils.checkIfEnvVariable(data);
			}
			else if (splits.length == 2) {
				Object value=this.excel.getCellValue(splits[0], splits[1]);
				retVal = value== null ? null : String.valueOf(value);
			}
			else {
				logger.debug("Either Splitted data is==0");
				throw new InvalidKeywordDataFormatException(
						"Either the format of " + data + " is invalid or the pointed cell not exists. See below: "
								+ "\n\nPass data directly \n"
								+ "To fetch data from datasheet use ColumnHeader-RowHeader e.g. FirstName-5  \n");
			}
			if(retVal != null) {
				//System.out.println(retVal);
				break;
			}
			
		}
		
		logger.traceExit("returning {}", retVal);
		return retVal;

	}

	public void close() {
		logger.traceEntry();
		try {
			this.excel.release();
		} catch (IOException e) {
			logger.error("Exception while closing", e);
		}
		logger.traceExit();
	}

	public void reportKeywordProblems(String keyword, String problem) {
		logger.traceEntry();
		keywordProblems.put(keyword, problem);
		logger.traceExit();
	}

	/**
	 * @purpose
	 * @date 31-Mar-2021
	 */
	public void writeProblems() {
		logger.traceEntry();
		String sheetName = PropertyCache.getProperty("KeywordSheetName").toString();
		this.excel.setSheet(sheetName);
		this.excel.addCommentsByCellContent(PropertyCache.getProperty("TestName").toString(), keywordProblems);
		logger.traceExit();

	}

	public String getTestDataError(String data) {
		String sheetName = PropertyCache.getProperty("TestDataSheetName").toString();
		this.excel.setSheet(sheetName);
		data = data.trim();
		if ((data.startsWith("\"") || data.startsWith("'")) && (data.endsWith("\"") || data.endsWith("'"))) {
			return null;
		}
		Object retVal = null;
		String[] splits = data.split("-");
		if (splits.length > 1) {
			logger.debug("Splitted Data lenght >1");
			retVal = this.excel.getCellValue(splits[0], splits[1]);
		}

		if (retVal == null) {
			return "Either the format of " + data + " is invalid or the pointed cell not exists. See below: "
					+ "\n\nTo pass data directly use \" or ' before and after string e.g. 'Test' or \"Test\" \n"
					+ "To fetch data from datasheet use ColumnHeader-Index e.g. FirstName-5 \n";
		}
		return null;
	}

	/*
	 * public String getTestData(String data) throws
	 * InvalidKeywordDataFormatException {
	 * logger.traceEntry(" with parameter {}",data); String
	 * sheetName=PropertyCache.getProperty("TestDataSheetName").toString();
	 * this.excel.setSheet(sheetName); data=data.trim(); if((data.startsWith("\"")
	 * || data.startsWith("'")) && (data.endsWith("\"") || data.endsWith("'"))) {
	 * logger.debug("Direct Data found... ");
	 * data=data.replace("\"","").replace("'", "");
	 * logger.traceExit("returning {}",data); return data; } else { Object
	 * retVal=null; String[] splits=data.split("-");
	 * logger.debug("Splitting data ... {}",Arrays.asList(splits));
	 * if(splits.length>1) { logger.debug("Splitted Data lenght >1");
	 * retVal=this.excel.getCellValue(splits[0], splits[1]); } if(retVal==null) {
	 * logger.debug("Either Splitted data is==0"); InvalidKeywordDataFormatException
	 * e= new InvalidKeywordDataFormatException("Either the format of "
	 * +data+" is invalid or the pointed cell not exists. See below: "+
	 * "\n\nTo pass data directly use \" or ' before and after string e.g. 'Test' or \"Test\" \n"
	 * + "To fetch data from datasheet use ColumnHeader-Index e.g. FirstName-5 \n");
	 * throw e; } retVal=retVal.toString().replace("\"","").replace("'", "");
	 * logger.traceExit("returning {}",data); return retVal.toString(); } }
	 */
	
	

}
