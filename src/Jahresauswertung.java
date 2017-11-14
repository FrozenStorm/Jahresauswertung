import java.awt.Button;


import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFCell;

import jxl.CellType;
import jxl.write.WriteException;

/* Aus Rennprogramm mit Excel ESH2 exportiert und gespeichert als 97_Arbeitsmappe (.xls)
 * 
 * 
 */

/**
 * @brief Wandelt Rennresultate in einem Ordner welche mit Excel ESH2 export aus dem Rennprogramm exportiert wurden und als .xlsx gespeichert
 * 		  sind in eine Jahresrangliste um. Der Dateiname muss folgenden Aufbau haben: Jahr_Klasse_Laufnummer_Austragungsort.xls
 * 		  Mit der Anzahl Laeufe koennen die in diesem Jahr zu fahrenden Laeufe eingestellt werden anhand welcher entschieden wird wie viele
 * 		  Laeufe gewertet werden. Die Jahresrangliste wird in den gleichen Ordner gespeichert aus welchem die Daten der Rennresultate stammen.
 * @author Daniel
 *
 */
public class Jahresauswertung extends Frame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static int T_NACHNAME = 2;
	private static int T_VORNAME = 3;
	private static int T_START = 1;
	private static int T_RANG = 0;
	//private static int T_SEC = 3;
	//private static int T_IDC = 4;
	//private static int T_SM = 2;
	//private static int T_JUNIOR_SENIOR = 1;
	private static int[] Punktetabelle = {100,90,82,74,66,60,54,50,46,42,38,36,34,32,30,28,26,24,22,20,18,16,14,12,10,8,6,4,2,1};
	private static int[] ZuWertendeLaeufe = {1,2,3,3,4,4,5,5,6,6,7,7};
	private Button B_Pfad = new Button("Öffnen");
	private Button B_Ok = new Button("Ok");
	private Button B_End = new Button("Beenden");
	private TextField TF_Pfad = new TextField(new java.io.File(".").getAbsolutePath()); // Default Pfad fuers Tests
	private TextField TF_AnzahlRennen = new TextField("1");
	private Label L_Pfad = new Label("Pfad zu den zu wertenden Rennresultaten");
	private Label L_AnzahlRennen = new Label("Gesamtanzahl Laeufe dieses Jahr");
	private JFileChooser chooser;
	private Jahresrangliste myJahresrangliste;
	/**
	 * @brief Startet GUI
	 * @param args
	 */
	public static void main(String[] args) {
	    new Jahresauswertung("Jahresauswertung");
	}
	/**
	 * @brief Initialisiert GUI
	 * @param Title
	 */
	public Jahresauswertung(String Title) {

		super(Title);
		setSize(900,120);
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		//Erste Zeile des GUI
		 
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbl.setConstraints(L_Pfad, gbc);
		add(L_Pfad);
		
		gbc.gridwidth = 3;
		gbl.setConstraints(TF_Pfad, gbc);
		add(TF_Pfad);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(B_Pfad, gbc);
		add(B_Pfad);
		
		//Zweite Zeile des GUI
		
		gbc.gridwidth = 1;
		gbl.setConstraints(L_AnzahlRennen, gbc);
		add(L_AnzahlRennen);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(TF_AnzahlRennen, gbc);
		add(TF_AnzahlRennen);
		
		// Dritte Zeile des GUI
		
		gbc.gridwidth = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbl.setConstraints(new Label(""), gbc);
		add(new Label(""));
		
		gbc.gridwidth = 1;
		gbl.setConstraints(B_Ok, gbc);
		add(B_Ok);
		
		gbc.gridwidth = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(B_End, gbc);
		add(B_End);
		
		// Event zuweisungen
		
		B_Pfad.addActionListener(this);
		B_Ok.addActionListener(this);
		B_End.addActionListener(this);
		
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e) {
			System.exit(0);
		}});
		
		this.setResizable(false);
		this.setVisible(true);
	}
	/**
	 * @brief Empfaengt Ereignis wenn ein Buton gedrueckt wird
	 * @param ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == B_Pfad){
		    choosePfad();
		}
		if(e.getSource() == B_Ok){
			createJahresrangliste();
		}
		if(e.getSource() == B_End){
			System.exit(0);
		}
	}
	/**
	 * @brief Erstellt Jahresrangliste aus den Daten welche im Pfad des im TF_Pfad gespeicherten Pfades liegen
	 */
	public void createJahresrangliste(){
		myJahresrangliste = new Jahresrangliste();
		ParsedFile myParsedFile = new ParsedFile();
		String Pfad = null;
		String filepath = null;
		
		try
		{
			// Daten aus Files lesen
			File[] fileList = getFileList(TF_Pfad.getText());
	
			if(fileList.length > 0)
			{
				for(File file : fileList) {
		            Pfad = file.getParentFile().getAbsolutePath();
		            filepath = file.getAbsolutePath();
		            System.out.println("Dateinamen: " + filepath);
		            readRennresultat(filepath,myJahresrangliste);
		        }
				
				// Rennjahr, Klasse aus Dateinamen extrahieren
				myParsedFile = ParseFileName(filepath);
				
				// Daten in File schreiben
				if(myJahresrangliste.Laeufe.size()>0)
				{
					System.out.println("Resultate:...................................................................");
					if(!TF_AnzahlRennen.getText().isEmpty()) {
						myJahresrangliste.setAnzahlGewertete(getAnzahlGewertete(Integer.parseInt(TF_AnzahlRennen.getText())));
						System.out.println("Anzahl zu Wertende Laeufe = " + getAnzahlGewertete(Integer.parseInt(TF_AnzahlRennen.getText())));
						System.out.println("Ausgabepfad: " + Pfad + "\\" + Integer.toString(myParsedFile.Jahr) + "_" + myParsedFile.Klasse + "_0" +"_Jahresrangliste.xlsx");
						writeJahresrangliste(Pfad + "\\" + Integer.toString(myParsedFile.Jahr) + "_" + myParsedFile.Klasse + "_0" +"_Jahresrangliste.xlsx",myJahresrangliste);
					}
				}
				else
				{
					System.out.println("Keine Rennresultate gefunden");
					JOptionPane.showMessageDialog(null, "Keine Rennresultate gefunden", "Report", JOptionPane.OK_OPTION);
				}
			}
			else
			{
				System.out.println("Keine Files gefunden");
				JOptionPane.showMessageDialog(null, "Keine Dateien gefunden", "Report", JOptionPane.OK_OPTION);
			}
		}
        catch(Exception e)
        {
        	System.out.println("Dateipfad nicht gefunden");
        	JOptionPane.showMessageDialog(null, "Dateipfad nicht gefunden", "Report", JOptionPane.OK_OPTION);
        }
	}
	/**
	 * @brief Liest aus einem File die Rennresultate fuer jeden aufgefuehrten Fahrer und fuegt diese der aktuellen Jahresrangliste hinzu
	 * @param filepath
	 * @param myJahresrangliste
	 */
	public void readRennresultat(String filepath, Jahresrangliste myJahresrangliste){
		ReadExcel reader = new ReadExcel();
		ParsedFile myParsedFile = ParseFileName(filepath);
	    int Punkte;
	    String Vorname = null; 
	    String Nachname = null;
	    // Anhand Dateinamen ueberpruefen ob das File ein korrektes Rennresultat ist
	    if((!myParsedFile.Austragungsort.equals(""))  && (!myParsedFile.Austragungsort.equals("Jahresrangliste")))
	    {
		    try {
		    	reader.setInputFile(filepath);
				reader.initRead();
			    myJahresrangliste.addLauf(myParsedFile.Austragungsort, myParsedFile.LaufNummer);
			    System.out.println(Integer.toString(myParsedFile.LaufNummer) + ": " + myParsedFile.Austragungsort);
			    System.out.println("Anzahl Teilnehmer = " + Integer.toString(reader.getRows()));
			    for (int i = T_START; i <= reader.getRows(); i++) {
				  //Rang
				  Punkte = getPunkteFromRang(Integer.parseInt(reader.readCell(T_RANG, i, XSSFCell.CELL_TYPE_NUMERIC)));
				  //Nachname
				  Nachname = reader.readCell(T_NACHNAME, i, XSSFCell.CELL_TYPE_STRING);
				  //Vorname
				  Vorname = reader.readCell(T_VORNAME, i, XSSFCell.CELL_TYPE_STRING);
				  
				  myJahresrangliste.addResultat(myParsedFile.LaufNummer, Punkte, Vorname, Nachname);
				}
		    } catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	/**
	 * @brief Schreibt eine Jahresrangliste in ein Excel
	 * @param filepath
	 * @param myRangliste
	 */
	public void writeJahresrangliste(String filepath, Jahresrangliste myJahresrangliste){
		WriteExcel writer = new WriteExcel();
		ParsedFile myParsedFile = ParseFileName(filepath);
		writer.setOutputFile(filepath);
		String[][] myRangliste = myJahresrangliste.getJahresrangliste();
		System.out.println("Groesse der Resultate [" + myRangliste.length + "][" + myRangliste[0].length + "]");
		try {
			writer.initWrite(myRangliste[0].length,myRangliste.length+1);
			
			// Titel hinzufuegen
			writer.addTitle(0, 0, myParsedFile.Austragungsort + " " + myParsedFile.Klasse + " " + Integer.toString(myParsedFile.Jahr), 0, 0, myRangliste[0].length-1, 0);
			for(int y=0 ; y<myRangliste.length ; y++)
			{
				for(int x=0 ; x<myRangliste[0].length ; x++)
				{
					// Abhaengig von der Zeile jeweils Nummern oder Text hinzufuegen
					if(y>1 && x!=1 && x!=2) writer.addNumber(x, y+1, Integer.parseInt(myRangliste[y][x]));
					else
					{
						if(y<2) writer.addCaption(x, y+1, myRangliste[y][x]); // Fügt die Ueberschriften hinzu
						else writer.addLabel(x, y+1, myRangliste[y][x]);
					}
					System.out.print(myRangliste[y][x] + "    ");
				}
				System.out.println();
			}
			System.out.println("Jahresrangliste gespeichert unter: " + filepath);
			JOptionPane.showMessageDialog(null, "Jahresrangliste gespeichert unter: " + filepath, "Report", JOptionPane.INFORMATION_MESSAGE);
			writer.finishWrite();
		} catch (IOException e) {
			System.out.print("Schreiben der Rangliste fehlgeschlagen");
			e.printStackTrace();
		}
	}
	/**
	 * @brief Extrahiert Jahr, Klasse, Laufnummer, Austragungsort aus dem Dateinamen
	 * @param filepath
	 * @return
	 */
	public ParsedFile ParseFileName(String filepath){
		ParsedFile myParsedFile = new ParsedFile();
		int index_1,index_2;
		index_1 = filepath.lastIndexOf("\\");
		index_2 = filepath.indexOf("_",index_1+1);
		
		// Es wird immer zwischen zwei '_' Daten gelesn
		if((index_1 != -1) && (index_2 !=-1))
		{
			char[] mychar;
			String mystring;
			
			// Jahr
			mychar = new char[index_2-(index_1+1)];
			filepath.getChars(index_1+1, index_2, mychar, 0);
			mystring = new String(mychar);
			System.out.println("Jahr: " + mystring);
			myParsedFile.Jahr = Integer.parseInt(mystring);
			
			// Klasse
			index_1 = index_2;
			index_2 = filepath.indexOf("_",index_1+1);
			mychar = new char[index_2-(index_1+1)];
			filepath.getChars(index_1+1, index_2, mychar, 0);
			mystring = new String(mychar);
			System.out.println("Klasse: " + mystring);
			myParsedFile.Klasse = mystring;
			
			// Laufnummer
			index_1 = index_2;
			index_2 = filepath.indexOf("_",index_1+1);
			mychar = new char[index_2-(index_1+1)];
			filepath.getChars(index_1+1, index_2, mychar, 0);
			mystring = new String(mychar);
			System.out.println("Laufnummer: " + mystring);
			myParsedFile.LaufNummer = Integer.parseInt(mystring);
			
			//Austragungsort
			index_1 = index_2;
			index_2 = filepath.indexOf(".",index_1+1);
			mychar = new char[index_2-(index_1+1)];
			filepath.getChars(index_1+1, index_2, mychar, 0);
			mystring = new String(mychar);
			System.out.println("Austragungsort: " + mystring);
			myParsedFile.Austragungsort = mystring;
		}
		return myParsedFile;
	}
	/**
	 * @brief Ruft den Java standard FileChooser auf
	 */
	public void choosePfad(){
		chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Datensatz auswaehlen");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getSelectedFile() : " 
	         +  chooser.getSelectedFile());
	      TF_Pfad.setText(chooser.getSelectedFile().getPath());
	      }
	    else {
	      System.out.println("No Selection ");
	      }
	}
	/**
	 * @brief Gibt alle .xlsx Files im angegebenen Pfad zurueck
	 * @param direction
	 * @return
	 */
	public File[] getFileList(String direction){
		File dir = new File(direction);   

        File[] fileList = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xlsx");
             }
        });
        
        return fileList;
	}
	/**
	 * @brief Gibt Punkte anhand des Ranges zurueck
	 * @param Rang
	 * @return
	 */
	public int getPunkteFromRang(int Rang)
	{
		if(Rang>0 && (Rang-1)<Punktetabelle.length) return Punktetabelle[Rang-1];
		else return 0;
	}
	/**
	 * @brief Gibt die Anzahl zu wertende Laeufe zurueck bei uebergebener Gesamtanzahl Rennen
	 * @param AnzahlRennen
	 * @return
	 */
	public int getAnzahlGewertete(int AnzahlRennen)
	{
		if(AnzahlRennen>0 && (AnzahlRennen-1)<ZuWertendeLaeufe.length) return ZuWertendeLaeufe[AnzahlRennen-1];
		else return 0;
	}
}