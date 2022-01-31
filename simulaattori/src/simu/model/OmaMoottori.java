package simu.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import controller.IKontrolleri;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;
import tietokanta.PalvelupisteDAO;
import tietokanta.Simulointi;
import tietokanta.SimulointiDAO;

/**
* <p>This class is one of the key classes in simulation process</p>
* <p>OmaMoottori class directs flow of the simulation</p>
* <p>The class sets up the simulation, moves Asiakas-objects through the simulation and save simulation data to the database</p>
* 
* @see <a href="Asiakas.html">Asiakas</a>
* 
* @author Mikhail Deriabin and Joona Nylander
* 
*/
public class OmaMoottori extends Moottori {
	/**
	 * @see <a href="../../tietokanta/SimulointiDAO.html">SimulointiDAO</a>
	 */
	SimulointiDAO simulointiDAO = new SimulointiDAO();
	/**
	 * @see <a href="../../tietokanta/PalvelupisteDAO.html">PalvelupisteDAO</a>
	 */
	PalvelupisteDAO palvelupisteDAO = new PalvelupisteDAO();

	/**
	 * Program inner clock
	 */
	Kello kello = Kello.getInstance();
	
	/**
	 * Normal-distribution for generating random service time for RUOKATISKI-type Palvelupiste-objects
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	Normal ruokatiskiJakauma;
	/**
	 * Normal-distribution for generating random service time for KASSA-type Palvelupiste-objects
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	Normal kassaJakauma;
	/**
	 * Normal-distribution for generating random service time for IPKASSA-type Palvelupiste-objects
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	Normal ipKassaJakauma;
	/**
	 * The array contains all distributions for generating random service time for all Palvelupiste's type objects
	 */
	Normal[] jakaumat;

	/**
	 * The object creates events with type ARR1 and adds it to the TapahtumaLista-object
	 * @see <a href="TapahtumanTyyppi.html">TapahtumanTyyppi</a>
	 */
	private Saapumisprosessi saapumisprosessi;
	
	/**
	 * Array of Palvelupiste-objects with type RUOKATISKI
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private Palvelupiste[] ruokatiskit;
	
	/**
	 * Array of all Palvelupiste-objects with types KASSA and IPKASSA
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private Palvelupiste[] kaikkiKassat;
	
	/**
	 * Array of all Palvelupiste-objects with type KASSA
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private Palvelupiste[] kassat;
	
	/**
	 * Array of all Palvelupiste-objects with type IPKASSA
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private Palvelupiste[] ipKassat;

	// Asetukset
	/**
	 * food courts count or Palvelupiste RUOKATISKI type objects, which will be created 
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private int ruokatiskienLkm;
	
	/**
	 * checkouts count or Palvelupiste KASSA type objects, which will be created 
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private int kassojenLkm;
	
	/**
	 * self-service checkouts count or Palvelupiste IPKASSA type objects, which will be created 
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="PalvelupisteenTyyppi.html">PalvelupisteenTyyppi</a>
	 */
	private int ipKassojenLkm;
	
	/**
	 * customers minimum and maximum count, which can come at one time
	 */
	private int[] asiakkaatKerralla = { 1, 5 }; // asiakkaiden min ja max lkm, jotka saapuvat ruokalaan kerralla

	/**
	 * food court minimum and maximum service time
	 */
	private double[] ruokatiskinAikaRajat = { 3d, 6d }; // min palveluaika, maks palveluaika
	
	/**
	 * checkout minimum and maximum service time
	 */
	private double[] kassanAikaRajat = { 2d, 4d }; // min palveluaika, maks palveluaika
	
	/**
	 * self-service checkout minimum and maximum service time
	 */
	private double[] ipKassanAikaRajat = { 1.7d, 3d }; // min palveluaika, maks palveluaika

