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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HealthProfile extends Activity implements JsonResponse {
	
	EditText ed_bp, ed_sugar, ed_cholestrol, ed_weight, ed_height;
	Button bt_submit;
	SharedPreferences sh;
	String bp = "", sugar = "", cholestrol = "", weight = "", height = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_profile);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed_bp = (EditText) findViewById(R.id.ed_bp);
		ed_sugar = (EditText) findViewById(R.id.ed_sugar);
		ed_cholestrol = (EditText) findViewById(R.id.ed_cholestrol);
		ed_weight = (EditText) findViewById(R.id.ed_weight);
		ed_height = (EditText) findViewById(R.id.ed_height);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		
		getMyHealthDetails();
		
		bt_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				bp = ed_bp.getText().toString();
				sugar = ed_sugar.getText().toString();
				cholestrol = ed_cholestrol.getText().toString();
				weight = ed_weight.getText().toString();
				height = ed_height.getText().toString();
				
				JsonReq JR = new JsonReq(getApplicationContext());
		        JR.json_response = (JsonResponse) HealthProfile.this;
		        String q = "add_health_profile/?login_id=" + sh.getString("login_id", "0") + "&blood_pressure="
		        		+ bp + "&sugar_level=" + sugar + "&cholesterol_level=" + cholestrol + "&body_weight="
		        		+ weight + "&Height=" + height;
		        JR.execute(q);
			}
		});
	}
	
	void getMyHealthDetails() {
		JsonReq JR = new JsonReq(getApplicationContext());
        JR.json_response = (JsonResponse) HealthProfile.this;
        String q = "get_my_health_profile/?login_id=" + sh.getString("login_id", "0");
        JR.execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_health_profile, menu);
		return true;
	}
	
	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (jo.getString("method").equals("add_health_profile")) {
            	if (status.equalsIgnoreCase("success")) {
	                Toast.makeText(getApplicationContext(), "Success..!", Toast.LENGTH_LONG).show();
	                getMyHealthDetails();
            	}
            } else if (jo.getString("method").equals("get_my_health_profile")) {
            	if (status.equalsIgnoreCase("success")) {
            		JSONArray ja = (JSONArray) jo.getJSONArray("data");
                    if (ja.length() > 0) {
                    	ed_bp.setText(ja.getJSONObject(0).getString("blood_pressure"));
                    	ed_sugar.setText(ja.getJSONObject(0).getString("sugar_level"));
                    	ed_cholestrol.setText(ja.getJSONObject(0).getString("cholesterol_level"));
                    	ed_weight.setText(ja.getJSONObject(0).getString("body_weight"));
                    	ed_height.setText(ja.getJSONObject(0).getString("Height"));
                    }
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_LONG).show();
        }
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), UserHome.class));
	}
}
