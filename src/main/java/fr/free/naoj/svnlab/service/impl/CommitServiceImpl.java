package fr.free.naoj.svnlab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.free.naoj.svnlab.dao.CommitDao;
import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.service.CommitService;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
@Service
public class CommitServiceImpl implements CommitService {

	@Autowired
	private CommitDao commitDao;
	
	@Transactional
	public Commit findByRevision(long revision) {
		return commitDao.findByRevision(revision);
	}
}
