package hu.szabot.yeelightapi.controller.model;

public class FlowTuple
{
	
	public enum FlowTupleMode {
	    COLOR(1), COLOR_TEMPERATURE(2),SLEEP(7);
	    private final int id;
	    FlowTupleMode(int id) { this.id = id; }
	    public int getValue() { return id; }
	}
	
	private int duration;
	private FlowTupleMode mode;
	private int value;
	private int brightness;
	
	public FlowTuple(int duration, FlowTupleMode mode, int value, int brightness) {
		this.duration = duration;
		this.mode = mode;
		this.value = value;
		this.brightness = brightness;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public FlowTupleMode getMode() {
		return mode;
	}

	public void setMode(FlowTupleMode mode) {
		this.mode = mode;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	
}