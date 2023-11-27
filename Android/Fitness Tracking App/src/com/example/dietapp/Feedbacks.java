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
import android.widget.ListView;
import android.widget.Toast;

public class Feedbacks extends Activity implements JsonResponse {
	
	EditText ed_feedback;
	Button bt_feedback;
	ListView lv_feedbacks;
	SharedPreferences sh;
	String[] feedbacks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedbacks);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed_feedback = (EditText) findViewById(R.id.ed_feedback);
		bt_feedback = (Button) findViewById(R.id.bt_feedback);
		lv_feedbacks = (ListView) findViewById(R.id.lv_feedbacks);
		
		getPrevFeedbacks();
		
		bt_feedback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String feedback = ed_feedback.getText().toString();
				if (feedback.equals("")) {
					ed_feedback.setError("Fill the field");
					ed_feedback.setFocusable(true);
				} else {
					JsonReq JR = new JsonReq(getApplicationContext());
			        JR.json_response = (JsonResponse) Feedbacks.this;
			        String q = "send_feedback/?login_id=" + sh.getString("login_id", "0") + "&feedback=" + feedback;
			        JR.execute(q);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedbacks, menu);
		return true;
	}

	void getPrevFeedbacks() {
		JsonReq JR = new JsonReq(getApplicationContext());
        JR.json_response = (JsonResponse) Feedbacks.this;
        String q = "view_my_feedback/?login_id=" + sh.getString("login_id", "0");
        JR.execute(q);
	}
	
	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (jo.getString("method").equals("view_my_feedback")) {
            	if (status.equalsIgnoreCase("success")) {
            		JSONArray ja = jo.getJSONArray("data");
            		if (ja.length() > 0) {
            			feedbacks = new String[ja.length()];
            			
            			for (int i = 0; i < ja.length(); i++) {
            				feedbacks[i] = ja.getJSONObject(i).getString("Description")
            						+ "\nReply : " + ja.getJSONObject(i).getString("reply")
            						+ "\nDate : " + ja.getJSONObject(i).getString("feedback_date") + "\n";
						}
            			
            			lv_feedbacks.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_list, feedbacks));
            		}
            	}
            } else if (jo.getString("method").equals("send_feedback")) {
            	if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    getPrevFeedbacks();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_LONG).show();
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
