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

package eu.europa.ec.fisheries.uvms.rules.mapper;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMessageType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.rules.entity.MessageId;
import eu.europa.ec.fisheries.uvms.rules.entity.RawMessage;
import eu.europa.ec.fisheries.uvms.rules.entity.ValidationMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * Created by padhyad on 5/5/2017.
 */
@Mapper(uses = CustomRuleMapper.class)
public interface RawMessageMapper {

    RawMessageMapper INSTANCE = Mappers.getMapper(RawMessageMapper.class);

    @Mappings({
            @Mapping(target = "rawMessage", source = "message"),
            @Mapping(target = "validationMessages", source = "validationMessage")
    })
    RawMessage mapToRawMessageEntity(RawMessageType rawMessageType);

    @Mappings({
            @Mapping(target = "messageIds", source = "messageId")
    })
    ValidationMessage mapToValidationMessageEntity(ValidationMessageType validationMessageType);

    @Mappings({
            @Mapping(target = "messageId", source = "messageIds")
    })
    ValidationMessageType mapToValidationMessageType(ValidationMessage validationMessage);

    List<ValidationMessageType> mapToValidationMessageTypes(List<ValidationMessage> validationMessage);

    @Mappings({
            @Mapping(target = "messageId", source = "message")
    })
    MessageId mapToMessageIdEntity(String message);

    List<MessageId> mapToMessageIdEntities(List<String> messages);

    List<String> mapToMessageId(List<MessageId> message);

    Collection<ValidationMessage> mapToValidationMessageEntities(Collection<ValidationMessageType> validationMessageTypes);
}
