package com.example.dietapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewSuggestions extends Activity implements JsonResponse {
	
	ListView lv_suggestions;
	SharedPreferences sh;
	String[] suggestions,datedon,val;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_suggestions);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		lv_suggestions = (ListView) findViewById(R.id.lv_suggestions);
		
		JsonReq JR = new JsonReq(getApplicationContext());
        JR.json_response = (JsonResponse) ViewSuggestions.this;
        String q = "view_suggestions/?login_id=" + sh.getString("login_id", "0");
        JR.execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_suggestions, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		try {
			String status = jo.getString("status");
			if (status.equalsIgnoreCase("success")) {
	    		JSONArray ja = jo.getJSONArray("data");
	    		if (ja.length() > 0) {
	    			
	    			suggestions = new String[ja.length()];
	    			datedon = new String[ja.length()];
	    			val = new String[ja.length()];
	    			
	    			for (int i = 0; i < ja.length(); i++) {
	    				
	    				suggestions[i] = ja.getJSONObject(i).getString("suggested_diet_details") ;
	    				datedon[i]= ja.getJSONObject(i).getString("suggestion_date") + "\n";
	    				
	    				val[i]="\nSuggestion : "+suggestions[i]+"Date : "+datedon[i];
					}
	    			
	    			lv_suggestions.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_list, val));
	    		}
	    	}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), UserHome.class));
	}
}