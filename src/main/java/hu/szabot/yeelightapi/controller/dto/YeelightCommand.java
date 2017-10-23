package hu.szabot.yeelightapi.controller.dto;

public class YeelightCommand extends YeelightCommandBase{

	private String method;
	private Object[] params;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	
}
