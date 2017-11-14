/**
 * @brief Lauf mit Namen und Nummer
 * @author Daniel
 * 
 */
public class Lauf implements Comparable<Lauf>{
	public String Austragungsort;
	public int LaufNummer;
	/** 
	 * @param Austragungsort
	 * @param LaufNummer
	 */
	public Lauf(String Austragungsort, int LaufNummer) {
		this.Austragungsort = Austragungsort;
		this.LaufNummer = LaufNummer;
	}
	/**
	 * @brief Vergleicht LaufNummer
	 * @param Lauf 
	 */
	public int compareTo(Lauf o) {
		return Integer.compare(this.LaufNummer,o.LaufNummer);
	}
}
