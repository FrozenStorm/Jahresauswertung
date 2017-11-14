import java.util.ArrayList;

/**
 * @brief Verwaltet eine Jahresrangliste mit zwei Listen, Laeufe und Rennfahrer. Generiert eine Jahresranglistentabelle aus den gesamelten Resultaten
 * @author Daniel
 *
 */
public class Jahresrangliste {
	private int AnzahlGewertete = 0; // Anzahl gewertete Laeufe welches es in diesem Jahr geben wird
	public ArrayList<Lauf> Laeufe; // Liste mit allen Laeufen welche dieses Jahr stadtgefunden haben sortiert nach Laufnummer
	public ArrayList<Fahrer> Rennfahrer; // Liste mit allen Rennfahrern und ihrer Resultaten sortiert nach Rang
	/**
	 * @brief Initialisiert eine neue Jahresrangliste
	 */
	public Jahresrangliste() {
		Laeufe = new ArrayList<Lauf>();
		Rennfahrer = new ArrayList<Fahrer>();
	}
	/** 
	 * Fuegt einen neuen Lauf hinzu und sortirt sie nach Laufnummer
	 * @param Austragungsort
	 * @param LaufNummer
	 */
	public void addLauf(String Austragungsort, int LaufNummer){
		Laeufe.add(new Lauf(Austragungsort,LaufNummer));
		sortLaeufe();
	}
	/**
	 * @brief Fuegt ein neues Resultat hinzu. Falls der Fahrer bereits in der Liste Vorhanden ist wird ihm das Resultat hinzugefuegt
	 * 		  ansonsten wird ein neuer Fahrer erzeugt
	 * @param LaufNummer
	 * @param Punkte
	 * @param Vorname
	 * @param Nachname
	 */
	public void addResultat(int LaufNummer, int Punkte, String Vorname, String Nachname){
		int index = -1;
		for(int i=0 ; i<Rennfahrer.size() ; i++)
		{
			if((Rennfahrer.get(i).Nachname.equals(Nachname)) && (Rennfahrer.get(i).Vorname.equals(Vorname))) index = i;
		}
		if(index == -1) // Fahrer existiert noch nicht
		{
			Rennfahrer.add(new Fahrer(Nachname,Vorname,AnzahlGewertete));
			index = Rennfahrer.size()-1;
		}
		Rennfahrer.get(index).addResultat(LaufNummer, Punkte);
		sortRennfahrer();
	}
	/**
	 * @brief Erzeugt eine Tabelle aus Strings mit der Jahresrangliste anhand der Resultate der Fahrer
	 * @return
	 */
	public String[][] getJahresrangliste(){
		int Rang = 1; // Der aktuelle Rang welcher geschrieben wird
		int Mehrfachrang = 1; // Falls der gleiche Rang mehrmals geschrieben wurde muessen danach entsprechend Raenge uebersprungen werden
		String[][] Rangliste = new String[Rennfahrer.size()+2][Laeufe.size()+4];
		sortLaeufe();
		sortRennfahrer();
		// Erste Zeile mit den Austrangungsorten
		for(int x=0 ; x<Laeufe.size() ; x++)
		{
			Rangliste[0][x+3] = Laeufe.get(x).Austragungsort;
		}
		
		// Zweite Zeile
		Rangliste[1][0] = "Rang";
		Rangliste[1][1] = "Name";
		Rangliste[1][2] = "Vorname";
		for(int x=0 ; x<Laeufe.size() ; x++)
		{
			Rangliste[1][x+3] = "Lauf " + Integer.toString(Laeufe.get(x).LaufNummer);
		}
		Rangliste[1][Laeufe.size()+3] = "Total";
		
		// Rangliste
		for(int y=0 ; y<Rennfahrer.size(); y++)
		{			
			// Rang des Fahrers errechnen
			if(y>0) 
			{
				if(Rennfahrer.get(y).compareTo(Rennfahrer.get(y-1))==0) // Gleichstand zweier Fahrer
				{
					Mehrfachrang = Mehrfachrang + 1; // Beim naechsten Rang soll einer uebersprungen werden
				}
				else // Hat weniger Punkte als der Ranghoehere
				{
					Rang = Rang + Mehrfachrang;
					Mehrfachrang = 1; 
				}
			}
			else // Der erste in der Liste ist immer 1.
			{
				Rang = 1;
			}
			
			// Rang eintragen
			Rangliste[y+2][0] = Integer.toString(Rang);
			
			// Namen des Fahrers eintragen
			Rangliste[y+2][1] = Rennfahrer.get(y).Nachname;
			Rangliste[y+2][2] = Rennfahrer.get(y).Vorname;
			
			// Resultate der Laeufe fuer diesen Fahrer eintragen
			for(int x=0 ; x<Laeufe.size() ; x++)
			{
				Rangliste[y+2][x+3] = Integer.toString(Rennfahrer.get(y).getLaufResultat(Laeufe.get(x).LaufNummer));
			}
			
			// Totalpunkte dieses Fahrers eintragen
			Rangliste[y+2][Laeufe.size()+3] = Integer.toString(Rennfahrer.get(y).getPunkte());
		}
		return Rangliste;
	}
	/**
	 * @brief Anzahl zu wertende Laeufe setzen und den neuen Werte bei allen bereits bekannen Fahrern aktuallisieren
	 * @param AnzahlGewertete
	 */
	public void setAnzahlGewertete(int AnzahlGewertete) {
		this.AnzahlGewertete = AnzahlGewertete;
		for(int i=0 ; i<Rennfahrer.size() ; i++)
		{
			Rennfahrer.get(i).setAnzahlGewertete(AnzahlGewertete);
		}
		sortRennfahrer();
	}
	/**
	 * @brief Rennfahrer anhand Punkte sortieren
	 */
	private void sortRennfahrer()
	{
		Rennfahrer.sort(null);
	}
	/**
	 * @brief Laeufe anhand Laufnummer sortieren
	 */
	private void sortLaeufe()
	{
		Laeufe.sort(null);
	}
}
