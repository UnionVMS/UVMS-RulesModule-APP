package eu.europa.ec.fisheries.uvms.rules.service.bean.responsemessagerule;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogResponseStatusEnum;
import eu.europa.ec.fisheries.uvms.rules.dao.ResponseMessageRuleDao;
import eu.europa.ec.fisheries.uvms.rules.dto.ResponseMessageRuleDto;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ResponseMessageRuleServiceBean {

    @EJB
    private ResponseMessageRuleDao messageRuleDao;


    @PostConstruct
    public void init() {
        ResponseMessageRuleProcessor.getInstance().setRules(messageRuleDao.findAll());
    }

    public ExchangeLogResponseStatusEnum applyRules(ResponseMessageRuleDto input) {
        ExchangeLogResponseStatusEnum status = ResponseMessageRuleProcessor.getInstance().process(input);
        return status;
    }
}
