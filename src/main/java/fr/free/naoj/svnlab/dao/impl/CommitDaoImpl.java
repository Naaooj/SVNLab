package fr.free.naoj.svnlab.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.free.naoj.svnlab.dao.CommitDao;
import fr.free.naoj.svnlab.entity.Commit;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
@Repository
public class CommitDaoImpl implements CommitDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Commit findByRevision(long revision) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Commit c where c.revisionNumber = :revision");
		query.setParameter("revision", revision);

		Commit c = null;
		
		try {
			c = (Commit) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}
}
