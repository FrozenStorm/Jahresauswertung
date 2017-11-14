import java.util.ArrayList;
/**
 * @brief Ein Fahrer mit seinen Resultaten aller Laeufe sortiert nach Punkten
 * @author Daniel
 *
 */
public class Fahrer implements Comparable<Fahrer> {
	public String Nachname;
	public String Vorname;
	private int AnzahlGewertete;
	public ArrayList<Resultat> Resultate;
	/**
	 * @param Nachname
	 * @param Vorname
	 * @param AnzahlGewertete
	 */
	public Fahrer(String Nachname, String Vorname, int AnzahlGewertete) {
		this.Nachname = Nachname;
		this.Vorname = Vorname;
		this.AnzahlGewertete = AnzahlGewertete;
		this.Resultate = new ArrayList<Resultat>();
	}
	/**
	 * @brief Setzt die Anzahl gewertete Laeufe in diesem Jahr
	 * @param AnzahlGewertete
	 */
	public void setAnzahlGewertete(int AnzahlGewertete) {
		this.AnzahlGewertete = AnzahlGewertete;
	}
	/**
	 * @brief Fuegt ein Resultat hinzu und sortiert danach die Resultate neu
	 * @param LaufNummer
	 * @param Punkte
	 */
	public void addResultat(int LaufNummer, int Punkte){
		Resultate.add(new Resultat(LaufNummer,Punkte));
		sortResultate(); //ev unnoetig
	}
	/**
	 * @brief Gibt die Punkte dieses Jahres zurueck wobei die lokale gesetzte AnzahlGewertete Laeufe gezaehlt werden
	 * @return
	 */
	public int getPunkte(){
		int Total = 0;
		sortResultate();
		for(int i = 0; (i < Resultate.size()) && (i < AnzahlGewertete); i++)
		{
			Total += Resultate.get(i).Punkte;
		}
		return Total;
	}
	/**
	 * @brief Gibt die Punkte dieses Hares zurueck wobei AnzahlGewertete Laeufe gezaehlt werden
	 * @param AnzahlGewertete
	 * @return
	 */
	public int getPunkte(int AnzahlGewertete){
		int Total = 0;
		sortResultate();
		for(int i = 0; (i < Resultate.size()) && (i < AnzahlGewertete); i++)
		{
			Total += Resultate.get(i).Punkte;
		}
		return Total;
	}
	/**
	 * @brief Gibt die Punkte eines bestimmten Laufes zurueck
	 * @param LaufNummer
	 * @return
	 */
	public int getLaufResultat(int LaufNummer){
		for(int i = 0; i < Resultate.size(); i++)
		{
			if(Resultate.get(i).LaufNummer == LaufNummer) return Resultate.get(i).Punkte;
		}
		return 0;
	}
	/**
	 * @brief Sortiert die Resultate anhand ihrer natuerlichen Reihenfolge
	 */
	private void sortResultate(){
		Resultate.sort(null);
	}
	/**
	 * @brief Vergleicht zwei Fahrer anhand ihrer Resultate. Bei Gleichstand mit AnzahlGewertete Laeufen werden weitere Laeufe dazugenommen.
	 * @param Fahrer
	 */
	public int compareTo(Fahrer o) { 
		int i = AnzahlGewertete;
		for(; (i <= AnzahlGewertete) || (i <= o.Resultate.size()) || (i <= this.Resultate.size()); i++)
		{
			if(o.getPunkte(i) > this.getPunkte(i)) return 1; // f1 > f2
			if(o.getPunkte(i) < this.getPunkte(i)) return -1; // f1 < f2
		}
		return 0;
	}
	
	
}