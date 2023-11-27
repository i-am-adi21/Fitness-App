package com.example.dietapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewDiscussions extends Activity implements JsonResponse, OnItemClickListener {
	
	ListView lv_discussions;
	SharedPreferences sh;
	String[] master_ids, titles, created_dates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_discussions);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		lv_discussions = (ListView) findViewById(R.id.lv_discussions);
		lv_discussions.setOnItemClickListener(this);
		
		JsonReq JR = new JsonReq(getApplicationContext());
        JR.json_response = (JsonResponse) ViewDiscussions.this;
        String q = "view_discussions/";
        JR.execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_discussions, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja = (JSONArray) jo.getJSONArray("data");
                if (ja.length() > 0) {
                	master_ids = new String[ja.length()];
                	titles = new String[ja.length()];
                	created_dates = new String[ja.length()];
                	String[] details = new String[ja.length()];
                	
                	for (int i = 0; i < ja.length(); i++) {
                		master_ids[i] = ja.getJSONObject(i).getString("master_id");
                		titles[i] = ja.getJSONObject(i).getString("title");
                		created_dates[i] = ja.getJSONObject(i).getString("created_date");
                		
                		details[i] = titles[i] + "\nDated on : " + created_dates[i] + "\n";
					}
                	
                	lv_discussions.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_list, details));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Nothing to show you..!!", Toast.LENGTH_LONG).show();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Editor ed = sh.edit();
		ed.putString("discussion_id", master_ids[arg2]);
		ed.commit();
		startActivity(new Intent(getApplicationContext(), DiscussionDetails.class));
	}
}
