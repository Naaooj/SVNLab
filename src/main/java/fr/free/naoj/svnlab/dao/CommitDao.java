package fr.free.naoj.svnlab.dao;

import fr.free.naoj.svnlab.entity.Commit;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public interface CommitDao {

	Commit findByRevision(long revision);
}
