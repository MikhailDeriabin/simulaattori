package tietokanta;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import simu.model.Palvelupiste;

/**
* <p>This class is a Data Access Object class for Palvelupiste class</p>
* <p>It contains methods for Palvelupiste objects processing with Hibernate library, such as saving Palvelupiste objects information to the database</p>
* 
* @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
* @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
* 
* @author Mikhail Deriabin
* 
*/
public class PalvelupisteDAO implements IPalvelupisteDAO{
	/**
	 * Session factory object, initialized in the constructor
	 * @see <a href="https://docs.jboss.org/hibernate/orm/5.6/javadocs/">Hibernate docs</a>
	 */
	private SessionFactory sessionFactory;
	/**
	 * Session object, initialized in each method, where connection to the DB needed
	 * @see <a href="https://docs.jboss.org/hibernate/orm/5.6/javadocs/">Hibernate docs</a>
	 */
	private Session session;

	/**
	 * <p>Parameterless constructor, which initialize SessionFactory object</p>
	 * @see <a href="https://docs.jboss.org/hibernate/orm/5.6/javadocs/">Hibernate docs</a>
	 */
	public PalvelupisteDAO() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	/**
	 * <p>The method saves single Palvelupiste-object data to the database</p>
	 * @param pp Palvelupiste-object, which will be saved to the database
	 * @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 * @return Data saving status: true for successful saving, false for unsuccessful 
	 */
	public boolean addPalvelupiste(Palvelupiste pp) {
		boolean status = false;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			if(pp != null) {
				tx = session.beginTransaction();
				session.save(pp);
				tx.commit();
				status = true;
			} else
				System.out.println("Palvelupiste on null");
								
		} catch(HibernateException he) {
			System.out.println("---Palvelupisteen tallentaminen epäonnistui---");
			handleHibernateError(tx);			
		} finally {
			finalizeSession();
		}
		return status;
	}
	
	/**
	 * <p>The method saves Palvelupiste-object Set data to the database</p>
	 * @param ppSet Palvelupiste-object Set, which will be saved to the database
	 * @see <a href="../simu/model/Palvelupiste.html">Palvelupiste</a>
	 * @return Data saving status: true only if all set was successfully saved 
	 */
	public boolean addPalvelupisteSet(Set<Palvelupiste> ppSet) {
		boolean status = true;
		for(Palvelupiste pp : ppSet) {
			if(status == true)
				status = addPalvelupiste(pp);
			else 
				addPalvelupiste(pp);
		}
		
		return status;
	}

	/**
	 * <p>The method roll back a transaction, called on the HibernateException occurring</p>
	 * @see <a href="https://docs.jboss.org/hibernate/orm/5.6/javadocs/">Hibernate docs</a>
	 */
	private void handleHibernateError(Transaction tx) {
		if(tx != null)
			tx.rollback();
	}
	
	/**
	 * <p>The method close a session, called each time, when connection to the DB was established</p>
	 * @see <a href="https://docs.jboss.org/hibernate/orm/5.6/javadocs/">Hibernate docs</a>
	 */
	private void finalizeSession() {
		try {
			if(session != null)
				session.close();
		}catch(HibernateException he) {
			System.out.println("---Istunto ei ole saatu suljettamaan---");
			he.printStackTrace();
		}
	}
}
