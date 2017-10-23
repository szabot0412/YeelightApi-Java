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
	
	/**
	 * Each visible state changing is defined to be a flow tuple that contains 4 elements: [duration, mode, value, brightness]. A flow expression is a series of flow tuples.
	 * @param duration Gradual change time or sleep time, in milliseconds, minimum value 50.
	 * @param mode Tuple mode.
	 * @param value RGB value when mode is COLOR, CT value when mode is COLOR_TEMPERATURE, Ignored when mode is SLEEP.
	 * @param brightness Brightness value, -1 or 1 ~ 100. Ignored when mode is SLEEP. When this value is -1, brightness in this tuple is ignored (only color or CT change takes effect).
	 */
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