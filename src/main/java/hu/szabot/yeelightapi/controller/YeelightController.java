package hu.szabot.yeelightapi.controller;

import java.util.List;

import hu.szabot.yeelightapi.controller.model.FlowTuple;
import hu.szabot.yeelightapi.model.YeelightDevice;
import hu.szabot.yeelightapi.model.YeelightDevice.ColorFlowEndAction;
import hu.szabot.yeelightapi.model.YeelightDevice.Effect;
import hu.szabot.yeelightapi.model.YeelightDevice.Features;
import hu.szabot.yeelightapi.model.YeelightDevice.Properties;
import hu.szabot.yeelightapi.parser.Parser;

public class YeelightController extends BaseController{

	public enum SceneType{
		
		/**
		 * means change the smart LED to specified color and brightness
		 */
		COLOR, 
		
		/**
		 * means change the smart LED to specified color and brightness.
		 */
		HSV, 
		
		/**
		 * means change the smart LED to specified ct and brightness.
		 */
		CT, 
		
		/**
		 * means start a color flow in specified fashion.
		 */
		CF, 
		
		/**
		 * means turn on the smart LED to specified brightness and start a sleep timer to turn off the light after the specified minutes.
		 */
		AUTO_DELAY_OFF
	}
	
	public enum AdjustAction{
		
		/**
		 * increase the specified property
		 */
		INCREASE,
		
		/**
		 * decrease the specified property
		 */
		DECREASE,
		
		/**
		 * increase the specified property, after it reaches the max value, go back to minimum value
		 */
		CIRCLE
	}
	
	public enum AdjustType{
		
		/**
		 * adjust brightness
		 */
		BRIGHT,
		
		/**
		 * adjust color temperature
		 */
		CT,
		
		/**
		 * adjust color. (When “prop" is “color", the “action" can only be “circle", otherwise, it will be deemed as invalid request.)
		 */
		COLOR
	}
	
	
	public YeelightController(YeelightDevice device) {
		super(device);
	}

	/**
	 * This method is used to toggle the smart LED.
	 * @param listener is the response listener.
	 */
	public void toggle(OnCommandListener listener)
	{
		sendCommand(Features.TOGGLE,listener);
	}
	
	/**
	 * This method is used to switch on or off the smart LED (software managed on/off).
	 * @param power turn on or off the smart LED.
	 * @param method support two values: "sudden" and "smooth". If effect is "sudden", then the color temperature will be changed directly to target value, under this case, the third parameter "duration" is ignored. If effect is "smooth", then the color temperature will be changed to target value in a gradual fashion, under this case, the total time of gradual change is specified in third parameter "duration".
	 * @param duration specifies the total time of the gradual changing. The unit is milliseconds. The minimum support duration is 30 milliseconds.
	 * @param listener is the response listener.
	 */
	public void setPower(boolean power, Effect method,int duration,OnCommandListener listener)
	{
		sendCommand(Features.SET_POWER,listener,Parser.boolToString(power),method.name().toLowerCase(),duration);
	}
	
	/**
	 * This method is used to change the color temperature of a smart LED.
	 * @param value is the target color temperature. The type is integer and range is 1700 ~ 6500 (k).
	 * @param method support two values: "sudden" and "smooth". If effect is "sudden", then the color temperature will be changed directly to target value, under this case, the third parameter "duration" is ignored. If effect is "smooth", then the color temperature will be changed to target value in a gradual fashion, under this case, the total time of gradual change is specified in third parameter "duration".
	 * @param duration specifies the total time of the gradual changing. The unit is milliseconds. The minimum support duration is 30 milliseconds.
	 * @param listener is the response listener.
	 */
	public void changeColorTemperature(int value, Effect method,int duration,OnCommandListener listener)
	{
		if(value<1700 || value>6500) 
		{
			throw new IllegalArgumentException();
		}

		sendCommand(Features.SET_CT_ABX,listener,value,method.name().toLowerCase(),duration);
	}
	
