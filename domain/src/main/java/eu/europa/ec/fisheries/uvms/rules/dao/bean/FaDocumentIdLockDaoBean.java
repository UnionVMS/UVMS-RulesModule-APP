package eu.europa.ec.fisheries.uvms.rules.dao.bean;

import eu.europa.ec.fisheries.uvms.rules.dao.FaDocumentIdLockDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FaDocumentIdLock;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@Stateless
public class FaDocumentIdLockDaoBean implements FaDocumentIdLockDao {

	private static final Map<String,Object> LOCK_TIMEOUT = Collections.singletonMap("javax.persistence.lock.timeout", 15000);

	@PersistenceContext(unitName = "rulesPostgresPU")
	public EntityManager em;

	@TransactionAttribute(REQUIRES_NEW)
	@Override
	public void takeNoteOfDocumentIdInNewTx(String documentId) {
		FaDocumentIdLock lock = em.find(FaDocumentIdLock.class, documentId);
		if( lock == null ) {
			String name = Thread.currentThread().getName();
			em.persist(new FaDocumentIdLock(documentId,name));
		}

	}

	@Override
	public void lock(String documentId) {
		FaDocumentIdLock lock = em.find(FaDocumentIdLock.class, documentId, LockModeType.PESSIMISTIC_WRITE,LOCK_TIMEOUT);

		if( lock == null ) {
			throw new IllegalStateException("lock " + documentId + " should have been created");
		}

		//Multithread issue. This bad fix was added for messages which had the same uuid but somehow they where persisted in the db even though pessimistic_write and pessimistic_force_increment was tried.
		//Also the previously added zero lock timeout, was causing many messages to fail in production and as a result they stayed in issued state and where not processed.
		if(!Thread.currentThread().getName().equals(lock.getThread())){
            try {
	            Random r = new Random();
                Thread.sleep(r.nextInt(200) + 300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

	}
}
