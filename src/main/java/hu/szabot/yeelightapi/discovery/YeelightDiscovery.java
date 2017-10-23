package hu.szabot.yeelightapi.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import hu.szabot.yeelightapi.model.YeelightDevice;
import hu.szabot.yeelightapi.parser.DeviceParser;

public class YeelightDiscovery implements Runnable
{
	public interface DiscoveryListener
	{
		void onDeviceFound(YeelightDevice device);
		void onDiscoveryFinished(Collection<YeelightDevice> devices);
		void onError(Exception e);
	}
	
	private static final String UDP_HOST = "239.255.255.250";
    private static final int UDP_PORT = 1982;
    private static final String MESSAGE = "M-SEARCH * HTTP/1.1\r\n" +
            "HOST:239.255.255.250:1982\r\n" +
            "MAN:\"ssdp:discover\"\r\n" +
            "ST:%s\r\n";
    private static final String DEVICE_TYPE="wifi_bulb";
    
    private static final int DEFAULT_SEARCH_TIME=10000;
   
    private boolean running=true;
    private DiscoveryListener listener;
    
    public void start(DiscoveryListener listener)
    {
    	this.listener=listener;
    	running=true;
    	new Thread(this).start();
    }
    
    public void stop()
    {
    	running=false;
    }
    
    public void run() 
    {
    	Set<YeelightDevice> devices=new HashSet<YeelightDevice>();
    	
    	DatagramSocket socket;
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(DEFAULT_SEARCH_TIME);
	
			String finalMessage=String.format(MESSAGE, DEVICE_TYPE);
			
	        DatagramPacket data = new DatagramPacket(finalMessage.getBytes(),finalMessage.getBytes().length, InetAddress.getByName(UDP_HOST),UDP_PORT);
	        
	        socket.send(data);

			while (running) 
			{
	            byte[] buffer = new byte[1024];
	            DatagramPacket dpRecv = new DatagramPacket(buffer,buffer.length);
	            
	            try{
	            	
		            socket.receive(dpRecv);
		            
		            if(!running)
		            	break;
		            
		            byte[] bytes = dpRecv.getData();
		            
		            String message = new String(bytes);
		            
		            YeelightDevice device=DeviceParser.parseDevice(message);
		            
		            if(listener!=null && !devices.contains(device))
		            	listener.onDeviceFound(device);
		            
		            devices.add(device);
		            
	            }catch(SocketTimeoutException e)
	            {
	            	running=false;
	            }
			}
			
			if(listener!=null)
            	listener.onDiscoveryFinished(devices);
			
		} catch (IOException e) {
			e.printStackTrace();
			
			if(listener!=null)
            	listener.onError(e);
		}
		
    }
}
