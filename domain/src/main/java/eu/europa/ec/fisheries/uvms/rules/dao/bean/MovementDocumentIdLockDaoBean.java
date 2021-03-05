package eu.europa.ec.fisheries.uvms.rules.dao.bean;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.dao.MovementDocumentIdLockDao;
import eu.europa.ec.fisheries.uvms.rules.entity.MovementDocumentIdLock;

@Stateless
public class MovementDocumentIdLockDaoBean implements MovementDocumentIdLockDao {

	private static final Map<String,Object> ZERO_LOCK_TIMEOUT = Collections.singletonMap("javax.persistence.lock.timeout", 0);

	@PersistenceContext(unitName = "rulesPostgresPU")
	public EntityManager em;

	@TransactionAttribute(REQUIRES_NEW)
	@Override
	public void takeNoteOfDocumentIdInNewTx(String documentId) {
		MovementDocumentIdLock lock = em.find(MovementDocumentIdLock.class, documentId);
		if( lock == null ) {
			em.persist(new MovementDocumentIdLock(documentId));
		}
	}

	@Override
	public void lock(String documentId) {
		MovementDocumentIdLock lock = em.find(MovementDocumentIdLock.class, documentId, LockModeType.PESSIMISTIC_WRITE, ZERO_LOCK_TIMEOUT);
		if( lock == null ) {
			throw new IllegalStateException("lock " + documentId + " should have been created");
		}
	}
}
