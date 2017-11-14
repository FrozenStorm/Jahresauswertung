/**
 * @brief Resultat eines Fahrers bei einem bestimmten Lauf
 * @author Daniel
 *
 */
public class Resultat implements Comparable<Resultat> {
	public int Punkte;
	public int LaufNummer;
	/**
	 * @param LaufNummer
	 * @param Punkte
	 */
	public Resultat(int LaufNummer, int Punkte) {
		this.Punkte = Punkte;
		this.LaufNummer = LaufNummer;
	}
	/**
	 * @brief Vergleicht Punkte der Resultate unabhaengig von der LaufNummer
	 * @param Resultat
	 */
	public int compareTo(Resultat o) {
		return  o.Punkte > this.Punkte ? 1 : this.Punkte == o.Punkte ? 0 : -1;
	}
}