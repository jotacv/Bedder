package com.jdelop.bedder;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
	
	ServerSocket skServer;
	Socket skDevice;
	Message m;
	Message mClose;
	Robot robot;
	int port = 1337;
	
	Server(){
		System.out.println("~·~·~ Bedder Server ~·~·~");
		System.out.println("");
		try {
			System.out.println("Starting...");
			skServer = new ServerSocket(port);
			robot = new Robot();
			while(true){
				Process ps = Runtime.getRuntime().exec (new String[]{"notify-send", "--hint=int:transient:1", "-t", "5000", "Bedder", "Service Started\nWaiting for connections", "-i", "/home/jotacv/Imágenes/Icons/bedder.png"});
				System.out.println("Waiting for connections..");
				skDevice = skServer.accept();
				System.out.println("Connection acepted.");
				Process p = Runtime.getRuntime().exec (new String[]{"notify-send", "--hint=int:transient:1", "-t", "5000", "Bedder", "Device Connected", "-i", "/home/jotacv/Imágenes/Icons/bedder.png"});
				while(true){
					try {
						ObjectInputStream ois = new ObjectInputStream(skDevice.getInputStream());
						Object aux = ois.readObject();
						if (aux instanceof Message){
							m = (Message) aux;
							if (!m.close){
								if (m.action == 1){		// Mouse Move
									HandleMouseAction(m);
								}
								if (m.action == 2){		// Debug text
									System.out.println(m.data);
								}
								if (m.action == 3){		// Key Event
									HandleKeyAction(m);
								}
								if (m.action == 4){		// Volume Rocker
									HandleVolumeAction(m);
								}
							}else {
								// close
							}
						} else{
							System.out.println(" Not acepted.");
						}
					} catch (ClassNotFoundException e) {
						System.out.println("Exception while obtaining remote object"+e);
						Process p1 = Runtime.getRuntime().exec (new String[]{"notify-send", "--hint=int:transient:1", "-t", "5000", "Bedder", "Device Disconnected", "-i", "/home/jotacv/Imágenes/touchpad.png"});
						break;
					} catch (EOFException e){
						System.out.println("Device went offline"+e);
						Process p2 = Runtime.getRuntime().exec (new String[]{"notify-send", "--hint=int:transient:1", "-t", "5000", "Bedder", "Device Disconnected", "-i", "/home/jotacv/Imágenes/touchpad.png"});
						break;
					}
				}
				System.out.println("Disconnected");
			}
		} catch (IOException e) {
			System.out.println("Exception while starting socket"+e);
			e.printStackTrace();
		} catch (AWTException e){
			System.out.println("Exception creating robot"+e);
			e.printStackTrace();	
		}
	}
	
	public void HandleMouseAction(Message m){
		if (m.data == null){				//Mouse Move
			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			int x = (int) b.getX();
			int y = (int) b.getY();
			robot.mouseMove(x - m.xDesp, y - m.yDesp);
			robot.delay(10);
			
		}else {								// Mouse Click or Mouse Scroll 
			int btn = 0;
			if (Integer.parseInt(m.data) == 1){
				btn = InputEvent.BUTTON1_MASK;
				robot.mousePress(btn);
				
			}else if (Integer.parseInt(m.data) == 2){
				btn = InputEvent.BUTTON2_MASK;
				robot.mousePress(btn);
				
			}else if (Integer.parseInt(m.data) == 3){
				btn = InputEvent.BUTTON3_MASK;
				robot.mousePress(btn);
				
			}else if (Integer.parseInt(m.data) == -1){
				btn = InputEvent.BUTTON1_MASK;
				robot.mouseRelease(btn);
				
			}else if (Integer.parseInt(m.data) == -2){
				btn = InputEvent.BUTTON2_MASK;
				robot.mouseRelease(btn);
				
			}else if (Integer.parseInt(m.data) == -3){
				btn = InputEvent.BUTTON3_MASK;
				robot.mouseRelease(btn);
			}else if(Integer.parseInt(m.data) == 0){
				if(m.yDesp > 0){
					robot.mouseWheel(-2);
				}else {
					robot.mouseWheel(2);
				}
			}else{
				System.out.println("Unrecognized mouse code");
			}
		}
	}

	public void HandleKeyAction(Message m){
		System.out.println("Key: "+m.data);
	}

	public void HandleVolumeAction(Message m){
		try {
			Process p = Runtime.getRuntime().exec (new String[]{"pactl", "set-sink-volume", "0", "--", m.data});
			//p.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
			new Server();
	}
}
