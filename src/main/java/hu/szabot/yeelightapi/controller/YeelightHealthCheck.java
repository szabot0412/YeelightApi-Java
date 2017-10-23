package hu.szabot.yeelightapi.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class YeelightHealthCheck {

	public YeelightHealthCheck() {
		
		DatagramSocket serverSocket;
		try {
			
			serverSocket = new DatagramSocket(1982);
		
	        byte[] receiveData = new byte[1024];
	        byte[] sendData = new byte[1024];
	        while(true)
	           {
	              DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	              serverSocket.receive(receivePacket);
	              String sentence = new String( receivePacket.getData());
	              System.out.println("RECEIVED: " + sentence);
	              InetAddress IPAddress = receivePacket.getAddress();
	              int port = receivePacket.getPort();
	              String capitalizedSentence = sentence.toUpperCase();
	              sendData = capitalizedSentence.getBytes();
	              DatagramPacket sendPacket =
	              new DatagramPacket(sendData, sendData.length, IPAddress, port);
	              serverSocket.send(sendPacket);
	           }
	        
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
