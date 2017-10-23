package hu.szabot.yeelightapi.controller.dto;

public class YeelightCommandBase {


	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof YeelightCommandBase)
		{
			return ((YeelightCommandBase) obj).getId()==getId();
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getId();
	}
}
