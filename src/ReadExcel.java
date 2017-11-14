import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @brief Liest Excelzellen aus. Basiert auf Beispiel siehe Link
 * {@link} http://www.vogella.com/tutorials/JavaExcel/article.html
 * @author Daniel
 *
 */
public class ReadExcel {
	private String inputFile;
	private XSSFSheet sheet;
	private FileInputStream inputWorkbook;
	private XSSFWorkbook w;
	/**
	 * @brief Setzt den Pfad von welchem gelesen werden soll
	 * @param inputFile
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	/**
	 * @brief oeffnet das File am zuvor uebergebenen Pfad und erzeugt das sheet Objekt ueber welches gelesen werden kann
	 * @throws IOException
	 */
	public void initRead() throws IOException  {
		inputWorkbook = new FileInputStream(inputFile);
	    // Finds the workbook instance for XLSX file
	    w = new XSSFWorkbook (inputWorkbook);
	   
	    // Return first sheet from the XLSX workbook
	    sheet = w.getSheetAt(0);
	}
	/**
	 * @brief Gibt die Anzahl von Zeilen zurueck welche das Excel hat
	 * @return
	 */
	public int getRows() {
		return sheet.getLastRowNum();
	}
	/**
	 * @brief Gibt den Inhalt der Zelle zurueck sofern der uebergebene Zellentyp mit dem gelesenen uebereinstimmt
	 * @param x
	 * @param y
	 * @param ct
	 * @return
	 */
	public String readCell(int x, int y, int ct){
		XSSFCell cell = sheet.getRow(y).getCell(x);
		int type = cell.getCellType();
		
		if (type == ct) {
			if(type == XSSFCell.CELL_TYPE_NUMERIC){
				System.out.println("Zeile:" + Integer.toString(y) + " Spalte:" + Integer.toString(x) + " : " + cell.getNumericCellValue());
				return Integer.toString((int)cell.getNumericCellValue());
			}
			else if(type == XSSFCell.CELL_TYPE_STRING){
				System.out.println("Zeile:" + Integer.toString(y) + " Spalte:" + Integer.toString(x) + " : " + cell.getStringCellValue());
				return cell.getStringCellValue();
			}
		}
		return null;
	}
} 