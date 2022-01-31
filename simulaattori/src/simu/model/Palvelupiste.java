package simu.model;

import java.text.DecimalFormat;
import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;
import tietokanta.Simulointi;

/**
* <p>This class is one of the key classes in simulation process</p>
* <p>Palvelupiste aka service point, or place, where Asiakas-objects (aka Customer) can "get service"</p>
* <p>The objects of the class can be different types and have different settings, for example max time of service</p>
* <p>It is also used as a storage for a simulation data, for example total amount of time of customer service</p>
* <p>Objects' data can be saved to the database via PalvelupisteDAO class</p>
* 
* @see <a href="Asiakas.html">Asiakas</a>
* @see <a href="../../tietokanta/PalvelupisteDAO.html">PalvelupisteDAO</a>
* 
* @author Mikhail Deriabin
* 
*/
public class Palvelupiste {

	/**
	 * Line of customers, used as storage for Asiakas-objects, which will be serviced by this Palvelupiste-object
	 * @see <a href="Asiakas.html">Asiakas</a>
	 */
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();

	/**
	 * Program inner clock
	 */
	Kello kello = Kello.getInstance();
	
	/**
	 * Continues random number generator
	 * @see <a href="../../eduni/distributions/ContinuousGenerator.html">ContinuousGenerator</a>
	 */
	private ContinuousGenerator generator;
	
	/**
	 * Event list, common for whole program, used as program sequencing
	 * @see <a href="../framework/Tapahtumalista.html">Tapahtumalista</a>
	 */
	private Tapahtumalista tapahtumalista;
	
	/**
	 * The next event type, used by OmaMoottori-class
	 * @see <a href="OmaMoottori.html">OmaMoottori</a>
	 * @see <a href="TapahtumanTyyppi.html">TapahtumanTyyppi</a>
	 */
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

	/**
	 * Status of the Palvelupiste-object, true = basy false = free
	 */
	private boolean varattu = false;

	// Palvelupisteen tiedot
	/**
	 * Specific order number of Palvelupiste-object type, used for generating name of the Palvelupite-object 
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private int ppNum;
	
	/**
	 * Common order number of the Palvelupite-object
	 */
	private int ppID;
	
	/**
	 * The next order number for RUOKATISKI-type Palvelupiste-object
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private static int nextRuokatiskiNum = 1;
	
	/**
	 * The next order number for KASSA-type Palvelupiste-object
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private static int nextKassaNum = 1;
	
	/**
	 * The next order number for IPKASSA-type Palvelupiste-object
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private static int nextIpKassaNum = 1;
	
	/**
	 * The next order number for generating common order number of the Palvelupite-object
	 */
	private static int nextPpId = 0;
	
	/**
	 * Name of the Palvelupite-object
	 */
	private String ppNimi;
	
	/**
	 * Type of the Palvelupiste-object 
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private PalvelupisteenTyyppi ppTyyppi = null;

	/**
	 * palvelupisteet-table primary key (auto_increment)
	 */
	private int id;

	/**
	 * palvelupisteet-table foreign key, can not be null
	 */
	private Simulointi simulointi;

	// Suorituskykysuureet + DB taulukkojen tiedot
	/**
	 * palvelupisteet-table customer went through the Palvelupiste-object count field, can be null
	 */
	private int asiakkaidenLkm;
	
	/**
	 * palvelupisteet-table amount of busy time of the Palvelupiste-object field, can be null
	 */
	private double aktiiviaika;
	
	/**
	 * palvelupisteet-table amount of waiting and service time of the Palvelupiste-object field, can be null
	 */
	private double kokoOleskAika;
	
	/**
	 * palvelupisteet-table utilization of the Palvelupiste-object field, can be null
	 */
	private double kayttoaste;
	
	/**
	 * palvelupisteet-table throughput of the Palvelupiste-object field, can be null
	 */
	private double suoritusteho;
	
	/**
	 * palvelupisteet-table average service time of the Palvelupiste-object field, can be null
	 */
	private double keskiPalveluaika;
	
	/**
	 * palvelupisteet-table average response time of the Palvelupiste-object field, can be null
	 */
	private double keskiLapiMenoAika;
	
	/**
	 * palvelupisteet-table average customer line length during the simulation of the Palvelupiste-object field, can be null
	 */
	private double keskiJononpituus;

	/**
	 * <p>Parameterless constructor, which does nothing</p>
	 */
	public Palvelupiste() {
	}

