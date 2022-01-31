package tietokanta;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Set;

import simu.model.Palvelupiste;

/**
* <p>This class is used as a storage for simulation metadata, such as date of simulation and simulation settings.</p>
* <p>It also contains all Palvelupiste-objects information used in the simulation</p>
* <p>Objects' data of the class can be saved to the database via SimulointiDAO class</p>
* <p>This class has only getters and setters methods for it's fields</p>
* 
* @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
* @see <a href="SimulointiDAO.html">SimulointiDAO</a>
* 
* @author Mikhail Deriabin
* 
*/
public class Simulointi {

	/**
	 * <p>Parameterless constructor, which does nothing</p>
	 */
	public Simulointi(){}
	
	/**
	 * simuloinnit-table primary key (auto_increment)
	 */
	private int id;
	
	/**
	 * Palvelupiste-objects Set, which contains them data
	 * <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 */
	private Set<Palvelupiste> palvelupisteet;
	
	//FIELDS
	/**
	 * simuloinnit-table data and time field, not automatic, can be null
	 */
	private Timestamp aikaleima;
	/**
	 * simuloinnit-table food courts count field, can be null
	 */
	private int ruokatiskienLkm;
	/**
	 * simuloinnit-table checkouts count field, can be null
	 */
	private int kassojenLkm; 
	/**
	 * simuloinnit-table self-service checkouts count field, can be null
	 */
	private int ipKassojenLkm;
	/**
	 * simuloinnit-table customers minimum count, which can come at one time field, can be null
	 */
	private int asiakkaatKerrallaMin;
	/**
	 * simuloinnit-table customers maximum count, which can come at one time field, can be null
	 */
	private int asiakkaatKerrallaMax;
	/**
	 * simuloinnit-table food court minimum service time field, can be null
	 */
	private double ruokatiskinAikaMin;
	/**
	 * simuloinnit-table food court maximum service time field, can be null
	 */
	private double ruokatiskinAikaMax;
	/**
	 * simuloinnit-table checkout minimum service time field, can be null
	 */
	private double kassanAikaMin;
	/**
	 * simuloinnit-table checkout maximum service time field, can be null
	 */
	private double kassanAikaMax;
	/**
	 * simuloinnit-table self-service checkout minimum service time field, can be null
	 */
	private double ipKassanAikaMin;
	/**
	 * simuloinnit-table elf-service maximum service time field, can be null
	 */
	private double ipKassanAikaMax;
	
