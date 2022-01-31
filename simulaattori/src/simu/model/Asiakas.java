package simu.model;

import simu.framework.Kello;
import simu.framework.Trace;


/**
* <p>This class is one of the key classes in simulation process</p>
* <p>Asiakas aka customer, or entity, which moves in the simulation process</p>
* <p>The objects is created in the OmaMoottori-object and "get service" in different Palvelupiste-objects(aka service points)</p>
* <p>It is also used as a storage for a simulation data, for example moment of creation</p>
* 
* @see <a href="Palvelupiste.html">Palvelupiste</a>
* @see <a href="OmaMoottori.html">OmaMoottori</a>
* 
* @author Joona Nylander
* 
*/
public class Asiakas {
	/**
	 * Moment of the object creation (program inner time)
	 */
	private double saapumisaika;
	/**
	 * Moment of the adding to Palvelupiste-object's customer line (program inner time)
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 */
	private double jonoonSaapumisAika = 0;
	/**
	 *  Moment of the object removing from the simulation (program inner time)
	 */
	private double poistumisaika;
	/**
	 * Object's unique number
	 */
	private int id;
	/**
	 * The next object's unique number, used for generating unique number
	 */
	private static int i = 1;
	/**
	 * Object's total time in the simulation
	 */
	private static long sum = 0;
	
	/**
	 * Parameterless constructor, which defines object's unique number
	 */
	public Asiakas(){
	    id = i++;
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika);
	}

	/**
	 * @return Moment of the object removing from the simulation (program inner time)
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * The method sets moment of the object removing from the simulation (program inner time)
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	/**
	 * @return Moment of the object creation (program inner time)
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}

	/**
	 * The method sets moment of the object creation (program inner time)
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	/**
	 * @return object's unique number
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return Moment of the adding to Palvelupiste-object's customer line (program inner time)
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 */
	public double getJonoonSaapumisAika() {
		return jonoonSaapumisAika;
	}

	/**
	 * The method sets moment of the adding to Palvelupiste-object's customer line (program inner time)
	 * @see <a href="Palvelupiste.html">Palvelupiste</a>
	 */
	public void setJonoonSaapumisAika(double jonoonSaapumisAika) {
		this.jonoonSaapumisAika = jonoonSaapumisAika;
	}
	
	//@return time of Asiakas-object beeing in a simulation
	public double getKokonaisaika() {
		return poistumisaika - saapumisaika;
	}
	
	/**
	 * The method resets needed static fields for program proper work
	 */
	public void reset() {
		i = 1;
		sum = 0;
		Kello.getInstance().setAika(0);
		System.out.println("Kello: " + Kello.getInstance().getAika());
	}

	/**
	 * The method prints a report to the console about object
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi: " +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}

}
