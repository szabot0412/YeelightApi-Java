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

	
	public enum SceneType{COLOR, HSV, CT, CF, AUTO_DELAY_OFF}
	public enum AdjustAction{INCREASE,DECREASE,CIRCLE}
	public enum AdjustType{BRIGHT,CT,COLOR}
	
	
	
	
	public YeelightController(YeelightDevice device) {
		super(device);
	}

	public void toggle(OnCommandListener listener) throws FeatureNotSupportedException
	{
		sendCommand(Features.TOGGLE,listener);
	}
	
	public void setPower(boolean power, Effect method,int duration,OnCommandListener listener) throws FeatureNotSupportedException
	{
		sendCommand(Features.SET_POWER,listener,Parser.boolToString(power),method.name().toLowerCase(),duration);
	}
	
	public void changeColorTemperature(int value, Effect method,int duration,OnCommandListener listener) throws FeatureNotSupportedException
	{
		if(value<1700 || value>6500) 
		{
			throw new IllegalArgumentException();
		}

		sendCommand(Features.SET_CT_ABX,listener,value,method.name().toLowerCase(),duration);
	}
	
	public void getProps(Properties[] properties,OnCommandListener listener) throws FeatureNotSupportedException
	{
		Object[] props=new String[properties.length];
		
		for(int i=0;i<properties.length;i++) 
		{
			props[i]=properties[i].name();
		}
		
		sendCommand(Features.GET_PROP, listener, props);
	}
	
	public void setRGB(int color,Effect method,int duration,OnCommandListener listener)
	{
		if(color<0 || color>0xffffff) 
		{
			throw new IllegalArgumentException();
		}

		sendCommand(Features.SET_RGB,listener,color,method.name().toLowerCase(),duration);
	}
	
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
	
	public void setBright(int brightness,Effect method,int duration,OnCommandListener listener)
	{
		if(brightness<1 || brightness>100) 
		{
			throw new IllegalArgumentException();
		}
		
		sendCommand(Features.SET_BRIGHT,listener,brightness,method.name().toLowerCase(),duration);
	}
	
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
	
	public void startColorFlow(int count,ColorFlowEndAction action,List<FlowTuple> tuples, OnCommandListener listener)
	{	
		sendCommand(Features.START_CF,listener,count,action.getValue(),flowTupleListToString(tuples));
	}
	
	public void stopColorFlow(OnCommandListener listener)
	{
		sendCommand(Features.STOP_CF,listener);
	}
	
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
	
	public void cronAdd(int minutes,OnCommandListener listener)
	{
		sendCommand(Features.CRON_ADD,listener,0,minutes);
	}
	
	public void cronGet(OnCommandListener listener)
	{
		sendCommand(Features.CRON_GET,listener,0);
	}
	
	public void cronDelete(OnCommandListener listener)
	{
		sendCommand(Features.CRON_DEL,listener,0);
	}
	
	public void setAdjust(AdjustAction action,AdjustType type,OnCommandListener listener)
	{
		if(type==AdjustType.COLOR && action!=AdjustAction.CIRCLE) 
		{
			throw new IllegalArgumentException();
		}
		
		sendCommand(Features.SET_ADJUST,listener,action.name(),type.name());
	}
	
	public void setMusic(boolean turn,String host,int port,OnCommandListener listener)
	{
		sendCommand(Features.SET_MUSIC,listener,turn?1:0,host,port);
	}
	
	public void setName(String name,OnCommandListener listener)
	{
		sendCommand(Features.SET_NAME,listener,name);
	}
	
	
}
