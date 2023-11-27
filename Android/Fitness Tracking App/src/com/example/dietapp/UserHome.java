package com.example.dietapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class UserHome extends Activity {
	
	Button bt_diets,bt_stepcounts,bt_health_profile, bt_food_intake, bt_diet_suggestion, bt_view_discussion, bt_feedbacks, bt_view_donors, bt_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		
		bt_health_profile = (Button) findViewById(R.id.bt_health_profile);
		bt_food_intake = (Button) findViewById(R.id.bt_food_intake);
		bt_diet_suggestion = (Button) findViewById(R.id.bt_diet_suggestion);
		bt_view_discussion = (Button) findViewById(R.id.bt_view_discussion);
		bt_feedbacks = (Button) findViewById(R.id.bt_feedbacks);
		bt_view_donors = (Button) findViewById(R.id.bt_view_donors);
		bt_stepcounts = (Button) findViewById(R.id.bt_viewstepcount);
		bt_logout = (Button) findViewById(R.id.bt_logout);
		bt_diets = (Button) findViewById(R.id.bt_diets);
		
		bt_health_profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), HealthProfile.class));
			}
		});
		
		bt_diets.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), Viewdietrules.class));
			}
		});
		
		bt_stepcounts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), Walkingdetails.class));
			}
		});
		
		bt_food_intake.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), FoodIntake.class));
			}
		});
		
		bt_diet_suggestion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), ViewSuggestions.class));
			}
		});
		
		bt_view_discussion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), ViewDiscussions.class));
			}
		});
		
		bt_feedbacks.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), Feedbacks.class));
			}
		});
		
		bt_view_donors.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), ViewDonors.class));
			}
		});
		
		bt_logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), Login.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_home, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), UserHome.class));
	}
}