	/**
	 * <p>Constructor for OmaMoottori, which works with GUI</p>
	 * @param kontrolleri Kontrolleri-object, which is used to define application behavior
	 * @see <a href="../../controller/IKontrolleri.html">IKontrolleri</a>
	 */
	public OmaMoottori(IKontrolleri kontrolleri) {

		super(kontrolleri);

		// Generoidaan Normal jakaumat palvelupisteille
		Normal ruokatiskiJakauma = this.getNormalGenerator(ruokatiskinAikaRajat[0], ruokatiskinAikaRajat[1]);
		Normal kassaJakauma = this.getNormalGenerator(kassanAikaRajat[0], kassanAikaRajat[1]);
		Normal ipKassaJakauma = this.getNormalGenerator(ipKassanAikaRajat[0], ipKassanAikaRajat[1]);
		jakaumat = new Normal[] { ruokatiskiJakauma, kassaJakauma, ipKassaJakauma };
	}

	/**
	 * The method makes all needed actions before simulation start
	 */
	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	/**
	 * The method executes event from TapahtumaLista-object, based on event type
	 * @param t event type
	 * @see <a href="TapahtumanTyyppi.html">TapahtumanTyyppi</a>
	 */
	@Override
	protected void suoritaTapahtuma(Tapahtuma t) { // B-vaiheen tapahtumat
		Asiakas a;
		switch (t.getTyyppi()) {
		case ARR1:
			// Tarkistaa kumman ruokatiskin jono on lyhyempi ja lisää asiakkaan sen jonoon
			Palvelupiste ruokatiski = lyhyinPpJono(ruokatiskit);

			int asiakkaidenLkm = ThreadLocalRandom.current().nextInt(asiakkaatKerralla[0], asiakkaatKerralla[1] + 1);
			for (int i = 0; i < asiakkaidenLkm; i++) {
				Asiakas uusiAsiakas = new Asiakas();
				ruokatiski.lisaaJonoon(uusiAsiakas);
				uusiAsiakas.setSaapumisaika(kello.getAika());
				uusiAsiakas.setJonoonSaapumisAika(kello.getAika());
			}

			System.out.println("ppID: " + ruokatiski.getPpID());
			int mihin = ruokatiski.getPpID();
			kontrolleri.varaaPP(mihin);
			saapumisprosessi.generoiSeuraava();
			break;

		case DEP1:
			int mista = t.getPpID();
			kontrolleri.vapautaPP(mista);
			a = palvelupisteet[mista].otaJonosta();
			Palvelupiste kassa = lyhyinPpJono(kaikkiKassat);
			kassa.lisaaJonoon(a);
			a.setJonoonSaapumisAika(kello.getAika());
			mihin = kassa.getPpID();
			kontrolleri.varaaPP(mihin);
			break;

		case DEP2:
			mista = t.getPpID();
			kontrolleri.vapautaPP(mista);
			a = palvelupisteet[mista].otaJonosta();
			a.setPoistumisaika(kello.getAika());
			a.raportti();
			kontrolleri.vieAsiakaslistaan(a);
			break;
		}
		kontrolleri.setAika(super.tapahtumalista.getSeuraavanAika());
	}

	/**
	 * The method determines the shortest customer line from given array.
	 * @param pp Palvelupiste-objects array
	 * @return Palvelupiste-object with shortest customer line
	 * @see <a href="Palvelupiste.html">Palvelupiste</a> 
	 */
	private Palvelupiste lyhyinPpJono(Palvelupiste[] pp) {
		int lyhyinJonoIndeksi = 0;
		ArrayList<Integer> samatJonoPituudetIndeksit = new ArrayList<>();
		samatJonoPituudetIndeksit.add(0);
		int lyhyinJonoPituus = pp[0].getJonoSize();

		for (int i = 1; i < pp.length; i++) {
			if (lyhyinJonoPituus > pp[i].getJonoSize()) {
				lyhyinJonoIndeksi = i;
				lyhyinJonoPituus = pp[i].getJonoSize();
				samatJonoPituudetIndeksit.clear();
			} else if (lyhyinJonoPituus == pp[i].getJonoSize()) {
				samatJonoPituudetIndeksit.add(i);
			}
		}
		if (samatJonoPituudetIndeksit.size() > 0) {
			int min = 0;
			int max = samatJonoPituudetIndeksit.size() - 1;
			lyhyinJonoIndeksi = ThreadLocalRandom.current().nextInt(min, max + 1);
		}

		return pp[lyhyinJonoIndeksi];

	}

