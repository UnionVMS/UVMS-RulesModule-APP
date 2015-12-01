package rest.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.service.CustomRulesRestResource;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mockdata.MockData;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.jms.JMSException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

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

    CustomRulesRestResource SERVICE_NULL = new CustomRulesRestResource();

    @Mock
    RulesService rulesService;

    @Mock
    ValidationService validationService;

    @InjectMocks
    CustomRulesRestResource customRulesRestResource;

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
    public void testGetCustomRuleList() throws RulesServiceException, RulesFaultException, JMSException {
        doReturn(DTO_LIST).when(validationService).getAllCustomRules();
        ResponseDto result = customRulesRestResource.getAllCustomRules();
        assertEquals(SUCCESS_RESULT_LIST.toString(), result.toString());
    }

    /**
     * Test get list when the injected EJB is null
     *
     * @throws RulesServiceException
     */
    @Test
    public void testGetCustomRuleListNull() throws RulesServiceException {
        ResponseDto result = SERVICE_NULL.getAllCustomRules();
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test get by id with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testCustomRuleByGuid() throws RulesServiceException, RulesModelMapperException, RulesFaultException {
        doReturn(DTO).when(rulesService).getCustomRuleByGuid(GUID);
        ResponseDto result = customRulesRestResource.getCustomRuleByGuid(GUID);
        Mockito.verify(rulesService).getCustomRuleByGuid(GUID);
        assertEquals(SUCCESS_RESULT_DTO.toString(), result.toString());
    }

    /**
     * Test get by id when the injected EJB is null
     *
     * @throws RulesServiceException
     */
    @Test
    public void testCustomRuleByGuidNull() throws RulesServiceException {
        ResponseDto result = SERVICE_NULL.getCustomRuleByGuid(GUID);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test create with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testCreateCustomRule() throws RulesServiceException, RulesFaultException {
        ResponseDto result = customRulesRestResource.create(DTO);
        Mockito.verify(rulesService).createCustomRule(DTO);
        assertEquals(SUCCESS_RESULT.toString(), result.toString());
    }

    /**
     * Test create when the injected EJB is null
     */
    @Test
    public void testCreateCustomRuleNull() {
        ResponseDto result = SERVICE_NULL.create(DTO);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

    /**
     * Test update with a happy outcome
     *
     * @throws RulesServiceException
     */
    @Test
    public void testUpdateCustomRule() throws RulesServiceException, RulesFaultException {
        ResponseDto result = customRulesRestResource.update(DTO);
        Mockito.verify(rulesService).updateCustomRule(DTO);
        assertEquals(SUCCESS_RESULT.toString(), result.toString());
    }

    /**
     * Test update when the injected EJB is null
     */
    @Test
    public void testUpdateCustomRuleNull() {
        ResponseDto result = SERVICE_NULL.update(DTO);
        assertEquals(ERROR_RESULT.toString(), result.toString());
    }

}
