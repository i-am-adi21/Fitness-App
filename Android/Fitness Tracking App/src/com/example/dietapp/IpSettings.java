package com.example.dietapp;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IpSettings extends Activity {
	
	EditText ed_ip;
	Button bt_ip;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_settings);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed_ip = (EditText) findViewById(R.id.ed_ip);
		bt_ip = (Button) findViewById(R.id.bt_ip);
		
		ed_ip.setText(sh.getString("ip", "192.168.0.0:0000"));
		
		bt_ip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String ipVal = ed_ip.getText().toString();
				if (ipVal.equals("")) {
					ed_ip.setError("IP address");
				} else {
					Editor ed = sh.edit();
					ed.putString("ip", ipVal);
					ed.commit();
					startActivity(new Intent(getApplicationContext(), Login.class));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip_settings, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), IpSettings.class));
	}
}
