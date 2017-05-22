/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.SalesFactMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class SalesReportFactGenerator extends AbstractGenerator<FLUXSalesReportMessage> {

    private FLUXSalesReportMessage fluxSalesReportMessage;
    private List<AbstractFact> facts;
    private SalesFactMapper mapper = SalesFactMapper.INSTANCE;

    @Override
    public List<AbstractFact> getAllFacts() {
        facts = new ArrayList<>();

        addFacts(forFLUXSalesReportMessage(), "FLUXSalesReportMessage");
        addFacts(forFLUXReportDocument(), "FLUXReportDocument");
        addFacts(forFLUXParty(), "FLUXParty");
        addFacts(forSalesReports(), "SalesReports");
        addFacts(forSalesDocuments(), "SalesDocument");

        return facts;
    }

    private void addFacts(Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> function, String context) {
        try {
            Collection<? extends AbstractFact> newFacts = function.apply(fluxSalesReportMessage);
            facts.addAll(newFacts);
        } catch (NullPointerException ex) {
            log.info("Could not generate facts for " + context);
        }
    }


    private Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> forFLUXSalesReportMessage() {
        return new Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>>() {
            @Override
            public Collection<? extends AbstractFact> apply(@Nullable FLUXSalesReportMessage fluxSalesReportMessage) {
                return Lists.newArrayList(mapper.generateFactForFLUXSalesReportMessage(fluxSalesReportMessage));
            }
        };
    }

    private Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> forSalesDocuments() {
        return new Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>>() {
            @Override
            public Collection<? extends AbstractFact> apply(@Nullable FLUXSalesReportMessage fluxSalesReportMessage) {
                return mapper.generateFactForSalesDocument(fluxSalesReportMessage.getSalesReports().get(0).getIncludedSalesDocuments());
            }
        };
    }

    private Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> forSalesReports() {
        return new Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>>() {
            @Override
            public Collection<? extends AbstractFact> apply(@Nullable FLUXSalesReportMessage fluxSalesReportMessage) {
                return mapper.generateFactForSalesReportFact(fluxSalesReportMessage.getSalesReports());
            }
        };
    }

    private Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> forFLUXParty() {
        return new Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>>() {
            @Override
            public Collection<? extends AbstractFact> apply(@Nullable FLUXSalesReportMessage fluxSalesReportMessage) {
                return Lists.newArrayList(mapper.generateFactForFLUXPartyFact(fluxSalesReportMessage.getFLUXReportDocument().getOwnerFLUXParty()));
            }
        };
    }

    private Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>> forFLUXReportDocument() {
        return new Function<FLUXSalesReportMessage, Collection<? extends AbstractFact>>() {
            @Override
            public Collection<? extends AbstractFact> apply(@Nullable FLUXSalesReportMessage fluxSalesReportMessage) {
                return Lists.newArrayList(mapper.generateFactForSalesFLUXReportDocumentFact(fluxSalesReportMessage.getFLUXReportDocument()));
            }
        };
    }

    @Override
    public void setBusinessObjectMessage(FLUXSalesReportMessage businessObject) throws RulesValidationException {
        this.fluxSalesReportMessage = (FLUXSalesReportMessage)businessObject;
    }
}
