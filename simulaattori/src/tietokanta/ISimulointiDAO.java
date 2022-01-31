package tietokanta;

import java.util.List;

/**
* <p>This is an interface for the SimulointiDAO class</p>
* <p>It declares methods for saving and receiving Simulointi-objects information to the database using Hibernate library</p>
* 
* @see <a href="SimulointiDAO.html">SimulointiDAO</a>
* @see <a href="Simulointi.html">Simulointi</a>
* @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
* 
* @author Mikhail Deriabin
* 
*/
public interface ISimulointiDAO {
	/**
	 * <p>The method saves single Simulointi-object data to the database</p>
	 * <p>The method should use Hibernate library methods</p>
	 * <p>The method should not throw any exceptions, instead it should contains try-catch statements</p>
	 * <p>
	 * 	The method should care about all saving to the database data logic,
	 *  such as opening and closing Session, beginning and committing transaction and handling all possible exceptions
	 * </p>
	 * @param sim Simulointi-object, which will be saved to the database
	 * @see <a href="Simulointi.html">Simulointi</a>
	 * @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
	 * @return data saving status: true for successful saving, false for unsuccessful 
	 */
	boolean lisaaSimulointi(Simulointi sim);
	
	/**
	 * <p>The method receives all Simulointi-objects' data from the database</p>
	 * <p>The method should use Hibernate library methods</p>
	 * <p>The method should not throw any exceptions, instead it should contains try-catch statements</p>
	 * <p>
	 * 	The method should care about all saving to the database data logic,
	 *  such as opening and closing Session, beginning and committing transaction and handling all possible exceptions
	 * </p>
	 * <p>Also the method should care about needed Simulointi-objects order</p>
	 * @see <a href="Simulointi.html">Simulointi</a>
	 * @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
	 * @return List of all Simulointi-objects, which was saved to the database
	 */
	List<Simulointi> haeSimuloinnit();
}
