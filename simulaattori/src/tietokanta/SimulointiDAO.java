package tietokanta;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
* <p>This class is a Data Access Object class for Simulointi class</p>
* <p>It contains methods for Simulointi objects processing with Hibernate library, such as saving to and getting from the database Simulointi objects' data</p>
* 
* @see <a href="Simulointi.html">Simulointi</a>
* @see <a href="https://hibernate.org/orm/documentation/5.6/">Hibernate docs</a>
* 
* @author Mikhail Deriabin
* 
*/
public class SimulointiDAO implements ISimulointiDAO{
	
	/**
	 * name of the class, for which the DAO is
	 * @see <a href="Simulointi.html">Simulointi</a>
	 */
	private final String className = "Simulointi";
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
	public SimulointiDAO() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	/**
	 * <p>The method saves single Simulointi-object data to the database</p>
	 * @param sim Simulointi-object, which will be saved to the database
	 * @see <a href="Simulointi.html">Simulointi</a>
	 * @return Data saving status: true for successful saving, false for unsuccessful 
	 */
	public boolean lisaaSimulointi(Simulointi sim) {
		boolean status = false;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			if(sim != null) {
				tx = session.beginTransaction();
				session.save(sim);
				tx.commit();
				status = true;
			} else
				System.out.println("Simulointi on null");
								
		} catch(HibernateException he) {
			System.out.println("---Simuloinnin tallentaminen epäonnistui---");
			handleHibernateError(tx);			
		} finally {
			finalizeSession();
		}
				
		return status;
	}
	
	/**
	 * <p>The method receives all Simulointi-objects' data from the database</p>
	 * @see <a href="Simulointi.html">Simulointi</a>
	 * @return List of all Simulointi-objects, which was saved to the database 
	 */
	public List<Simulointi> haeSimuloinnit() {
		List<Simulointi> result = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			//from class name (not table), ordered by timestamp  			  			
			result = session.createQuery("from " + className + " order by aikaleima desc", Simulointi.class).getResultList();
			
		}catch(HibernateException e) {
			System.out.println("---Simulointien haku epäonnistui---");
			handleHibernateError(tx);
		} finally {
			finalizeSession();
		}			
		return result;
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
			System.out.println("---Istunto ei ole saatu suljettu---");
			he.printStackTrace();
		}
	}
}
