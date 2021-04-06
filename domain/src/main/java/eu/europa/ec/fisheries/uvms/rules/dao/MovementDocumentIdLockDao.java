package eu.europa.ec.fisheries.uvms.rules.dao;

import javax.ejb.Local;

@Local
public interface MovementDocumentIdLockDao {
	void takeNoteOfDocumentIdInNewTx(String documentId);

	void lock(String documentId);
}