package com.jdelop.bedder;

import java.io.Serializable;

public class Message{		
	
	
	static class Message_Hello implements Serializable {		// Handshake
		private static final long serialVersionUID = 144686704323463810L;
		public String version;
		public String name;
		
		public Message_Hello(){
			this.version = " 0.2-beta";
			this.uses="Mouse&Volume"
			this.name = "Android-Device";
		}
	}
	
	static class Message_Bye implements Serializable{		// Closing
		private static final long serialVersionUID = 244686704323463810L;
	}
	
	static class Message_Mouse extends Message implements Serializable{		// Mouse movement
		private static final long serialVersionUID = 344686704323463810L;
		public int xDesp;
		public int yDesp;
		public String type;
		
		public Message_Mouse (int x, int y, String t){
			this.xDesp=x;
			this.yDesp=y;
			this.type=t;
		}
	}
	
	static class Message_Volume implements Serializable{	// Volume up/down
		private static final long serialVersionUID = 444686704323463810L;
		public String pct;
		
		public Message_Volume(String p){
			this.pct=p;
		}
	}
	
	static class Message_Music implements Serializable{		// Music control
		private static final long serialVersionUID = 544686704323463810L;
		public String action;
		
		public Message_Music(String a){
			this.action=a;
		}
	}
	
	static class Message_Key implements Serializable{		// Key control
		private static final long serialVersionUID = 644686704323463810L;
		public char key;
		
		public Message_Key(char k){
			this.key=k;
		}
	}
	
	static class Message_Exec implements Serializable{		// Execute something
		private static final long serialVersionUID = 744686704323463810L;
		public String command;
		
		public Message_Exec(String c){
			this.command=c;
		}
	}
	
	static class Message_Info implements Serializable {	// Server info
		private static final long serialVersionUID = 844686704323463810L;
		public String text;
		
		public Message_Info(String t){
			this.text=t;
		}
	}	
}