	/**
	 * The properties is a list of property names and the response contains a list of corresponding property values. If the requested property name is not recognized by smart LED, then a empty string value ("") will be returned.
	 * @param properties is the list of property names.
	 * @param listener is the response listener.
	 * @throws FeatureNotSupportedException
	 */
	public void getProps(Properties[] properties,OnCommandListener listener) throws FeatureNotSupportedException
	{
		Object[] props=new String[properties.length];
		
		for(int i=0;i<properties.length;i++) 
		{
			props[i]=properties[i].name();
		}
		
		sendCommand(Features.GET_PROP, listener, props);
	}
	
	/**
	 * This method is used to change the color of a smart LED.
	 * @param color is the target color, whose type is integer. It should be expressed in decimal integer ranges from 0 to 16777215 (hex: 0xFFFFFF).
	 * @param method support two values: "sudden" and "smooth". If effect is "sudden", then the color temperature will be changed directly to target value, under this case, the third parameter "duration" is ignored. If effect is "smooth", then the color temperature will be changed to target value in a gradual fashion, under this case, the total time of gradual change is specified in third parameter "duration".
	 * @param duration specifies the total time of the gradual changing. The unit is milliseconds. The minimum support duration is 30 milliseconds.
	 * @param listener is the response listener.
	 */
	public void setRGB(int color,Effect method,int duration,OnCommandListener listener)
	{
		if(color<0 || color>0xffffff) 
		{
			throw new IllegalArgumentException();
		}

		sendCommand(Features.SET_RGB,listener,color,method.name().toLowerCase(),duration);
	}
	
	/**
	 * This method is used to change the color of a smart LED.
	 * @param hue is the target hue value, whose type is integer. It should be expressed in decimal integer ranges from 0 to 359.
	 * @param sat is the target saturation value whose type is integer. It's range is 0 to 100.
	 * @param method support two values: "sudden" and "smooth". If effect is "sudden", then the color temperature will be changed directly to target value, under this case, the third parameter "duration" is ignored. If effect is "smooth", then the color temperature will be changed to target value in a gradual fashion, under this case, the total time of gradual change is specified in third parameter "duration".
	 * @param duration specifies the total time of the gradual changing. The unit is milliseconds. The minimum support duration is 30 milliseconds.
	 * @param listener is the response listener.
	 */
	public void setHSV(int hue,int sat,Effect method,int duration,OnCommandListener listener)
	{
		if(hue<0 || hue>359) 
		{
			throw new IllegalArgumentException();
		}
		
		if(sat<0 || sat>100) 
		{
			throw new IllegalArgumentException();
		}
		
		sendCommand(Features.SET_HSV,listener,hue,sat,method.name().toLowerCase(),duration);
	}
	
	/**
	 * This method is used to change the brightness of a smart LED.
	 * @param brightness is the target brightness. The type is integer and ranges from 1 to 100. The brightness is a percentage instead of a absolute value. 100 means maximum brightness while 1 means the minimum brightness.
	 * @param method support two values: "sudden" and "smooth". If effect is "sudden", then the color temperature will be changed directly to target value, under this case, the third parameter "duration" is ignored. If effect is "smooth", then the color temperature will be changed to target value in a gradual fashion, under this case, the total time of gradual change is specified in third parameter "duration".
	 * @param duration specifies the total time of the gradual changing. The unit is milliseconds. The minimum support duration is 30 milliseconds.
	 * @param listener is the response listener.
	 */
	public void setBright(int brightness,Effect method,int duration,OnCommandListener listener)
	{
		if(brightness<1 || brightness>100) 
		{
			throw new IllegalArgumentException();
		}
		
		sendCommand(Features.SET_BRIGHT,listener,brightness,method.name().toLowerCase(),duration);
	}
	
	/**
	 * This method is used to save current state of smart LED in persistent memory. So if user powers off and then powers on the smart LED again (hard power reset), the smart LED will show last saved state.
	 * @param listener is the response listener.
	 */
	public void setDefault(OnCommandListener listener)
	{
		sendCommand(Features.SET_DEFAULT,listener);
	}
	
	private String flowTupleListToString(List<FlowTuple> tuples) 
	{
		StringBuffer sb=new StringBuffer();
		
		for(FlowTuple tuple : tuples)
		{
			sb.append(tuple.getDuration());
			sb.append(',');
			sb.append(tuple.getMode().getValue());
			sb.append(',');
			sb.append(tuple.getValue());
			sb.append(',');
			sb.append(tuple.getBrightness());
			sb.append(',');
		}
		
		String tupleList=sb.toString();
		
		return tupleList.substring(0,tupleList.length()-1);
	}
	