	/**
	 * The method used to calculate mean for Normal-object
	 * @param min minimum number, which can Normal-object generate
	 * @param max maximum number, which can Normal-object generate
	 * @return mean for normal distribution
	 * @see <a href="../../eduni/distributions/Normal">Normal</a>
	 */
	private double laskeMean(double min, double max) {
		return (min + max) / 2;
	}

	/**
	 * The method used to calculate var for Normal-object
	 * @param max maximum number, which can Normal-object generate
	 * @param mean mean for Normal-object
	 * @return var for normal distribution
	 * @see <a href="../../eduni/distributions/Normal">Normal</a>
	 */
	private double laskeVar(double max, double mean) {
		return Math.pow((max - mean) / 3, 2);
	}

	/**
	 * The method generates new Normal-object
	 * @param minAika minimum number, which can Normal-object generate
	 * @param maxAika maximum number, which can Normal-object generate
	 * @return Normal with given frames
	 * @see <a href="../../eduni/distributions/Normal">Normal</a>
	 */
	private Normal getNormalGenerator(double minAika, double maxAika) {
		double mean = laskeMean(minAika, maxAika);
		double var = laskeVar(maxAika, mean);
		return new Normal(mean, var);
	}

	/**
	 * The method generates Palvelupiste-object array, based on given parameters
	 * @param jakaumat array of random number generators
	 * @return array of Palvelupiste-objects, can not be null
	 * 
	 * @see <a href="../../eduni/distributions/Normal">Normal</a> 
	 */
	private void generoiPalvelupisteet(Normal[] jakaumat) {
		int counter = 0;
		System.out.println("GENEROIPALVELUPISTEET: \n" + ruokatiskit.length + " " + kassat.length + " " + ipKassat.length);
		
		// Ruokalinjastot
		for (int i = 0; i < ruokatiskienLkm; i++) {
			ruokatiskit[i] = new Palvelupiste(jakaumat[0], tapahtumalista, PalvelupisteenTyyppi.RUOKATISKI, counter);
			counter++;
		}

		// Kassat
		for (int i = 0; i < kassojenLkm; i++) {
			kassat[i] = new Palvelupiste(jakaumat[1], tapahtumalista, PalvelupisteenTyyppi.KASSA, counter);
			counter++;
		}

		// IP-Kassat
		for (int i = 0; i < ipKassojenLkm; i++) {
			ipKassat[i] = new Palvelupiste(jakaumat[2], tapahtumalista, PalvelupisteenTyyppi.IPKASSA, counter);
			counter++;
		}
	}

	/**
	 * The method creates all needed Palvelupiste-objects based on settings 
	 */
	public void luoPalvelupisteet() {
		// Generoidaan palvelupisteet
		ruokatiskienLkm = kontrolleri.getRuokalinjastot();
		kassojenLkm = kontrolleri.getKassat();
		ipKassojenLkm = kontrolleri.getIPKassat();

		ruokatiskit = new Palvelupiste[ruokatiskienLkm];
		System.out.println("Ruokatiskit: " + ruokatiskit.length);
		kassat = new Palvelupiste[kassojenLkm];
		ipKassat = new Palvelupiste[ipKassojenLkm];
		generoiPalvelupisteet(jakaumat);

		kaikkiKassat = yhdistaPalvelupisteet(kassat, ipKassat);
		palvelupisteet = yhdistaPalvelupisteet(ruokatiskit, kaikkiKassat);

		saapumisprosessi = new Saapumisprosessi(new Negexp(20, 5), tapahtumalista, TapahtumanTyyppi.ARR1);
	}

