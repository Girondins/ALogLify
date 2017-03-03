package com.examen.aloglife;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout spineView;
	private Button stepsBtn,calBtn;
	private int stepTotal,calories;
	private int timer;
	private Double bmr,weight,height;
	private Controller cont;
	private String authCode,refToken,header,userName,birthday;
	private SwipeRefreshLayout swipeContainer;
	private AndroidApplicationConfiguration config;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		extractInfo();
		initiateComp();
		setupController();
		initiateSwipe();


	}

	public void initiateComp(){
		spineView = (RelativeLayout) findViewById(R.id.spineViewID);
		stepsBtn = (Button) findViewById(R.id.stepsBTN);
		calBtn = (Button) findViewById(R.id.calBTN);
		config = new AndroidApplicationConfiguration();
		setTexts();

	}

	public void initiateSpineView(int toPlay){

		switch(toPlay){
			case 0:
				spineView.addView(initializeForView(new SimpleTest1(),config));
				break;
			case 1:
				spineView.addView(initializeForView(new SimpleTestLog(),config));
		}

	}

	public void initiateSwipe(){
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				cont.updateInfo();
			}
		});
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	public void setTexts(){
		Log.d("Setting Texts", "Steps " + stepTotal);
		stepsBtn.setText("Steps: " + stepTotal);
		calBtn.setText("Cal Burnt: " + calories);
	}

	public void setupController(){
		cont = new Controller(this,userName,height,weight,bmr,birthday);
		cont.setupUpdate(authCode,refToken,header);
		cont.setSteps(stepTotal);
		cont.setBurntCalories(calories);
		cont.setBmr(bmr);
	}

	public void extractInfo(){
		stepTotal = getIntent().getExtras().getInt("steps");
		calories = getIntent().getExtras().getInt("calories");
		authCode = getIntent().getExtras().getString("auth");
		refToken = getIntent().getExtras().getString("ref");
		header = getIntent().getExtras().getString("header");
		bmr = getIntent().getExtras().getDouble("bmr");
		userName = getIntent().getExtras().getString("user");
		birthday = getIntent().getExtras().getString("birthday");
		height = getIntent().getExtras().getDouble("height");
		weight = getIntent().getExtras().getDouble("weight");
	//	Log.d("Extract from intent", stepTotal + "  " + "/// " + header);
	}

	public void updateInfo(int stepsTotal, int calories){
		this.stepTotal = stepsTotal;
		this.calories = calories;
		stepsBtn.setText("Steps: " + stepsTotal);
		calBtn.setText("Cal Burnt: " + calories);

	}

	public void refreshComplete(){
		swipeContainer.setRefreshing(false);
	}


	protected void launchApp(String packageName) {
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (mIntent != null) {
			try {
				sendBroadcast(mIntent);
			} catch (ActivityNotFoundException err) {
				Toast t = Toast.makeText(getApplicationContext(),
						"App not found", Toast.LENGTH_SHORT);
				t.show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if(timer == 0){
			Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
			Timer time = new Timer();
			time.schedule(new TimerTask() {
				@Override
				public void run() {
					timer = 0;
				}
			},2000);
			timer = 1;
		}else{
			Log.d("Finsih", "Shut down");
			finish();
		}


	}


}
