/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by kovian on 14/06/2017.
 */
public class XPathStringWrapper {

    public static final XPathStringWrapper INSTANCE = new XPathStringWrapper();

    private static StringBuffer strBuff = new StringBuffer();

    private final static String LOCAL_NODE_START = "//*[local-name()='";
    private final static String LOCAL_NODE_END = "']";

    private static final String SQUARE_BRACKET_START = "[";
    private static final String SQUARE_BRACKET_END = "]";

    private static final String PARENTHESIS_OPEN = "(";
    private static final String PARENTHESIS_CLOSE = ")";

    /**
     * Singleton. Single instance.
     */
    private XPathStringWrapper(){
        super();
    }

    /**
     * Appends a string to the buffer without wrapping.
     *
     * @param stringsToAppend
     * @return
     */
    public XPathStringWrapper appendWithoutWrapping(String stringsToAppend){
        strBuff.append(stringsToAppend);
        return INSTANCE;
    }

    /**
     * Appends a string to the buffer wrapping it with the "local-name()" xpath notation.
     *
     * @param stringsToAppend
     * @return
     */
    public XPathStringWrapper append(String... stringsToAppend){
        if(stringsToAppend != null && StringUtils.isNotEmpty(stringsToAppend[0])){
            for(String strToAppend : stringsToAppend){
                strBuff.append(LOCAL_NODE_START).append(strToAppend).append(LOCAL_NODE_END);
            }
        }
        return INSTANCE;
    }

    /**
     * Appends a string to the buffer wrapping it with the "local-name()" xpath notation and an index.
     *
     * @param strToAppend
     * @param index
     * @return
     */
    public XPathStringWrapper appendWithIndex(String strToAppend, int index){
        append(strToAppend);
        String actualValue = getValue();
        strBuff.append(PARENTHESIS_OPEN).append(actualValue).append(PARENTHESIS_CLOSE).append(SQUARE_BRACKET_START).append(index).append(SQUARE_BRACKET_END);
        return INSTANCE;
    }

    /**
     * Return the string value appended so far and clears the buffer.
     *
     * @return
     */
    public String getValue(){
        if(strBuff.length() == 0){
            return StringUtils.EMPTY;
        }
        String resultingString = strBuff.toString();
        clear();
        return resultingString;
    }

    /**
     * Stores the xPath string in the XPathRepository.
     * After this operation the String buffer will be cleared.
     *
     * @param fact
     * @param key
     */
    public void storeInRepo(AbstractFact fact, String key){
        if(fact.getSequence() == 0){
            fact.setSequence(XPathRepository.INSTANCE.getNewSequence());
        }
        XPathRepository.INSTANCE.addToMap(fact.getSequence(), key, INSTANCE.getValue());
    }

    /**
     * Clears the strBuffer of this wrapper.
     *
     */
    public XPathStringWrapper clear(){
        if(strBuff.length() > 0){
            strBuff.delete(0, strBuff.length());
        }
        return INSTANCE;
    }
}
