package rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.service.RulesRestResource;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mockdata.MockData;

public class RestResourceTest {

    private static final Long ID = 1L;
    private static final String GUID = "1";
    private static final Integer CUSTOM_RULE_LIST_SIZE = 3;

    List<CustomRuleType> DTO_LIST = MockData.getDtoList(CUSTOM_RULE_LIST_SIZE);
    CustomRuleType DTO = MockData.getDto(ID);

    private final ResponseDto ERROR_RESULT;
    private final ResponseDto SUCCESS_RESULT;
    private final ResponseDto SUCCESS_RESULT_LIST;
    private final ResponseDto SUCCESS_RESULT_DTO;

    RulesRestResource SERVICE_NULL = new RulesRestResource();

    @Mock
    RulesService serviceLayer;

    @InjectMocks
    RulesRestResource rulesRestResource;

    public RestResourceTest() {
        ERROR_RESULT = new ResponseDto(null, ResponseCode.UNDEFINED_ERROR);
        SUCCESS_RESULT = new ResponseDto(null, ResponseCode.OK);
        SUCCESS_RESULT_LIST = new ResponseDto(DTO_LIST, ResponseCode.OK);
        SUCCESS_RESULT_DTO = new ResponseDto(DTO, ResponseCode.OK);
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test get list with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testGetVesselList() throws RulesServiceException {
        doReturn(DTO_LIST).when(serviceLayer).getCustomRuleList();
        ResponseDto result = rulesRestResource.getCustomRuleList();
        assertEquals(SUCCESS_RESULT_LIST.toString(), result.toString());
    }

    /**
     * Test get list when the injected EJB is null
     *
     * @throws RulesServiceException
     */
    @Test
    public void testGetVesselListNull() throws RulesServiceException {
        ResponseDto result = SERVICE_NULL.getCustomRuleList();
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test get by id with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testGetVesselByGuid() throws RulesServiceException, RulesModelMapperException, RulesFaultException {
        doReturn(DTO).when(serviceLayer).getByGuid(GUID);
        ResponseDto result = rulesRestResource.getByGuid(GUID);
        Mockito.verify(serviceLayer).getByGuid(GUID);
        assertEquals(SUCCESS_RESULT_DTO.toString(), result.toString());
    }

    /**
     * Test get by id when the injected EJB is null
     *
     * @throws RulesServiceException
     */
    @Test
    public void testGetVesselByIdNull() throws RulesServiceException {
        ResponseDto result = SERVICE_NULL.getByGuid(GUID);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test create with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testCreateVessel() throws RulesServiceException {
        ResponseDto result = rulesRestResource.create(DTO);
        Mockito.verify(serviceLayer).createCustomRule(DTO);
        assertEquals(SUCCESS_RESULT.toString(), result.toString());
    }

    /**
     * Test create when the injected EJB is null
     */
    @Test
    public void testCreateVesselNull() {
        ResponseDto result = SERVICE_NULL.create(DTO);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test update with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testUpdateVessel() throws RulesServiceException {
        ResponseDto result = rulesRestResource.update(DTO);
        Mockito.verify(serviceLayer).updateCustomRule(DTO);
        assertEquals(SUCCESS_RESULT.toString(), result.toString());
    }

    /**
     * Test update when the injected EJB is null
     */
    @Test
    public void testUpdateVesselNull() {
        ResponseDto result = SERVICE_NULL.update(DTO);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

}
