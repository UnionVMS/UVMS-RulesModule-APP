/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * Created by kovian on 19/06/2017.
 */
public class XPathRepository {

    public static final XPathRepository INSTANCE = new XPathRepository();

    private Map<Integer, Map<String, String>> xpathsMap = new ConcurrentHashMap<>();

    private Integer sequence = 10000;

    private XPathRepository(){
        super();
    }

    public void addToMap(Integer sequence, String propName, String xpath){
        Map<String, String> xpathSingleMap = xpathsMap.get(sequence);
        if(MapUtils.isNotEmpty(xpathSingleMap)){
            xpathSingleMap.put(propName.toLowerCase(), xpath);
        } else {
            Map<String, String> newXpathMap = new HashMap<>();
            newXpathMap.put(propName.toLowerCase(), xpath);
            xpathsMap.put(sequence, newXpathMap);
        }
    }

    public void clear(Collection<AbstractFact> facts){
        if(CollectionUtils.isNotEmpty(facts)){
            for(AbstractFact fact : facts){
                xpathsMap.remove(fact.getSequence());
            }
        }
        sequence = 10000;
    }

    public void clearFactsWithSequences(Collection<Integer> factSequenceList){
        if(CollectionUtils.isNotEmpty(factSequenceList)){
            for(Integer factSequence : factSequenceList){
                xpathsMap.remove(factSequence);
            }
        }
        sequence = 10000;
    }

    public String getMapForSequence(Integer sequence, String propName) {
        String xpath = null;
        Map<String, String> propsMap = INSTANCE.getXpathsMap().get(sequence);
        if(MapUtils.isNotEmpty(propsMap)){
            xpath = propsMap.get(propName.toLowerCase());
        }
        return xpath;
    }

    public Map<String, String> getMapForSequence(Integer sequence){
        return xpathsMap.get(sequence);
    }

    public Integer getNewSequence(){
        sequence++;
        return  sequence;
    }


    public Map<Integer, Map<String, String>> getXpathsMap() {
        return xpathsMap;
    }
}
