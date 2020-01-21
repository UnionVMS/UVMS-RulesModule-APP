package eu.europa.ec.fisheries.uvms.rules.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fa_doc_id_lock")
@NamedQueries({
		@NamedQuery(name = FaDocumentIdLock.LOAD_BY_UUID, query = "FROM FaDocumentIdLock f WHERE f.uuid in (:uuids)")
})
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class FaDocumentIdLock {

	public static final String LOAD_BY_UUID = "faDocumentIdLock.loadByUuid";

	@Id
	@NonNull
	@Column(name = "uuid")
	private String uuid;
}
