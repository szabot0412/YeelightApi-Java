package hu.szabot.yeelightapi.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.szabot.yeelightapi.model.YeelightDevice;
import hu.szabot.yeelightapi.model.YeelightDevice.Features;
import hu.szabot.yeelightapi.model.YeelightDevice.Mode;
import hu.szabot.yeelightapi.model.YeelightDevice.ModelType;
import hu.szabot.yeelightapi.model.YeelightDevice.Properties;

public class DeviceParser extends Parser{

	public static YeelightDevice parseDevice(String message) 
	{
		YeelightDevice device=new YeelightDevice();
		
		Map<String, String> headers = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("(.*): (.*)");
        
        String[] lines=message.split("\r\n");
        
        for(String line : lines)
        {
        	Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) 
            {
                headers.put(matcher.group(1).toUpperCase().trim(), matcher.group(2).trim());
            }
        }
    
        device.setLocation(headers.get(Properties.LOCATION.name()));
        device.setId(headers.get(Properties.ID.name()));
        device.setModel(ModelType.valueOf(headers.get(Properties.MODEL.name()).toUpperCase()));
        device.setFirmwareVersion(convertToInt(headers.get(Properties.FW_VER.name())));
        	device.setPower(stringToBool(headers.get(Properties.POWER.name())));
        	device.setBright(convertToInt(headers.get(Properties.BRIGHT.name())));
        	device.setColorMode(Mode.getModeByValue(convertToInt(headers.get(Properties.COLOR_MODE.name()))));
        	device.setColorTemperature(convertToInt(headers.get(Properties.CT.name())));
        	device.setRgb(convertToInt(headers.get(Properties.RGB.name())));
        	device.setHue(convertToInt(headers.get(Properties.HUE.name())));
        	device.setSaturation(convertToInt(headers.get(Properties.SAT.name())));
        	device.setName(headers.get(Properties.NAME.name()));
	    	
        	device.setSupport(new HashSet<Features>());
	    	
	    	if(headers.get(Properties.SUPPORT.name())!=null)
	    	{
	    		String[] items=headers.get(Properties.SUPPORT.name()).split(" ");
	    		
	    		for(String item : items)
	    		{
	    			device.getSupport().add(Features.valueOf(item.toUpperCase()));
	    		}
	    	}
	    	
		try {
			
			device.setAddress(new URI(device.getLocation()).getHost());
		    
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return device;
	}
	
}
