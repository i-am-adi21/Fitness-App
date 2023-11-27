package com.example.dietapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class DiscussionDetails extends Activity implements JsonResponse {
	
	EditText ed_opinion;
	Button bt_opinion;
	ListView lv_details;
	SharedPreferences sh;
	String[] details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discussion_details);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed_opinion = (EditText) findViewById(R.id.ed_opinion);
		bt_opinion = (Button) findViewById(R.id.bt_opinion);
		lv_details = (ListView) findViewById(R.id.lv_details);
		
		getPrevComments();
		
		bt_opinion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String opinion = ed_opinion.getText().toString();
				if (opinion.equals("")) {
					ed_opinion.setError("Fill the field");
					ed_opinion.setFocusable(true);
				} else {
					try {
						JsonReq JR = new JsonReq(getApplicationContext());
				        JR.json_response = (JsonResponse) DiscussionDetails.this;
				        String q = "send_discussion_opinion/?login_id=" + sh.getString("login_id", "0") + "&discussion_id=" + sh.getString("discussion_id", "0") + "&opinion=" + opinion;
				        JR.execute(q);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	void getPrevComments() {
		JsonReq JR = new JsonReq(getApplicationContext());
        JR.json_response = (JsonResponse) DiscussionDetails.this;
        String q = "view_discussion_details/?discussion_id=" + sh.getString("discussion_id", "0");
        JR.execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.discussion_details, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		try { 
			if (jo.getString("method").equals("view_discussion_details")) {
				if (jo.getString("status").equals("success")) {
					JSONArray ja = (JSONArray) jo.getJSONArray("data");
					if (ja.length() > 0) {
						details = new String[ja.length()];
						for (int i = 0; i < ja.length(); i++) {
							details[i] = ja.getJSONObject(i).getString("fname") + " " + ja.getJSONObject(i).getString("lname")
									+ "\n" + ja.getJSONObject(i).getString("comment_description") + "\n"
									+ ja.getJSONObject(i).getString("details_date") + "\n";
						}
						lv_details.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_list, details));
					}
				}
			}
			
			if (jo.getString("method").equals("send_discussion_opinion")) {
				if (jo.getString("status").equals("success")) {
					Toast.makeText(getApplicationContext(), "Success..!!", Toast.LENGTH_SHORT).show();
					getPrevComments();
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), ViewDiscussions.class));
	}
}
