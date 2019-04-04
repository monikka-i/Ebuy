package io.doll.Dollar.Response;

public class OutputResponse {
	private int outputCode;
	private String outputStatus;
	private String message;
	private Object data;
	public int getOutputCode() {
		return outputCode;
	}
	public void setOutputCode(int outputCode) {
		this.outputCode = outputCode;
	}
	public String getOutputStatus() {
		return outputStatus;
	}
	public void setOutputStatus(String outputStatus) {
		this.outputStatus = outputStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public OutputResponse() {
		this.outputCode = 400;
		this.outputStatus = "failure";
		this.message = "no content found";
		this.data = "";
	}
	public OutputResponse(int outputCode, String outputStatus, String message, Object data) {
		this.outputCode = outputCode;
		this.outputStatus = outputStatus;
		this.message = message;
		this.data = data;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
