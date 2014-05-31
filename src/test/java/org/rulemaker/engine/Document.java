package org.rulemaker.engine;

public class Document {
	
	private Double amount;
	private String documentType;
	private Integer clientId;
	
	public Document(Double amount, String documenType, Integer clientId) {
		super();
		this.amount = amount;
		this.documentType = documenType;
		this.clientId = clientId;
	}

	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getClientId() {
		return clientId;
	}
	
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
}
