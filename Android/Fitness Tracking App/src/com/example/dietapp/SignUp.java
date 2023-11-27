package com.example.dietapp;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SignUp extends Activity implements JsonResponse {
	
	EditText ed_fname, ed_lname, ed_dob, ed_place, ed_phone, ed_email, ed_username, ed_password;
	RadioButton rb_male, rb_female, rb_others;
	Button bt_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		ed_fname = (EditText) findViewById(R.id.ed_fname);
		ed_lname = (EditText) findViewById(R.id.ed_lname);
		ed_dob = (EditText) findViewById(R.id.ed_dob);
		ed_place = (EditText) findViewById(R.id.ed_place);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_username = (EditText) findViewById(R.id.ed_username);
		ed_password = (EditText) findViewById(R.id.ed_password);
		rb_male = (RadioButton) findViewById(R.id.rb_male);
		rb_female = (RadioButton) findViewById(R.id.rb_male);
		rb_others = (RadioButton) findViewById(R.id.rb_male);
		bt_register = (Button) findViewById(R.id.bt_register);
		
		bt_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String first_name, last_name, dob, gender = "Male", place, phone, email, username, password;
				first_name = ed_fname.getText().toString();
				last_name = ed_lname.getText().toString();
				dob = ed_dob.getText().toString();
				if (rb_female.isChecked())
					gender = "Female";
				else if (rb_others.isChecked())
					gender = "Other";
				place = ed_place.getText().toString();
				email = ed_email.getText().toString();
				phone = ed_phone.getText().toString();
				username = ed_username.getText().toString();
				password = ed_password.getText().toString();
				
				int flg = 0;
				if (first_name.equals("")) {
					flg++;
					ed_fname.setError("Fill the field");
				}
				if (last_name.equals("")) {
					flg++;
					ed_lname.setError("Fill the field");
				}
				if (dob.equals("")) {
					flg++;
					ed_dob.setError("Fill the field");
				}
				if (place.equals("")) {
					flg++;
					ed_place.setError("Fill the field");
				}
				if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
					flg++;
					ed_email.setError("Enter valid email ID");
				}
				if (phone.equals("") || phone.length() != 10) {
					flg++;
					ed_phone.setError("Enter valid phone number");
				}
				if (username.equals("")) {
					flg++;
					ed_username.setError("Fill the field");
				}
				if (password.equals("")) {
					flg++;
					ed_password.setError("Fill the field");
				}
				
				if (flg == 0) {
					JsonReq JR = new JsonReq(getApplicationContext());
	                JR.json_response = (JsonResponse) SignUp.this;
	                String q = "register/?first_name=" + first_name + "&last_name=" + last_name + "&phone=" + phone
	                		+ "&email=" + email + "&place=" + place + "&username=" + username + "&password="
	                		+ password + "&dob=" + dob + "&gender=" + gender;
	                JR.execute(q);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {

                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            } else {
                Toast.makeText(getApplicationContext(), "Registration failed!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exc : " + e, Toast.LENGTH_LONG).show();
        }
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), Login.class));
	}
}
