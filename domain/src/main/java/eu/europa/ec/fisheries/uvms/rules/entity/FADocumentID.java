/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.entity;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.domain.Audit;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(exclude = {"id", "audit"})
@NamedQueries({
        @NamedQuery(name = FADocumentID.LOAD_BY_UUID, query = "FROM FADocumentID f WHERE f.uuid in (:uuids)")
})
@RequiredArgsConstructor
@NoArgsConstructor
public class FADocumentID implements Serializable {

    public static final String LOAD_BY_UUID = "fADocumentID.loadByUUID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String uuid;

    @Enumerated(EnumType.STRING)
    @NonNull
    private FAUUIDType type;

    @Embedded
    private Audit audit = new Audit();

    @PrePersist
    private void prePersist() {
        audit.setCreatedOn(DateUtils.nowUTC().toDate());
    }

}
