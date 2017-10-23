package hu.szabot.yeelightapi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpConnection {
	
	public interface TcpConnectionListener
	{
		public void onConnectionStateChanged(boolean connected);
	}
	
	public interface OnMessageListener
	{
		public void onMessageReceived(String message);
	}

	private Socket socket;
	private boolean running=true;
	private BufferedReader input;
	private PrintWriter output;
	private TcpConnectionListener connectionListener;
	private OnMessageListener onMessageListener;
	private boolean connected;
	
	public TcpConnection(String address,int port) throws UnknownHostException, IOException 
	{
		socket = new Socket(address,port);
	}
	
	public void setConnectionListener(TcpConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	public void setOnMessageListener(OnMessageListener onMessageListener) {
		this.onMessageListener = onMessageListener;
	}
	
	private void changeState(final boolean connected)
	{
		if(connected!=this.connected)
		{
			this.connected=connected;
			

			new Thread(){
				
				public void run() 
				{
					if(connectionListener!=null)
						connectionListener.onConnectionStateChanged(connected);
				};
				
			}.start();
			
		}
	}
	
	public void start()
	{
		new Thread(){
			
			public void run() 
			{
				
				try {
					input =new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output = new PrintWriter(socket.getOutputStream());
					
				} catch (IOException e) {
					e.printStackTrace();
					
					close();
					
					return;
				}

				changeState(true);
				
				while(running)
				{
					try {
						final String line=input.readLine();
						
						if(running && line!=null && line.trim().length()>0)
						{
							new Thread()
							{
								@Override
								public void run() {
									handleMessage(line);
								}
							}.start();
						}
						
						if(line==null)
						{
							close();
						}
						
					} catch (IOException e) 
					{
						e.toString();
					}
				}
				
			}
			
		}.start();
		
	}
	
	public void close()
	{
		running=false;
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		changeState(false);
	}	
	
	private void handleMessage(String line)
	{
		if(onMessageListener!=null)
			onMessageListener.onMessageReceived(line);
		
	}

	public void sendMessage(String message)
	{
		output.println(message);
		output.flush();
	}
	
}
