package hu.szabot.yeelightapi.parser;

import hu.szabot.yeelightapi.model.YeelightDevice;
import hu.szabot.yeelightapi.model.YeelightDevice.Mode;
import hu.szabot.yeelightapi.model.YeelightDevice.Properties;

public class NotificationParser extends Parser{

	public static boolean updateParam(Properties enumName,Object value,YeelightDevice device)
	{
		boolean valueChanged=false;
		
		boolean boolValue;
		int intValue;
		
		switch (enumName) {
		
		case POWER:
			
			boolValue=stringToBool((String)value);
			
			valueChanged=device.isPower()!=boolValue;
			
			device.setPower(boolValue);
			
			break;
		case BRIGHT:
			
			intValue=convertToInt((String)value);
			
			valueChanged=device.getBright()!=intValue;
			
			device.setBright(intValue);
			
			break;
		case COLOR_MODE:
			
			Mode mode=Mode.getModeByValue(convertToInt((String)value));
			
			valueChanged=!device.getColorMode().equals(mode);
			
			device.setColorMode(mode);
			
			break;
		case CT:
			
			intValue=convertToInt((String)value);
			
			valueChanged=device.getColorTemperature()!=intValue;
			
			device.setColorTemperature(intValue);
			
			break;
		case DELAYOFF:
			
			//TODO: implement
			
			break;
		case FLOWING:
			
			//TODO: implement
			
			break;
		case FLOW_PARAMS:
			
			//TODO: implement
			
			break;
		case HUE:
			
			intValue=convertToInt((String)value);
			
			valueChanged=device.getHue()!=intValue;
			
			device.setHue(intValue);
			
			break;
		case MUSIC_ON:
			
			//TODO: implement
			
			break;
		case NAME:
			
			valueChanged=!device.getName().equals((String)value);
			
			device.setName((String)value);
			
			break;
		case RGB:
			
			intValue=convertToInt((String)value);
			
			valueChanged=device.getRgb()!=intValue;
			
			device.setRgb(intValue);
			
			break;
		case SAT:
			
			intValue=convertToInt((String)value);
			
			valueChanged=device.getSaturation()!=intValue;
			
			device.setSaturation(intValue);
			
			break;
		default:
			break;
		
		}
		
		return valueChanged;
	}

}
