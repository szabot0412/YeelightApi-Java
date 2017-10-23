package hu.szabot.yeelightapi.model;

import java.util.Set;

public class YeelightDevice {

	public enum ModelType{MONO,COLOR,STRIPE}
	
	public enum Mode {
	    COLOR_MODE(1), COLOR_TEMPERATURE_MODE(2),HSV_MODE(3);
	    private final int id;
	    Mode(int id) { this.id = id; }
	    public int getValue() { return id; }
	    
	    public static Mode getModeByValue(int value)
	    {
	    	for(Mode m : Mode.values())
	    	{
	    		if(m.getValue()==value)
	    			return m;
	    	}
	    	
	    	return null;
	    }
	}
	
	public enum ColorFlowEndAction {
	    STAY_BEFORE(0), STAY_AFTER(1),OFF(2);
	    private final int id;
	    ColorFlowEndAction(int id) { this.id = id; }
	    public int getValue() { return id; }
	}
	
	public enum Effect{SUDDEN,SMOOTH};
	
	public enum Properties{LOCATION,ID,MODEL,FW_VER,SUPPORT,POWER,BRIGHT,COLOR_MODE,CT,RGB,HUE,SAT,NAME,FLOWING,DELAYOFF,FLOW_PARAMS,MUSIC_ON}
	
	public enum Features{GET_PROP,SET_CT_ABX,SET_RGB,SET_HSV,SET_BRIGHT,SET_POWER,TOGGLE,SET_DEFAULT,START_CF,STOP_CF,SET_SCENE,CRON_ADD,CRON_GET,CRON_DEL,SET_ADJUST,SET_MUSIC,SET_NAME}
	
	private String location;
	private String id;
	private ModelType model;
	private int firmwareVersion;
	private Set<Features> support;
	private boolean power;
	private int bright;
	private Mode colorMode;
	private int colorTemperature;
	private int rgb;
	private int hue;
	private int saturation;
	private String name;
	private String address;
	
	
	@Override
	public int hashCode() 
	{
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof YeelightDevice)
		{
			return id.equals(((YeelightDevice) obj).id);
		}
		
		return super.equals(obj);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModelType getModel() {
		return model;
	}

	public void setModel(ModelType model) {
		this.model = model;
	}

	public int getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(int firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public Set<Features> getSupport() {
		return support;
	}

	public void setSupport(Set<Features> support) {
		this.support = support;
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public int getBright() {
		return bright;
	}

	public void setBright(int bright) {
		this.bright = bright;
	}

	public Mode getColorMode() {
		return colorMode;
	}

	public void setColorMode(Mode colorMode) {
		this.colorMode = colorMode;
	}

	public int getColorTemperature() {
		return colorTemperature;
	}

	public void setColorTemperature(int colorTemperature) {
		this.colorTemperature = colorTemperature;
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public int getHue() {
		return hue;
	}

	public void setHue(int hue) {
		this.hue = hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isFeatureSupported(Features feature)
	{
		return support.contains(feature);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
