package eu.europa.ec.fisheries.uvms.rules.entity;

import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQuery(name= UvmsConstants.RESPONSEMESSAGERULE_FIND_ALL, query="SELECT rule FROM ResponseMessageRule rule")
public class ResponseMessageRule implements Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name = "responsemessagerule_seq_generator", sequenceName = "responsemessagerule_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "responsemessagerule_seq_generator")
	private Long id;
	
	@Column(name="data_flow")
	private String dataFlow;

	@Column(name = "message_type")
	private String messageType;

	@Column(name = "receiver")
	private String receiver;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataFlow() {
		return dataFlow;
	}

	public void setDataFlow(String dataFlow) {
		this.dataFlow = dataFlow;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}