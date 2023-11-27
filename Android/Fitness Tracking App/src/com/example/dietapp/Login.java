package com.example.dietapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements JsonResponse{
	
	EditText ed_username, ed_password;
	Button bt_login;
	TextView tv_signup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ed_username = (EditText) findViewById(R.id.ed_username);
		ed_password = (EditText) findViewById(R.id.ed_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		tv_signup = (TextView) findViewById(R.id.tv_signup);
		
		bt_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String username = ed_username.getText().toString();
				String password = ed_password.getText().toString();
				
				if (username.equals(""))
					ed_username.setError("Username please");
				else if (password.equals(""))
					ed_password.setError("Password please");
				else {
					JsonReq JR = new JsonReq(getApplicationContext());
                    JR.json_response = (JsonResponse) Login.this;
                    String q = "login/?username=" + username + "&password=" + password;
                    JR.execute(q);
				}
			}
		});
		
		tv_signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), SignUp.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                JSONArray ja = (JSONArray) jo.getJSONArray("data");
                String log_id = ja.getJSONObject(0).getString("logid");

                SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                ed.putString("login_id", log_id);
                ed.commit();
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), UserHome.class));
            } else {
                Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_LONG).show();
        }
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), IpSettings.class));
	}

}