	/**
	 * <p>Constructor, which set up all object information</p>
	 * @param generator Random number gerator
	 * @param tapahtumalista object, which contains information about simulation events
	 * @param ppt type of the Palvelupiste-object 
	 * @see <a href="../../eduni/distibutions/Normal.html">Normal</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	public Palvelupiste(Normal generator, Tapahtumalista tapahtumalista, PalvelupisteenTyyppi ppt, int id) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = maaritaTT(ppt);
		this.ppTyyppi = ppt;
		this.ppID = id;
		this.setPpInfo();
		this.asiakkaidenLkm = 0;
		this.kokoOleskAika = 0;
	}

	/**
	 * <p>The method adds Asikas-object to the Palvelupiste-object's line for waiting for service</p>
	 * @param a Asiakas-object, which will be added to the line
	 * @see <a href="Asiakas.html">Asiakas</a>
	 */
	public void lisaaJonoon(Asiakas a) { // Jonon 1. asiakas aina palvelussa
		jono.add(a);
	}

	/**
	 * <p>The method remove Asikas-object from the Palvelupiste-object's line</p>
	 * @see <a href="Asiakas.html">Asiakas</a>
	 * @return Asiakas-object, which was removed
	 */
	public Asiakas otaJonosta() { // Poistetaan palvelussa ollut
		varattu = false;
		return jono.poll();
	}

	
	/**
	 * <p>The method starts "service" for Asikas-object</p>
	 * <p>The method collects and data about Palvelupiste-object</p>
	 * @see <a href="Asiakas.html">Asiakas</a>
	 */
	public void aloitaPalvelu() { // Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		Asiakas palvAsiakas = jono.peek();
		Trace.out(Trace.Level.INFO, this.ppNimi + ": Aloitetaan uusi palvelu asiakkaalle " + palvAsiakas.getId());

		varattu = true;
		double palveluaika = generator.sample();
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, kello.getAika() + palveluaika, ppID));

		// Suorituskykysureiden päivittäminen
		this.asiakkaidenLkm++;
		aktiiviaika += palveluaika;
		double asSaapJonoonHetki = palvAsiakas.getJonoonSaapumisAika();
		double asPoistHetki = kello.getAika() + palveluaika;
		kokoOleskAika += (asPoistHetki - asSaapJonoonHetki);
		System.out.println();
	}

	/**
	 * <p>The method checks is Palvelupiste-object free for getting next Asiakas-object</p>
	 * @see <a href="Asiakas.html">Asiakas</a>
	 * @return true if busy, false if free
	 */
	public boolean onVarattu() {
		return varattu;
	}
	
	/**
	 * <p>The method checks is Palvelupiste-object's line empty or not</p>
	 * @see <a href="Asiakas.html">Asiakas</a>
	 * @return true if empty, false if not
	 */
	public boolean onJonossa() {
		return jono.size() != 0;
	}

	/**
	 * <p>The method calculate all Palvelupiste-object data which need to be calculated based on collected information during the simulation</p>
	 * <p>For example average service time</p>
	 */
	public void laskeSuorituskykysuureet() {
		this.kayttoaste = laskeKayttoaste();
		this.suoritusteho = laskeSuoritusteho();
		this.keskiPalveluaika = laskeKeskiPalveluaika();
		this.keskiLapiMenoAika = laskeKeskiLapiMenoAika();
		this.keskiJononpituus = laskeKeskiJononpituus();
	}
	
	/**
	 * <p>The method prints a raport to the console about finished simulation</p>
	 */
	public void raportti() {
		System.out.println("\n" + this.ppNimi + (this.ppNum) + ", suorituskykysuureet:\n");

		System.out.println("Palveltujen asiakkaiden lukumäärä: " + this.getAsiakkaidenLkm());
		System.out.println("Palvelupisteen aktiiviaika: " + this.getAktiiviaika());
		System.out.println("Asiakkaiden kokonaisoleskeluaika palvelupisteessa: " + this.getKokoOleskAika());
		System.out.println("Palvelupisteen käyttöaste: " + this.getKayttoaste());
		System.out.println("Palvelupisteen suoritusteho: " + this.getSuoritusteho());
		System.out.println("Palvelupisteen keskipalveluaika: " + this.getKeskiPalveluaika());
		System.out.println("Palvelupisteen keskiläpimenoaika: " + this.getKeskiLapiMenoAika());
		System.out.println("Palvelupisteen keski jononpituus: " + this.getKeskiJononpituus() + "\n");
	}
	
	/**
	 * The method returns results of the simulation
	 * @return results of the simulation
	 */
	public String getSimuTulos() {
		// Tulokset ilmoitetaan DecimalFormatin avulla 2 desimaalin tarkkuudella.
		DecimalFormat df = new DecimalFormat("#.##");
		return "Palveltujen asiakkaiden lukumäärä: " + this.getAsiakkaidenLkm() + "\nAktiiviaika: "
				+ df.format(this.getAktiiviaika()) + "\nAsiakkaiden kokonaisoleskeluaika palvelupisteessa: "
				+ df.format(this.getKokoOleskAika()) + "\nKäyttöaste: " + df.format(this.getKayttoaste())
				+ "\nSuoritusteho: " + df.format(this.getSuoritusteho()) + "\nPalveluajan keskiarvo: "
				+ df.format(this.getKeskiPalveluaika()) + "\nLäpimenoajan keskiarvo: "
				+ df.format(this.getKeskiLapiMenoAika()) + "\nJonon keskimääräinen pituus: "
				+ df.format(this.getKeskiJononpituus()) + "\n";
	}

	/**
	 * The method sets Palvelupiste-object info fields, such as type, name etc.
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private void setPpInfo() {
		switch (this.ppTyyppi) {
		case RUOKATISKI:
			this.ppNum = nextRuokatiskiNum;
			ppNimi = "Ruokatiski";
			nextRuokatiskiNum++;
			break;
		case KASSA:
			this.ppNum = nextKassaNum;
			ppNimi = "Kassa";
			nextKassaNum++;
			break;
		case IPKASSA:
			this.ppNum = nextIpKassaNum;
			ppNimi = "IP kassa";
			nextIpKassaNum++;
			break;
		}
		nextPpId++;
	}

	/**
	 * The method defines the next event
	 * @param ppt type of the Palvelupiste-object
	 * @return the next event, which will be added to the TapahtumaLista
	 * @see <a href="TapahtumanTyyppi.html">TapahtumanTyyppi</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 * @see <a href="../framework/Tapahtumalista.html">Tapahtumalista</a>
	 */
	private TapahtumanTyyppi maaritaTT(PalvelupisteenTyyppi ppt) {
		TapahtumanTyyppi tt = null;
		if (ppt == PalvelupisteenTyyppi.RUOKATISKI)
			tt = TapahtumanTyyppi.DEP1;
		else if (ppt == PalvelupisteenTyyppi.KASSA || ppt == PalvelupisteenTyyppi.IPKASSA)
			tt = TapahtumanTyyppi.DEP2;

		return tt;
	}

	// DB tiedot (id, simulointi + suorituskykysuureet + nimi)
	/**
	 * @return object's primary key in the palvelupisteet-table, can not be null
	 */
	public int getId() {
		return id;
	}

	/**
	 * Used by Hibernate-library only, Do not set manually. The method sets object's primary key in the palvelupiste-table
	 * @param id can not be null
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Simulointi-object, to which the object is related, can not be null
	 * @see <a href="../../tietokanta/Simulointi.html">Simulointi</a>
	 */
	public Simulointi getSimulointi() {
		return simulointi;
	}

	/**
	 * The method sets Simulointi-object, to which the object is related, can not be null
	 * @param simulointi simulation to which the object is related
	 * @see <a href="../../tietokanta/Simulointi.html">Simulointi</a>
	 */
	public void setSimulointi(Simulointi simulointi) {
		this.simulointi = simulointi;
	}

	// Suorituskykysuureet

	// getters
	/**
	 * @return palvelupisteet-table customer went through the Palvelupiste-object count field, can be null
	 */
	public int getAsiakkaidenLkm() {
		return asiakkaidenLkm;
	}

	/**
	 * @return palvelupisteet-table amount of busy time of the Palvelupiste-object field, can be null
	 */
	public double getAktiiviaika() {
		return aktiiviaika;
	}

	/**
	 * @return palvelupisteet-table amount of waiting and service time of the Palvelupiste-object field, can be null
	 */
	public double getKokoOleskAika() {
		return kokoOleskAika;
	}

	/**
	 * @return palvelupisteet-table utilization of the Palvelupiste-object field, can be null
	 */
	public double getKayttoaste() {
		return kayttoaste;
	}

	/**
	 * @return palvelupisteet-table throughput of the Palvelupiste-object field, can be null
	 */
	public double getSuoritusteho() {
		return suoritusteho;
	}

	/**
	 * @return palvelupisteet-table average service time of the Palvelupiste-object field, can be null
	 */
	public double getKeskiPalveluaika() {
		return keskiPalveluaika;
	}

	/**
	 * @return palvelupisteet-table average response time of the Palvelupiste-object field, can be null
	 */
	public double getKeskiLapiMenoAika() {
		return keskiLapiMenoAika;
	}

	/**
	 * @return palvelupisteet-table average customer line length during the simulation of the Palvelupiste-object field, can be null
	 */
	public double getKeskiJononpituus() {
		return keskiJononpituus;
	}

	// setters
	/**
	 * The method sets customer went through the Palvelupiste-object count field in the palvelupiste-table
	 * @param asiakkaidenLkm can be null
	 */
	public void setAsiakkaidenLkm(int asiakkaidenLkm) {
		this.asiakkaidenLkm = asiakkaidenLkm;
	}

	/**
	 * The method sets amount of busy time of the Palvelupiste-object field in the palvelupiste-table
	 * @param aktiiviaika can be null
	 */
	public void setAktiiviaika(double aktiiviaika) {
		this.aktiiviaika = aktiiviaika;
	}

	/**
	 * The method sets amount of waiting and service time of the Palvelupiste-object field in the palvelupiste-table
	 * @param kokoOleskAika can be null
	 */
	public void setKokoOleskAika(double kokoOleskAika) {
		this.kokoOleskAika = kokoOleskAika;
	}

	/**
	 * The method sets utilization of the Palvelupiste-object field in the palvelupiste-table
	 * @param kayttoaste can be null
	 */
	public void setKayttoaste(double kayttoaste) {
		this.kayttoaste = kayttoaste;
	}

	/**
	 * The method sets throughput of the Palvelupiste-object field in the palvelupiste-table
	 * @param suoritusteho can be null
	 */
	public void setSuoritusteho(double suoritusteho) {
		this.suoritusteho = suoritusteho;
	}

	/**
	 * The method sets average service time of the Palvelupiste-object field in the palvelupiste-table
	 * @param keskiPalveluaika can be null
	 */
	public void setKeskiPalveluaika(double keskiPalveluaika) {
		this.keskiPalveluaika = keskiPalveluaika;
	}

	/**
	 * The method sets average response time of the Palvelupiste-object field in the palvelupiste-table
	 * @param keskiLapiMenoAika can be null
	 */
	public void setKeskiLapiMenoAika(double keskiLapiMenoAika) {
		this.keskiLapiMenoAika = keskiLapiMenoAika;
	}

	/**
	 * The method sets average customer line length during the simulation of the Palvelupiste-object field in the palvelupiste-table
	 * @param keskiJononpituus can be null
	 */
	public void setKeskiJononpituus(double keskiJononpituus) {
		this.keskiJononpituus = keskiJononpituus;
	}

	// laskeminen
	/**
	 * The method calculates throughput of the Palvelupiste-object
	 * @return throughput of the Palvelupiste-object
	 */
	private double laskeKayttoaste() {
		return this.aktiiviaika / kello.getAika();
	}

	/**
	 * The method calculates average service time of the Palvelupiste-object
	 * @return average service time of the Palvelupiste-object
	 */
	private double laskeSuoritusteho() {
		return this.asiakkaidenLkm / kello.getAika();
	}

	/**
	 * The method calculates average service time of the Palvelupiste-object
	 * @return average service time of the Palvelupiste-object
	 */
	private double laskeKeskiPalveluaika() {
		if (this.asiakkaidenLkm > 0)
			return this.aktiiviaika / this.asiakkaidenLkm;
		return 0d;
	}

	/**
	 * The method calculates average response time of the Palvelupiste-object
	 * @return average response time of the Palvelupiste-object
	 */
	private double laskeKeskiLapiMenoAika() {
		if (this.asiakkaidenLkm > 0)
			return this.kokoOleskAika / this.asiakkaidenLkm;
		return 0d;
	}

	/**
	 * The method calculates average customer line length during the simulation of the Palvelupiste-object
	 * @return average customer line length during the simulation of the Palvelupiste-object
	 */
	private double laskeKeskiJononpituus() {
		return this.kokoOleskAika / kello.getAika();
	}

	/**
	 * @return length of the customer line
	 */
	public int getJonoSize() {
		return this.jono.size();
	}

	/**
	 * @return common order number of the Palvelupite-object
	 */
	public int getPpID() {
		return this.ppID;
	}

	/**
	 * The method sets the common order number of the Palvelupite-object, should not be negative
	 */
	public void setPpID(int ppID) {
		this.ppID = ppID;
	}

	/**
	 * @return name of the object
	 */
	public String getPpNimi() {
		return this.ppNimi;
	}

	/**
	 * The method sets name of the object
	 */
	public void setPpNimi(String ppNimi) {
		this.ppNimi = ppNimi;
	}

	/**
	 * @return Palvelupiste-object's number
	 */
	public int getPpNum() {
		return this.ppNum;
	}

	/**
	 * The method resets needed static fields for program proper work
	 */
	public void reset() {
		nextRuokatiskiNum = 1;
		nextKassaNum = 1; 
		nextIpKassaNum = 1;
		nextPpId = 0;
	}
	
	/**
	 * The method overrides toString() method to form "Palvelupiste " + its name
	 */
	@Override
	public String toString() {
		return "Palvelupiste " + ppNimi;
	}
}
