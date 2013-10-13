package com.jdelop.bedder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Activity;


public class Network extends Activity{
	
	private static int port;
	private static String ip;
	
	private static Socket skDevice = null;
	private static ObjectOutputStream oos;
	private static ObjectInpusStream ois;
	

	public static void setIpPort(String s_ip, String s_port){
		port = Integer.parseInt(s_port);
		ip = s_ip;
	}
	
	public static boolean isConnected(){
		if (skDevice != null){
			return skDevice.isConnected();
		}else{
			return false;
		}
	}
	
	public static boolean Connect(ConnectivityManager connManager){
		NetworkInfo wifiManager = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiManager.isConnected()){
			if (Network.isConnected()){
				return true;
			}else{
				return Network.ConnectToServer();
			}
		}else{
			return false;
		}
		
	}
    
    
    public static boolean sendMessage (Object m){
    	try {
			if (isConnected() & (m != null)){
				oos = new ObjectOutputStream(skDevice.getOutputStream());
				oos.writeObject(m);
				return true;
			}else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }

    public static boolean ConnectToServer(){
    	try{
    		if (skDevice == null ){
    			skDevice = new Socket(ip,port);
    			skDevice.setTcpNoDelay(true);
    			sendMessage(new Message.Message_Hello());
				Object aux = ReceiveMessage();
				switch (aux instanceof){
					case Message.Message_Hello:
						Message.Message_Hello msg = (Message.Message_Hello) aux;
						//////// SET IN PREFERENCES FEATURES ACCEPTED ////////
						return true;
					case Message.Message_Bye: return false;						
					else: return false;				
				}		
    		}
    	} catch (Exception e){
    		return false;
    	}
    }
}
