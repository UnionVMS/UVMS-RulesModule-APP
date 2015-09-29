package rest.service.dto;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;

public class ResponseTest {

    public ResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void checkDtoReturnsValid() {

        String VALUE = "HELLO_DTO";
        ResponseDto dto = new ResponseDto(VALUE, ResponseCode.OK);
        Assert.assertEquals(dto.getCode().intValue(), ResponseCode.OK.getCode());
        Assert.assertEquals(VALUE, dto.getData());

        dto = new ResponseDto(VALUE, ResponseCode.UNDEFINED_ERROR);
        Assert.assertEquals(dto.getCode().intValue(), ResponseCode.UNDEFINED_ERROR.getCode());
        Assert.assertEquals(VALUE, dto.getData());

    }
}
