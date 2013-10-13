package com.jdelop.bedder;

import com.jdelop.bedder.Message.Message_Hello;
import com.jdelop.bedder.Message.Message_Info;
import com.jdelop.bedder.Message.Message_Key;
import com.jdelop.bedder.Message.Message_Mouse;
import com.jdelop.bedder.Message.Message_Volume;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnGestureListener {
	
	private GestureDetector gdetector;
	private boolean stillMousePress1 = false;
	//private boolean stillMousePress2 = false;
	private boolean stillMousePress3 = false;
	private boolean wheelingFlag1 = false;
	private boolean wheelingFlag2 = false;
	private boolean layoutConnected=false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(0);
        setContentView(R.layout.activity_main);
        
        gdetector = new GestureDetector(this,this,null, false);
                
        Context context = getApplicationContext();
/* PSEUDO:

SETUP()
SET LAYOUT WITH CONNECT BUTTON
WHEN CLICKED CHANGUE BUTTON SOMEHOW TO CLICKED LOOK
WITH INTENT SOMEHOW CALL "CONNECT_TO_SERVER"
RETURN ASYNCHRONUSLY
AT RETURN OF CONNECT_TO_SERVER STAY IN LAYOUT OOR CHANGE LAYOUT TO CONNECTED_LAYOUT

*/
        
		int duration = Toast.LENGTH_SHORT;
		Network.setIpPort(getString(R.string.ip),getString(R.string.port));
		Boolean success = Network.Connect((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE));
		if (success){
			setBackgroundConnected(true);
        	setDebugOutput("Connected.");
            Toast toast = Toast.makeText(context, "Connected", duration);
    		toast.show();
		}else{
			setBackgroundConnected(false);
        	setDebugOutput("Not connected. Long-press the screen to show settings.");
            Toast toast = Toast.makeText(context, "Not connected. Long-press the screen to show settings.", duration);
    		toast.show();
    	}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public boolean onFling(MotionEvent start, MotionEvent finish
    , float xVelocity, float yVelocity) {    	
    	return false;
    }


	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
        //Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
        //startActivity(settingsActivity);
	}
	
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float xVelocity,
			float yVelocity) {
    	CharSequence text;
    	
    	if ((arg0.getPointerCount() == 1) & (arg1.getPointerCount() == 1)){    	
	    	float xDesp = 2*xVelocity;
	    	float yDesp = 2*yVelocity;
	    	if((xDesp<80)&(xDesp>-80) & (yDesp<80)&(yDesp>-80)){
		        text = "Scrolling ("+Float.toString(xDesp)+","+ Float.toString(yDesp)+")";
		        setDebugOutput((String)text);
		        
		        Message_Mouse msg = new Message_Mouse((int)xDesp,(int)yDesp,null);        
		        sendMessage(msg);
	    	}
    	}else {
	        text = "Wheeling ("+Float.toString(xVelocity)+","+ Float.toString(yVelocity)+")";
	        setDebugOutput((String) text);
	        
	        if (wheelingFlag1){
	        	if(wheelingFlag2){
	        		Message_Mouse msg = new Message_Mouse((int)xVelocity,(int)yVelocity,"0");        
	    	        sendMessage(msg);
	    	        wheelingFlag1=false;
	    	        wheelingFlag2=false;
	        	}else{
	        		wheelingFlag2=true;
	        	}
	        }else{
	        	wheelingFlag1=true;
	        }

	    }
    	return true;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
		//setDebugOutput("onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		setDebugOutput("Screen Click");
		Message_Mouse msg = new Message_Mouse(0,0,"1");
		sendMessage(msg);
		msg = new Message_Mouse(0,0,"-1");
		sendMessage(msg);
		return true;
	}
	
	@Override	//VIP..!! without this, gdetector wont fire
	public boolean onTouchEvent(MotionEvent me) {
		return gdetector.onTouchEvent(me);

	}
	   
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	if (keyCode == 80){ 		// Button 1 
    		setDebugOutput("Button 1 Up");
    		Message_Mouse msg = new Message_Mouse(0,0,"-1");
    		Network.sendMessage(msg);
            stillMousePress1=false;
            
    	}else if (keyCode == 82){	// Button 3
    		Message_Mouse msg = new Message_Mouse(0,0,"-3");
    		setDebugOutput("Button 3 Up");
    		Network.sendMessage(msg);
    		stillMousePress3=false;
    	}
    	return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	Object msg= null;
    	
    	if (keyCode == 80){ 		// Button 1 
    		if (!stillMousePress1){
    			setDebugOutput("Button 1 Down");
    			msg = new Message_Mouse(0,0,"1");
            	stillMousePress1=true;
    		}
            
    	}else if (keyCode == 82){	// Button 3
    		if (!stillMousePress3){
	    		setDebugOutput("Button 3 Down");
	    		msg = new Message_Mouse(0,0,"3");
	            stillMousePress3=true;
    		}
            
    	}else if (keyCode == 24){	// Volume Rocker UP
    		setDebugOutput("Volume UP");
    		msg = new Message_Volume("+2");  
            
    	}else if (keyCode == 25){	// Volume Rocker DOWN
    		setDebugOutput("Volume DOWN");
    		msg = new Message_Volume("-2");
            
       	}else{						// Keyboard
    		char unicodeChar = (char)event.getUnicodeChar();
    		setDebugOutput("Tapped: "+keyCode+": "+unicodeChar);
    	}
    	
    	Network.sendMessage(msg);
    	return true;
    }
    
    
    public void setBackgroundConnected(boolean s){
    	View lay = findViewById(R.id.main_layout);
    	if (s){
    		lay.setBackgroundResource(R.drawable.texture_mettalic_silver);
    	}else {
    		lay.setBackgroundResource(R.drawable.texture_mettalic_red);
    	}
    	layoutConnected=s;
    }
    
    public void setDebugOutput (String s){
    	TextView out = (TextView) findViewById(R.id.debug_output);
    	out.setText(s);
    }
    
    public void sendMessage (Message m){
    	if (!Network.sendMessage(m)){
    		if (!Network.isConnected()){
    			if (layoutConnected){
		            Context context = getApplicationContext();
		    		int duration = Toast.LENGTH_SHORT;
		    		setBackgroundConnected(false);
		            Toast toast = Toast.makeText(context, "Disconnected", duration);
		    		toast.show();
    			}
    		}
    	}
    }
}
