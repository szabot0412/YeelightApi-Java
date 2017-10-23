package hu.szabot.yeelightapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.szabot.yeelightapi.controller.TcpConnection.OnMessageListener;
import hu.szabot.yeelightapi.controller.TcpConnection.TcpConnectionListener;
import hu.szabot.yeelightapi.controller.dto.YeelightCommand;
import hu.szabot.yeelightapi.controller.dto.YeelightError;
import hu.szabot.yeelightapi.controller.dto.YeelightNotification;
import hu.szabot.yeelightapi.controller.dto.YeelightResponse;
import hu.szabot.yeelightapi.model.YeelightDevice;
import hu.szabot.yeelightapi.model.YeelightDevice.Features;
import hu.szabot.yeelightapi.model.YeelightDevice.Properties;
import hu.szabot.yeelightapi.parser.NotificationParser;

public class BaseController implements OnMessageListener {

	private static final int TCP_PORT=55443;
	
	private static final int COMMAND_TIMEOUT=20000;
	
	private static final int TIMEOUT_ERROR_CODE=42;
	
	private static ObjectMapper mapper=new ObjectMapper();
	private static Timer timer=new Timer();
	
	public class FeatureNotSupportedException extends RuntimeException
	{
		private static final long serialVersionUID = 5560736921411702167L;
	}
	
	public interface OnCommandListener
	{
		public void onCommandFinished(YeelightDevice device, Features feature,Object[] response);
		public void onCommandError(YeelightDevice device, Features feature,YeelightError error);
		
	}
	
	public interface OnPropertyUpdated
	{
		public void onPropertyUpdated(YeelightDevice device,Properties property);
	}
	
	private class FeatureListenerPair
	{
		public Features feature;
		public OnCommandListener listener;
		
		public FeatureListenerPair(Features feature, OnCommandListener listener) {
			this.feature = feature;
			this.listener = listener;
		}
		
	}
	
	private class TimeoutTimerTask extends TimerTask
	{
		int id;
		
		public TimeoutTimerTask(int id) 
		{
			this.id = id;
		}

		@Override
		public void run() {
			
			synchronized (pendingCommands) 
			{
				
				FeatureListenerPair pair=pendingCommands.get(id);
				
				if(pair!=null) 
				{
					YeelightError error=new YeelightError();
					
					error.setCode(TIMEOUT_ERROR_CODE);
					error.setMessage("Timeout");
					
					pair.listener.onCommandError(device, pair.feature,error);
					pendingCommands.remove(id);
				}

			}
		}
		
	}

	
	private int lastUniqueId;
	private YeelightDevice device;
	private TcpConnection connection;
	private Map<Integer,FeatureListenerPair> pendingCommands=new HashMap<Integer, FeatureListenerPair>();
	private OnPropertyUpdated propertyUpdateListener;
	
	public BaseController(YeelightDevice device)
	{
		this.device = device;

	}
	
	public void connect(TcpConnectionListener listener) throws IOException
	{
		connection=new TcpConnection(device.getAddress(), TCP_PORT);
		connection.setConnectionListener(listener);
		connection.setOnMessageListener(this);
		connection.start();
	}
	
	public void setOnPropertyUpdateListener(OnPropertyUpdated propertyUpdateListener) {
		this.propertyUpdateListener = propertyUpdateListener;
	}
	
	private void checkFeature(Features feature) throws FeatureNotSupportedException
	{
		if (!device.isFeatureSupported(feature))
			throw new FeatureNotSupportedException();
	}
	
	private int getUniqueId()
	{
		return ++lastUniqueId;
	}
	
	protected void sendCommand(Features feature,OnCommandListener listener,Object... params) throws FeatureNotSupportedException
	{
		checkFeature(feature);
		
		YeelightCommand command=new YeelightCommand();

		command.setId(getUniqueId());
		command.setMethod(feature.name().toLowerCase());
		command.setParams(params);
		
		try {
			
			pendingCommands.put(command.getId(),new FeatureListenerPair(feature, listener));
			
			timer.schedule(new TimeoutTimerTask(command.getId()), COMMAND_TIMEOUT);
			
			connection.sendMessage(mapper.writeValueAsString(command)+"\r\n");
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void onMessageReceived(String message) 
	{
		synchronized (pendingCommands) 
		{
			try {
				YeelightResponse response=mapper.readValue(message, YeelightResponse.class);
				
				FeatureListenerPair pair=pendingCommands.get(response.getId());
				
				if(pair!=null)
				{
					if(response.getError()!=null)
					{
						pair.listener.onCommandError(device, pair.feature, response.getError());
					}else 
					{
						pair.listener.onCommandFinished(device, pair.feature,response.getResult());
						pendingCommands.remove(response.getId());
					}
				}
				
			} catch (IOException e) {
				
				try {
					
					YeelightNotification notification=mapper.readValue(message, YeelightNotification.class);
				
					Iterator<String> iterator=notification.getParams().keySet().iterator();
					
					while(iterator.hasNext())
					{
						String key=iterator.next();

						Properties enumName=Properties.valueOf(key.toUpperCase());
						
						if(NotificationParser.updateParam(enumName, notification.getParams().get(key), device))
						{
							if(propertyUpdateListener!=null)
								propertyUpdateListener.onPropertyUpdated(device, enumName);
						}
					}
					
					
				} catch (IOException e1) 
				{
					
					e1.printStackTrace();
				}
				
			}
		}
	}
	
}
