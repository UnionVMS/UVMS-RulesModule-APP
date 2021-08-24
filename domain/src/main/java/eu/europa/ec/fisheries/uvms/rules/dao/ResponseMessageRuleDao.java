package eu.europa.ec.fisheries.uvms.rules.dao;

import eu.europa.ec.fisheries.uvms.rules.entity.ResponseMessageRule;
import javax.ejb.Local;
import java.util.List;


@Local
public interface ResponseMessageRuleDao {

    List<ResponseMessageRule> findAll();

}