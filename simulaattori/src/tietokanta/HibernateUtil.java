package tietokanta;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
* Class contains methods for Hibernate settings and utilities
* 
* @author Mikhail Deriabin
* 
*/
public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	/**
	 * <p>Build session factory based on hibernate.cfg.xml file</p>
	 * @see <a href="file:../hibernate.cfg.xml">hibernate.cfg.xml</a>
	 * @return session factory object
	 */
	public static SessionFactory buildSessionFactory() {
		SessionFactory sessionFactory = null;
		
		try {
			Configuration configuration = new Configuration();
			//Set configuration file
			configuration.configure("hibernate.cfg.xml");
			sessionFactory = configuration.buildSessionFactory();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return sessionFactory;
	}
	
	/**
	 * <p>Return session factory based on hibernate.cfg.xml file</p>
	 * @see <a href="file:../hibernate.cfg.xml">hibernate.cfg.xml</a>
	 * @return session factory object
	 */
	public static SessionFactory getSessionFactory() {	return sessionFactory;	}
}