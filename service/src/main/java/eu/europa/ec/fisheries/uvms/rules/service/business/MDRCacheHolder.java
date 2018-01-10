/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

/**
 * Does some thing in old style.
 *
 * @deprecated use {@link #MDRCacheService()} instead.
 */
@Deprecated
@Slf4j
public class MDRCacheHolder {

    private LoadingCache<MDRAcronymType, List<ObjectRepresentation>> cache  = CacheBuilder.newBuilder()
            .refreshAfterWrite(100000000000000L,TimeUnit.DAYS)
                .maximumSize(100)
                .initialCapacity(80)
                .recordStats()
                .build(
                        new CacheLoader<MDRAcronymType, List<ObjectRepresentation>>() {
        @Override
        public List<ObjectRepresentation> load(MDRAcronymType acronymType) throws Exception {
            return Collections.singletonList(null);
        }
    });

    private MDRCacheHolder() {
        super();
    }

    private static class Holder {

        private Holder() {
            super();
        }

        private static final MDRCacheHolder INSTANCE = new MDRCacheHolder();
    }

    public static MDRCacheHolder getInstance() {
        return Holder.INSTANCE;
    }

    public void setCache(LoadingCache<MDRAcronymType, List<ObjectRepresentation>> cache){
     this.cache = cache;
    }

    public void addToCache(MDRAcronymType type, List<ObjectRepresentation> values) {
        cache.put(type, values);
    }

    public List<String> getList(MDRAcronymType type) {
        return getList(type, "code");
    }

    public List<String> getList(MDRAcronymType type, String column) {
        List<String> codeColumnValues = new ArrayList<>();
        List<ObjectRepresentation> ObjectRepresentationList = null;
        try {
            ObjectRepresentationList = cache.get(type);
        } catch (ExecutionException e) {
            log.error("Error while trying to get Entry By type in MDRCache.");
        }
        if (CollectionUtils.isEmpty(ObjectRepresentationList))
            return Collections.emptyList();
        for (ObjectRepresentation representation : ObjectRepresentationList) {
            List<ColumnDataType> columnDataTypes = representation.getFields();
            if (CollectionUtils.isEmpty(columnDataTypes)) {
                continue;
            }
            for (ColumnDataType nameVal : columnDataTypes) {
                if (column.equals(nameVal.getColumnName())) {
                    codeColumnValues.add(nameVal.getColumnValue());
                }
            }
        }
        return codeColumnValues;
    }

    public List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType type) {
        if (type == null)
            return Collections.emptyList();
        try {
            return cache.get(type);
        } catch (ExecutionException e) {
            log.error("Error while trying to get Entry By type in MDRCache.");
        }
        return null;
    }

}
