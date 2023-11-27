package com.example.dietapp;

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
import android.widget.Toast;

public class FoodIntake extends Activity implements JsonResponse {
	
	EditText ed_food, ed_qty;
	Button bt_food_intake;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_intake);
		
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed_food = (EditText) findViewById(R.id.ed_food);
		ed_qty = (EditText) findViewById(R.id.ed_qty);
		bt_food_intake = (Button) findViewById(R.id.bt_food_intake);
		
		bt_food_intake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String food = ed_food.getText().toString();
				String qty = ed_qty.getText().toString();
				
				if (food.equals(""))
					ed_food.setError("Fill the field");
				else if (qty.equals(""))
					ed_qty.setError("Fill the field");
				else {
					JsonReq JR = new JsonReq(getApplicationContext());
                    JR.json_response = (JsonResponse) FoodIntake.this;
                    String q = "food_intake/?login_id=" + sh.getString("login_id", "0") + "&food=" + food + "&qty=" + qty;
                    JR.execute(q);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_intake, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), UserHome.class));
	}

	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), "success..", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), UserHome.class));
            } else {
                Toast.makeText(getApplicationContext(), "failed!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_LONG).show();
        }
	}

}
