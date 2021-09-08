package eu.europa.ec.fisheries.uvms.rules.dao.bean;

import eu.europa.ec.fisheries.uvms.rules.dao.FaDocumentIdLockDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FaDocumentIdLock;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.Map;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@Stateless
public class FaDocumentIdLockDaoBean implements FaDocumentIdLockDao {

	private static final Map<String,Object> ZERO_LOCK_TIMEOUT = Collections.singletonMap("javax.persistence.lock.timeout", 0);

	@PersistenceContext(unitName = "rulesPostgresPU")
	public EntityManager em;

	@TransactionAttribute(REQUIRES_NEW)
	@Override
	public void takeNoteOfDocumentIdInNewTx(String documentId) {
		FaDocumentIdLock lock = em.find(FaDocumentIdLock.class, documentId);
		if( lock == null ) {
			em.persist(new FaDocumentIdLock(documentId));
		}
	}

	@Override
	public void lock(String documentId) {
		FaDocumentIdLock lock = em.find(FaDocumentIdLock.class, documentId, LockModeType.PESSIMISTIC_WRITE);
		if( lock == null ) {
			throw new IllegalStateException("lock " + documentId + " should have been created");
		}
	}
}
