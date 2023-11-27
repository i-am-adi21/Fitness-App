package com.example.dietapp;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.dietapp.ShakeDetector.OnShakeListener;

public class Walkingdetails extends Activity {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	EditText e1;
	Button b1;
	
	public static Integer vals=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walkingdetails);
		e1=(EditText)findViewById(R.id.editText1);
		b1=(Button)findViewById(R.id.button1);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				vals=0;
				e1.setText(vals+"");
			}
		});
		

		 //////////////////////////////////////////////////////////////////
	     // ShakeDetector initialization
	     		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	     		mAccelerometer = mSensorManager
	     				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	     		mShakeDetector = new ShakeDetector();
	     		mShakeDetector.setOnShakeListener(new OnShakeListener() {
					
					 
					
					public void onShake(int count) {
						// TODO Auto-generated method stub
						vals=vals+1;
						
						
						e1.setText(vals+"");
						
						
					}
				});
	      
	     		///////////////////////////////////////////////
	     		
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walkingdetails, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// Add the following line to register the Session Manager Listener onResume
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
	}
 
	@Override
	public void onPause() {
		// Add the following line to unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);
		super.onPause();
	}
	

}