	/**
	 * The method change the Simulointi-object's date and time to dd.MM.yyyy HH:mm:ss format
	 * @return date and time in dd.MM.yyyy HH:mm:ss format
	 */
	public String getFormattedAikaleima() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		if(this.aikaleima != null)
			return dateFormat.format(this.aikaleima);
		else
			return "";
	}
	
	//Getterit
	/**
	 * @return object's primary key in the simuloinnit-table, can not be null
	 */
	public int getId() {	return id;	}
	/**
	 * @return simuloinnit-table data and time field, can be null
	 */
	public Timestamp getAikaleima() {	return aikaleima;	}
	/**
	 * @return simuloinnit-table food courts count field, can be null
	 */
	public int getRuokatiskienLkm() {	return ruokatiskienLkm;	}
	/**
	 * @return simuloinnit-table checkouts count field, can be null
	 */
	public int getKassojenLkm() {	return kassojenLkm;	}
	/**
	 * @return simuloinnit-table self-service checkouts count field, can be null
	 */
	public int getIpKassojenLkm() {	return ipKassojenLkm;	}
	/**
	 * @return simuloinnit-table customers minimum count, which can come at one time field, can be null
	 */
	public int getAsiakkaatKerrallaMin() {	return asiakkaatKerrallaMin;	}
	/**
	 * @return simuloinnit-table customers maximum count, which can come at one time field, can be null
	 */
	public int getAsiakkaatKerrallaMax() {	return asiakkaatKerrallaMax;	}
	/**
	 * @return simuloinnit-table food court minimum service time field, can be null
	 */
	public double getRuokatiskinAikaMin() {	return ruokatiskinAikaMin;	}
	/**
	 * @return simuloinnit-table food court maximum service time field, can be null
	 */
	public double getRuokatiskinAikaMax() {	return ruokatiskinAikaMax;	}
	/**
	 * @return simuloinnit-table checkout minimum service time field, can be null
	 */
	public double getKassanAikaMin() {	return kassanAikaMin;	}
	/**
	 * @return simuloinnit-table checkout maximum service time field, can be null
	 */
	public double getKassanAikaMax() {	return kassanAikaMax;	}
	/**
	 * @return simuloinnit-table self-service checkout minimum service time field, can be null
	 */
	public double getIpKassanAikaMin() {	return ipKassanAikaMin;	}
	/**
	 * @return simuloinnit-table self-service checkout maximum service time field, can be null
	 */
	public double getIpKassanAikaMax() {	return ipKassanAikaMax;	}
	/**
	 * <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 * @return Palvelupiste-objects Set, which contains them data
	 */
	public Set<Palvelupiste> getPalvelupisteet() {	return palvelupisteet;	}
	
	//Setterit
	/**
	 * Used by Hibernate-library only, Do not set manually. The method sets object's primary key in the simuloinnit-table
	 * @param id can not be null
	 */
	public void setId(int id) {	this.id = id;	}
	/**
	 * The method sets object's data and time field in the simuloinnit-table
	 * @param aikaleima can be null
	 */
	public void setAikaleima(Timestamp aikaleima) {	this.aikaleima = aikaleima;	}	
	/**
	 * The method sets food courts count field in the simuloinnit-table
	 * @param ruokatiskienLkm can be null
	 */
	public void setRuokatiskienLkm(int ruokatiskienLkm) {	this.ruokatiskienLkm = ruokatiskienLkm;	}
	/**
	 * The method sets checkouts count field in the simuloinnit-table
	 * @param kassojenLkm can be null
	 */
	public void setKassojenLkm(int kassojenLkm) {	this.kassojenLkm = kassojenLkm;	}
	/**
	 * The method sets self-service checkouts count field in the simuloinnit-table
	 * @param ipKassojenLkm can be null
	 */
	public void setIpKassojenLkm(int ipKassojenLkm) {	this.ipKassojenLkm = ipKassojenLkm;	}
	/**
	 * The method sets simuloinnit-table customers minimum count, which can come at one time field
	 * @param asiakkaatKerrallaMin can be null
	 */
	public void setAsiakkaatKerrallaMin(int asiakkaatKerrallaMin) {	this.asiakkaatKerrallaMin = asiakkaatKerrallaMin;	}
	/**
	 * The method sets simuloinnit-table customers maximum count, which can come at one time field
	 * @param asiakkaatKerrallaMax can be null
	 */
	public void setAsiakkaatKerrallaMax(int asiakkaatKerrallaMax) {	this.asiakkaatKerrallaMax = asiakkaatKerrallaMax;	}
	/**
	 * The method sets simuloinnit-table food court minimum service time field
	 * @param ruokatiskinAikaMin can be null
	 */
	public void setRuokatiskinAikaMin(double ruokatiskinAikaMin) {	this.ruokatiskinAikaMin = ruokatiskinAikaMin;	}
	/**
	 * The method sets simuloinnit-table food court maximum service time field
	 * @param ruokatiskinAikaMax can be null
	 */
	public void setRuokatiskinAikaMax(double ruokatiskinAikaMax) {	this.ruokatiskinAikaMax = ruokatiskinAikaMax;	}
	/**
	 * The method sets simuloinnit-table checkouts minimum service time field
	 * @param kassanAikaMin can be null
	 */
	public void setKassanAikaMin(double kassanAikaMin) {	this.kassanAikaMin = kassanAikaMin;	}
	/**
	 * The method sets simuloinnit-table checkouts maximum service time field
	 * @param kassanAikaMax can be null
	 */
	public void setKassanAikaMax(double kassanAikaMax) {	this.kassanAikaMax = kassanAikaMax;	}
	/**
	 * The method sets simuloinnit-table self-service checkouts minimum service time field
	 * @param ipKassanAikaMin can be null
	 */
	public void setIpKassanAikaMin(double ipKassanAikaMin) {	this.ipKassanAikaMin = ipKassanAikaMin;	}
	/**
	 * The method sets simuloinnit-table self-service checkouts maximum service time field
	 * @param ipKassanAikaMax can be null
	 */
	public void setIpKassanAikaMax(double ipKassanAikaMax) {	this.ipKassanAikaMax = ipKassanAikaMax;	}
	
	/**
	 * The method sets Palvelupiste-objects Set, which contains them data
	 * @param palvelupisteet can be null
	 * <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 */
	public void setPalvelupisteet(Set<Palvelupiste> palvelupisteet) {	this.palvelupisteet = palvelupisteet;	}
	
	/**
	 * The method overrides toString() method to form "Simulointi " + its id and date and time
	 */
	@Override
	public String toString() {
		return "Simulointi " + this.id + " pvm: " + getFormattedAikaleima();
	}
}
