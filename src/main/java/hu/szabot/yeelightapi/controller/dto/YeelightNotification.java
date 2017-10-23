package hu.szabot.yeelightapi.controller.dto;

import java.util.Map;

public class YeelightNotification 
{
	private String method;
	private Map<String,String> params;
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}
