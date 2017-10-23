package hu.szabot.yeelightapi.controller.dto;

public class YeelightResponse extends YeelightCommandBase{

	private Object[] result;
	private YeelightError error;
	
	public Object[] getResult() {
		return result;
	}
	
	public void setResult(Object[] result) {
		this.result = result;
	}

	public YeelightError getError() {
		return error;
	}

	public void setError(YeelightError error) {
		this.error = error;
	}
	
}