	/**
	 * The method combines two Palvelupiste-objects arrays into one
	 * @param pp1 1st array, must not be null
	 * @param pp2 2nd array, must not be null
	 * @return combined array
	 * 
	 * @see <a href="Palvelupiste.html">Palvelupiste</a> 
	 */
	private Palvelupiste[] yhdistaPalvelupisteet(Palvelupiste[] pp1, Palvelupiste[] pp2) {
		if (pp1 == null && pp2 == null)
			return new Palvelupiste[0];
		int pp1Lkm = pp1.length;
		int pp2Lkm = pp2.length;
		int resultLkm = pp1Lkm + pp2Lkm;
		Palvelupiste[] result = new Palvelupiste[resultLkm];

		int j = 0;
		for (int i = 0; i < pp1Lkm; i++) {
			result[j] = pp1[i];
			j++;
		}

		for (int i = 0; i < pp2Lkm; i++) {
			result[j] = pp2[i];
			j++;
		}

		return result;
	}

	/**
	 * The method makes all needed actions, as printing, calculating etc in the end of the simulation
	 */
	@Override
	protected void tulokset() {
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());
		kontrolleri.simuloinninJalkeen();
		Set<Palvelupiste> palvelupisteSet = new HashSet<Palvelupiste>();
		Simulointi sim = new Simulointi();
		for (int i = 0; i < palvelupisteet.length; i++) {
			// HUOM: Pakko laskea suorituskykysuureet ennen raportointia
			palvelupisteet[i].laskeSuorituskykysuureet();
			palvelupisteet[i].raportti();

			// Tietokanta jutut
			palvelupisteet[i].setSimulointi(sim);
			palvelupisteSet.add(palvelupisteet[i]);
		}

		// Tallenna tiedot tietokannalle (simuloinnin meta data + suorituskykysuureet)

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		sim.setAikaleima(ts);
		sim.setRuokatiskienLkm(ruokatiskienLkm);
		sim.setKassojenLkm(kassojenLkm);
		sim.setIpKassojenLkm(ipKassojenLkm);
		sim.setAsiakkaatKerrallaMin(asiakkaatKerralla[0]);
		sim.setAsiakkaatKerrallaMax(asiakkaatKerralla[0]);
		sim.setRuokatiskinAikaMin(ruokatiskinAikaRajat[0]);
		sim.setRuokatiskinAikaMax(ruokatiskinAikaRajat[1]);
		sim.setKassanAikaMin(kassanAikaRajat[0]);
		sim.setKassanAikaMax(kassanAikaRajat[1]);
		sim.setIpKassanAikaMin(ipKassanAikaRajat[0]);
		sim.setIpKassanAikaMax(ipKassanAikaRajat[1]);

		boolean status1 = simulointiDAO.lisaaSimulointi(sim);
		boolean status2 = palvelupisteDAO.addPalvelupisteSet(palvelupisteSet);

