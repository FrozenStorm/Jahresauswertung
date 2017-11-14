import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFBorderFormatting;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts;


/**
 * @brief Schreibt Nummern Ueberschriften und Zahlen in ein Excelfile. Basiert auf Beispiel siehe Link
 * {@link} http://www.vogella.com/tutorials/JavaExcel/article.html
 * @author Daniel
 *
 */
public class WriteExcel {
	private XSSFCellStyle arial;
	private XSSFCellStyle arialBold;
	private XSSFCellStyle arialBold12;
	
	private int maxStringLength = 1;
	private int maxColumn;
	private int maxRow;
	
	private String outputFile;
	private XSSFSheet excelSheet;
	private FileOutputStream outputWorkbook;
	private XSSFWorkbook workbook;
	/**
	 * @brief Setzt den Pfad zum File welches beschrieben werden soll
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	/**
	 * @brief Erzeugt ein Excelfile am zuvor uebergebenen Pfad in der angegebenen Grösse und erzeugt das Objekt excelSheet auf welches geschrieben werden kann
	 * @param column
	 * @param row
	 * @throws IOException
	 * @throws WriteException
	 */
	public void initWrite(int column, int row) throws IOException{
		//Create Blank workbook
	    workbook = new XSSFWorkbook(); 
	    //Create file system using specific name
	    outputWorkbook = new FileOutputStream(new File(outputFile));
		workbook.createSheet("Report");
		excelSheet = workbook.getSheetAt(0);
		maxRow = row;
		maxColumn = column;
		for(int y=0 ; y<maxRow ; y++) // Create all Cells
		{
			excelSheet.createRow(y);
			for(int x=0 ; x<maxColumn ; x++)
			{
				excelSheet.getRow(y).createCell(x);
			}
		}
		createLabel();
	}
	/**
	 * @brief Schliesst das geoeffnete File
	 * @throws WriteException
	 * @throws IOException
	 */
	public void finishWrite() throws IOException{
		for(int x=0 ; x<maxColumn ; x++)
		{
			excelSheet.setColumnWidth(x,maxStringLength*256+100);
		}
		workbook.write(outputWorkbook);
		outputWorkbook.close();
	}
	/**
	 * @brief Erzeugt die verschiedenen Schriften welche später zum schreiben von ueberschriften und Text verwendet werden koennen
	 * @throws WriteException
	 */
	private void createLabel(){
		
		XSSFFont farial = workbook.createFont();
		farial.setFontName("arial");
		farial.setFontHeightInPoints((short) 10);
		
		XSSFFont farialBold = workbook.createFont();
		farial.setFontName("arialBold");
		farial.setFontHeightInPoints((short) 10);
		farialBold.setBold(true);
		
		XSSFFont farialBold12 = workbook.createFont();
		farialBold12.setFontName("arialBold12");
		farialBold12.setFontHeightInPoints((short) 20);
		farialBold12.setBold(true);
		farialBold12.setBold(true);
		
		
		arial = workbook.createCellStyle();
		arial.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		arial.setBottomBorderColor(
		IndexedColors.BLACK.getIndex());
		arial.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		arial.setLeftBorderColor( 
		IndexedColors.BLACK.getIndex());
		arial.setBorderRight(XSSFCellStyle.BORDER_THIN);
		arial.setRightBorderColor( 
		IndexedColors.BLACK.getIndex());
		arial.setBorderTop(XSSFCellStyle.BORDER_THIN);
		arial.setTopBorderColor( 
		IndexedColors.BLACK.getIndex());
		arial.setWrapText(false);
		arial.setFont(farial);
		
		
		
		arialBold = (XSSFCellStyle)arial.clone();
		arialBold.setFont(farialBold);
		arialBold.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index );
		arialBold.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		
		
		
		arialBold12 = (XSSFCellStyle)arialBold.clone();
		arialBold12.setFont(farialBold12);
		arialBold12.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index );
		arialBold12.setAlignment(HorizontalAlignment.CENTER);
	}
	/**
	 * @brief Fuegt einen Titel hinzu
	 * @param column
	 * @param row
	 * @param s
	 * @param StartCol
	 * @param StartRow
	 * @param DestCol
	 * @param DestRow

	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void addTitle(int column, int row, String s, int StartCol, int StartRow, int DestCol, int DestRow){
		excelSheet.addMergedRegion(new CellRangeAddress(StartRow,DestRow,StartCol,DestCol));
		excelSheet.getRow(row).getCell(column).setCellValue(s);
		for(int x=0 ; x<maxColumn ; x++)
		{
			excelSheet.getRow(row).getCell(x).setCellStyle(arialBold12);
		}
	}
	/**
	 * @brief Fuegt eine ueberschrift hinzu
	 * @param column
	 * @param row
	 * @param s
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void addCaption(int column, int row, String s){
		if(s!=null)if(s.length()>maxStringLength) maxStringLength = s.length();
		excelSheet.getRow(row).getCell(column).setCellValue(s);
		excelSheet.getRow(row).getCell(column).setCellStyle(arialBold);
	}
	/**
	 * @brief Fuegt eine Zahl hinzu
	 * @param column
	 * @param row
	 * @param integer
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void addNumber(int column, int row, Integer integer){
		excelSheet.getRow(row).getCell(column).setCellValue(integer);
		excelSheet.getRow(row).getCell(column).setCellStyle(arial);
	}
	/**
	 * @brief Fuegt ein Textfeld hinzu
	 * @param column
	 * @param row
	 * @param s
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void addLabel(int column, int row, String s){
		if(s!=null)if(s.length()>maxStringLength) maxStringLength = s.length();
		excelSheet.getRow(row).getCell(column).setCellValue(s);
		excelSheet.getRow(row).getCell(column).setCellStyle(arial);
	}
} 