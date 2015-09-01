package rest.service.dto;

import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        Assert.assertEquals(dto.getCode(), ResponseCode.OK.getCode());
        Assert.assertEquals(dto.getData(), VALUE);

        dto = new ResponseDto(ResponseCode.ERROR);
        Assert.assertEquals(dto.getCode(), ResponseCode.ERROR.getCode());
        Assert.assertEquals(dto.getData(), null);

    }
}