	/**
	 * This method is used to start a color flow. Color flow is a series of smart LED visible state changing. It can be brightness changing, color changing or color temperature changing. This is the most powerful command. All our recommended scenes, e.g. Sunrise/Sunset effect is implemented using this method. With the flow expression, user can actually “program” the light effect.
	 * Only accepted if the smart LED is currently in "on" state.
	 * @param count is the total number of visible state changing before color flow stopped. 0 means infinite loop on the state changing.
	 * @param action is the action taken after the flow is stopped.
	 * @param tuples is the expression of the state changing series.
	 * @param listener is the response listener.
	 */
	public void startColorFlow(int count,ColorFlowEndAction action,List<FlowTuple> tuples, OnCommandListener listener)
	{	
		sendCommand(Features.START_CF,listener,count,action.getValue(),flowTupleListToString(tuples));
	}
	
	/**
	 * This method is used to stop a running color flow.
	 * @param listener is the response listener.
	 */
	public void stopColorFlow(OnCommandListener listener)
	{
		sendCommand(Features.STOP_CF,listener);
	}
	
	/**
	 * This method is used to set the smart LED directly to specified state. If the smart LED is off, then it will turn on the smart LED firstly and then apply the specified command.
	 * @param sceneType can be "color", "hsv", "ct", "cf", "auto_dealy_off".
	 * @param val1 is class specific.
	 * @param val2 is class specific.
	 * @param flowTuples is the tuple list if the mode is "cf".
	 * @param listener is the response listener.
	 */
	public void setScene(SceneType sceneType,int val1, int val2, List<FlowTuple> flowTuples,OnCommandListener listener)
	{
		if(sceneType==SceneType.CF) 
		{
			sendCommand(Features.SET_SCENE,listener,sceneType.name(),0,0,flowTupleListToString(flowTuples));
		}else 
		{
			sendCommand(Features.SET_SCENE,listener,sceneType.name(),val1,val2);
		}
	}
	
	/**
	 * This method is used to start a timer job on the smart LED
	 * @param minutes is the length of the timer (in minutes).
	 * @param listener is the response listener.
	 */
	public void cronAdd(int minutes,OnCommandListener listener)
	{
		sendCommand(Features.CRON_ADD,listener,0,minutes);
	}
	
	/**
	 * This method is used to retrieve the setting of the current cron job of the specified type.
	 * @param listener is the response listener.
	 */
	public void cronGet(OnCommandListener listener)
	{
		sendCommand(Features.CRON_GET,listener,0);
	}
	
	/**
	 * This method is used to stop the specified cron job.
	 * @param listener is the response listener.
	 */
	public void cronDelete(OnCommandListener listener)
	{
		sendCommand(Features.CRON_DEL,listener,0);
	}
	
	/**
	 * This method is used to change brightness, CT or color of a smart LED without knowing the current value, it's main used by controllers.
	 * @param action the direction of the adjustment.
	 * @param type the property to adjust.
	 * @param listener is the response listener.
	 */
	public void setAdjust(AdjustAction action,AdjustType type,OnCommandListener listener)
	{
		if(type==AdjustType.COLOR && action!=AdjustAction.CIRCLE) 
		{
			throw new IllegalArgumentException();
		}
		
		sendCommand(Features.SET_ADJUST,listener,action.name(),type.name());
	}
	
	/**
	 * This method is used to start or stop music mode on a device. Under music mode, no property will be reported and no message quota is checked.
	 * @param turn Turn music mode on or off.
	 * @param host the IP address of the music server.
	 * @param port the TCP port music application is listening on.
	 * @param listener is the response listener.
	 */
	public void setMusic(boolean turn,String host,int port,OnCommandListener listener)
	{
		sendCommand(Features.SET_MUSIC,listener,turn?1:0,host,port);
	}
	
	/**
	 * This method is used to name the device. The name will be stored on the device and reported in discovering response. User can also read the name through “get_prop” method.
	 * @param name is the name of the device.
	 * @param listener is the response listener.
	 */
	public void setName(String name,OnCommandListener listener)
	{
		sendCommand(Features.SET_NAME,listener,name);
	}
	
	
}
