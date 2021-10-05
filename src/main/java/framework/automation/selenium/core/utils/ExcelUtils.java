package framework.automation.selenium.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Souvik Sarkar
 * @createdOn 25-Mar-2021
 * @purpose :
 */
public class ExcelUtils {
	private static final Logger log = LogManager.getLogger(ExcelUtils.class);
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private DataFormatter df;
	private File workbookFile;

	/**
	 * @param workbookPath
	 * @param keywordSheetName
	 * @param dataSheetName
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	public ExcelUtils(String workbookUrl)
			throws FileNotFoundException, IOException, InvalidFormatException {
		log.traceEntry("with {}",workbookUrl);
		this.workbookFile=new File(workbookUrl);
		OPCPackage ofile = OPCPackage.open(this.workbookFile);
		this.workbook = new XSSFWorkbook(ofile);
		//ofile.close();
		this.df = new DataFormatter();
		
		log.traceExit();
	}

	public Object[] getEntireRowValues(String rowHeaderText, int columnPickupStartIndex, int headerIndex) {
		log.traceEntry();
		ArrayList<Object> steps = new ArrayList<Object>();
		int rowInd=this.getRowHeaderIndex(rowHeaderText, headerIndex);
		if(rowInd>=0) {
			Iterator<Cell> col =  this.sheet.getRow(rowInd).cellIterator();
			while (col.hasNext()) {
				Cell nextCol = col.next();
				if (nextCol.getColumnIndex() >= columnPickupStartIndex) {
					steps.add(this.df.formatCellValue(nextCol));
				}
			}
		}
		log.traceExit();
		return steps.toArray();
	}

	public Object[] getEntireColumnValues(String columnHeaderText, int rowPickupStartIndex,int headerIndex) {
		log.traceEntry();
		ArrayList<Object> value = new ArrayList<Object>();
		int coldInd=this.getColumnHeaderIndex(columnHeaderText, headerIndex);
		if(coldInd>=0) {
			Iterator<Row> row = this.sheet.iterator();
			while (row.hasNext()) {
				Row nextRow = row.next();
				if (nextRow.getRowNum() >= rowPickupStartIndex) {
					value.add(this.df.formatCellValue(nextRow.getCell(coldInd)));
				}
			}
		}
		
		log.traceExit();
		return value.toArray();
	}

	public Object getCellValue(String columnHeader, String rowHeader) {
		log.traceEntry();
		int rowInd=this.getRowHeaderIndex(rowHeader, 0);
		int colInd=this.getColumnHeaderIndex(columnHeader, 0);
		//System.out.println(rowInd+","+colInd);
		if(rowInd>=0 && colInd>=0)
			return this.df.formatCellValue(this.sheet.getRow(rowInd).getCell(colInd));
		log.traceExit();
		return null;
	}

	private int getColumnHeaderIndex(String headerName,int headerRow) {
		int index=-1;
		Iterator<Cell> iterator = this.sheet.getRow(headerRow).iterator();
		while (iterator.hasNext()) {
			Cell nextCell = iterator.next();
			//System.out.println(headerName.equalsIgnoreCase(Objects.toString(this.df.formatCellValue(nextCell),"")));
			//System.out.println(nextCell.getColumnIndex());
			if (headerName.equalsIgnoreCase(Objects.toString(this.df.formatCellValue(nextCell),""))) {
				index = nextCell.getColumnIndex();
				break;
			}
		}
		return index;
	}
	
	private int getRowHeaderIndex(String headerName,int headerCol) {
		int index=-1;
		Iterator<Row> iterator = this.sheet.iterator();
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			//System.out.println(headerName+"   "+nextRow.getLastCellNum());
			//System.out.println(headerName.equalsIgnoreCase(Objects.toString(this.df.formatCellValue(nextRow.getCell(headerCol)), "")));
			//System.out.println(nextRow.getRowNum());
			if (headerName.equalsIgnoreCase(Objects.toString(this.df.formatCellValue(nextRow.getCell(headerCol)), ""))) {
				index=nextRow.getRowNum();
				break;
			}
		}
		return index;
	}

	public void release() throws IOException {
        //FileOutputStream os = new FileOutputStream(this.workbookFile);
		//this.workbook.write(os);
		this.workbook.close();
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheetName) {
		this.sheet = this.workbook.getSheet(sheetName);
	}
	
	public void addCommentsByCellContent(String rowHeaderText,Map<String, String> comments) {
		CreationHelper factory = this.workbook.getCreationHelper();
		XSSFRow row=this.sheet.getRow(this.getRowHeaderIndex(rowHeaderText, 0));
		Iterator<Cell> iterator = row.iterator();
		while (iterator.hasNext()) {
			Cell nextCell = iterator.next();
			if (comments.containsKey(nextCell.getStringCellValue())) {
				Drawing<?> drawing = this.sheet.createDrawingPatriarch();
				ClientAnchor anchor = factory.createClientAnchor();
				anchor.setCol1(nextCell.getColumnIndex());
				anchor.setCol2(nextCell.getColumnIndex()+1);
				anchor.setRow1(row.getRowNum());
				anchor.setRow2(row.getRowNum()+3);
				Comment comment = drawing.createCellComment(anchor);
				RichTextString str = factory.createRichTextString("Hello, World!");
				comment.setString(str);
				comment.setAuthor("Auto Bot");
				nextCell.setCellComment(comment);
			}
		}
	}
	
	
	
	

}
