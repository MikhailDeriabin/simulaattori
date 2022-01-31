package tietokanta;

import java.util.Set;

import simu.model.Palvelupiste;

/**
* <p>This is an interface for the PalvelupisteDAO class</p>
* <p>It declares methods for saving Palvelupiste-objects information to the database using Hibernate library</p>
* 
* @see <a href="PalvelupisteDAO.html">PalvelupisteDAO</a>
* @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
* @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
* 
* @author Mikhail Deriabin
* 
*/
public interface IPalvelupisteDAO {
	/**
	 * <p>The method saves single Palvelupiste-object data to the database</p>
	 * <p>The method should use Hibernate library methods</p>
	 * <p>The method should not throw any exceptions, instead it should contains try-catch statements</p>
	 * <p>
	 * 	The method should care about all saving to the database data logic,
	 *  such as opening and closing Session, beginning and committing transaction and handling all possible exceptions
	 * </p>
	 * @param pp Palvelupiste-object, which will be saved to the database
	 * @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
	 * @return data saving status: true for successful saving, false for unsuccessful 
	 */
	boolean addPalvelupiste(Palvelupiste pp);
	
	/**
	 * <p>The method saves Set of Palvelupiste-object data to the database</p>
	 * <p>The method should use Hibernate library methods</p>
	 * <p>The method should not throw any exceptions, instead it should use try-catch statements</p>
	 * <p>
	 * 	The method should care about all saving to the database data logic,
	 *  such as opening and closing Session, beginning and committing transaction and handling all possible exceptions
	 * </p>
	 * @param ppSet Palvelupiste-objects Set, which will be saved to the database
	 * @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 * @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
	 * @return data saving status: true for successful saving, false for unsuccessful 
	 */
	boolean addPalvelupisteSet(Set<Palvelupiste> ppSet);
}
