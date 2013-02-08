package fr.free.naoj.svnlab.service;

import fr.free.naoj.svnlab.entity.Commit;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public interface CommitService {

	Commit findByRevision(long revision);
}