		if (status1 == true && status2 == true)
			System.out.println("Tietojen tallennus on onnistunut");
		else {
			System.out.println("Tietojen tallennus on epäonnistunut");
		}
	}

	// Asetuksien getterit ja setterit
	/**
	 * @return food courts count
	 */
	public int getRuokatiskienLkm() {
		return ruokatiskienLkm;
	}

	/**
	 * @return checkouts count
	 */
	public int getKassojenLkm() {
		return kassojenLkm;
	}

	/**
	 * @return self-service checkouts count
	 */
	public int getIpKassojenLkm() {
		return ipKassojenLkm;
	}

	/**
	 * @return customers minimum and maximum count, which can come at one time
	 */
	public int[] getAsiakkaatKerralla() {
		return asiakkaatKerralla;
	}

	/**
	 * @return food court minimum and maximum service time
	 */
	public double[] getRuokatiskinAikaRajat() {
		return ruokatiskinAikaRajat;
	}

	/**
	 * @return checkout minimum and maximum service time
	 */
	public double[] getKassanAikaRajat() {
		return kassanAikaRajat;
	}

	/**
	 * @return self-service checkout minimum and maximum service time
	 */
	public double[] getIpKassanAikaRajat() {
		return ipKassanAikaRajat;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param ruokatiskienLkm must be over 0, otherwise value will not be set
	 */
	public void setRuokatiskienLkm(int ruokatiskienLkm) {
		if (ruokatiskienLkm >= 0)
			this.ruokatiskienLkm = ruokatiskienLkm;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param kassojenLkm must be over 0, otherwise value will not be set
	 */
	public void setKassojenLkm(int kassojenLkm) {
		if (kassojenLkm >= 0)
			this.kassojenLkm = kassojenLkm;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param ipKassojenLkm must be over 0, otherwise value will not be set
	 */
	public void setIpKassojenLkm(int ipKassojenLkm) {
		if (ipKassojenLkm >= 0)
			this.ipKassojenLkm = ipKassojenLkm;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param asiakkaatKerralla must have 2 elements, first must be smaller than second and both must be over 0, in case then it has over 2 elements first elements will be used, otherwise value will not be set
	 */
	public void setAsiakkaatKerralla(int[] asiakkaatKerralla) {
		if (asiakkaatKerralla != null && asiakkaatKerralla.length >= 2) {
			if (asiakkaatKerralla[0] >= 0 && asiakkaatKerralla[1] >= 1
					&& asiakkaatKerralla[0] <= asiakkaatKerralla[1]) {
				this.asiakkaatKerralla = asiakkaatKerralla;
			} else {
				System.err.println("setAsiakkaatKerralla(): tarkista parametri");
			}
		} else {
			System.err.println("setAsiakkaatKerralla(): parametri on null tai koko on pienempi kuin 2");
		}
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param ruokatiskinAikaRajat must have 2 elements, first must be smaller than second and both must be over 0, in case then it has over 2 elements first elements will be used, otherwise value will not be set
	 */
	public void setRuokatiskinAikaRajat(double[] ruokatiskinAikaRajat) {
		if (tarkistaRajat(ruokatiskinAikaRajat, "setRuokatiskinAikaRajat"))
			this.ruokatiskinAikaRajat = ruokatiskinAikaRajat;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param kassanAikaRajat must have 2 elements, first must be smaller than second and both must be over 0, in case then it has over 2 elements first elements will be used, otherwise value will not be set
	 */
	public void setKassanAikaRajat(double[] kassanAikaRajat) {
		if (tarkistaRajat(kassanAikaRajat, "setKassanAikaRajat"))
			this.kassanAikaRajat = kassanAikaRajat;
	}

	/**
	 * <p>OmaMoottori-object's settings setter</p>
	 * @param ipKassanAikaRajat must have 2 elements, first must be smaller than second and both must be over 0, in case then it has over 2 elements first elements will be used, otherwise value will not be set
	 */
	public void setIpKassanAikaRajat(double[] ipKassanAikaRajat) {
		if (tarkistaRajat(ipKassanAikaRajat, "setIpKassanAikaRajat"))
			this.ipKassanAikaRajat = ipKassanAikaRajat;
	}

	/**
	 * The method checks given time frames and print error if they are not given right
	 * @param rajat minimum ja maximum time frame (minimum < maximum and they both are not negative)
	 * @param metoodinNimi name of the method, where this method was called, used for generating error message
	 * @return true = right frames, false = wrong frames 
	 */
	private boolean tarkistaRajat(double[] rajat, String metoodinNimi) {
		boolean status = false;
		if (rajat != null && rajat.length >= 2) {
			if (rajat[0] >= 0 && rajat[1] >= 1 && rajat[0] <= rajat[1]) {
				status = true;
			} else {
				System.err.println(metoodinNimi + "(): tarkista parametri");
			}
		} else {
			System.err.println(metoodinNimi + "(): parametri on null tai koko on pienempi kuin 2");
		}

		return status;
	}
	
	/**
	 * The method resets program 
	 */
	public void reset() {
		Asiakas a = new Asiakas();
		Palvelupiste pp = new Palvelupiste();
		a.reset();
		pp.reset();
		super.palvelupisteet = null;
		ruokatiskiJakauma = this.getNormalGenerator(ruokatiskinAikaRajat[0], ruokatiskinAikaRajat[1]);
		kassaJakauma = this.getNormalGenerator(kassanAikaRajat[0], kassanAikaRajat[1]);
		ipKassaJakauma = this.getNormalGenerator(ipKassanAikaRajat[0], ipKassanAikaRajat[1]);
	}

}