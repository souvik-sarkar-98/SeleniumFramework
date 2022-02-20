package framework.automation.selenium.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;



/**
 * @author Souvik Sarkar
 * @createdOn 25-Mar-2021
 * @purpose :
 */
public class TestReportUtil {

	public String createNewWord(String tempPath, String filePath, String fileName) throws InvalidFormatException, IOException {
		XWPFDocument createNewWordDocument = new XWPFDocument();
		XWPFRun createNewWordRun = createNewWordDocument.createParagraph().createRun();
		File[] files = new File(tempPath).listFiles();
		sortFiles(files);

		for (int i = 0; i < files.length; i++) {
			InputStream pic;
			pic = new FileInputStream(files[i].getPath());

			createNewWordRun.setText(files[i].getName().replace(".png", ""));
			createNewWordRun.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, files[i].getName(), Units.toEMU(500),
					Units.toEMU(300));
			pic.close();
			if (i != files.length - 1) {
				if (i % 2 == 0)
					createNewWordRun.addBreak();
				else
					createNewWordRun.addBreak(BreakType.PAGE);
			}
		}
		FileOutputStream createNewWordOut = new FileOutputStream(filePath+"/"+fileName+".docx");
		createNewWordDocument.write(createNewWordOut);
		createNewWordOut.flush();
		createNewWordOut.close();
		createNewWordDocument.close();
		return new File(filePath+"/"+fileName+".docx").getAbsolutePath();
	}

	private void sortFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});
	}

}
