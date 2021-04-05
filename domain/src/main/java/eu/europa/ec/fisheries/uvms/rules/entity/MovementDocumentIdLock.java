package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "movement_doc_id_lock")
@NamedQueries({
		@NamedQuery(name = MovementDocumentIdLock.LOAD_BY_UUID, query = "FROM MovementDocumentIdLock f WHERE f.uuid in (:uuids)")
})
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class MovementDocumentIdLock {

	public static final String LOAD_BY_UUID = "MovementDocumentIdLock.loadByUuid";

	@Id
	@NonNull
	@Column(name = "uuid")
	private String uuid;
}
