/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */


package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.jms.JMSException;
import javax.xml.bind.UnmarshalException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.RulesResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RulesResponseConsumerBeanTest {

    @InjectMocks private RulesResponseConsumerBean rulesResponseConsume;

    @Before
    public void before(){

    }

    @Test(expected = ConfigMessageException.class)
    public void testGetConfigMessage() throws UnmarshalException, RulesValidationException, MessageException, JMSException, ConfigMessageException {
        rulesResponseConsume.getConfigMessage("", String.class);
    }

    @Test(expected = MessageException.class)
    public void testGetMessage() throws UnmarshalException, RulesValidationException, MessageException, JMSException, ConfigMessageException
    {
        rulesResponseConsume.getMessage("", String.class);
    }

}
