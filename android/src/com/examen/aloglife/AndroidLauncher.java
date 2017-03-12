package com.examen.aloglife;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Timer;
import java.util.TimerTask;

import static com.examen.aloglife.Controller.SPINEVIEW.NORMAL;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout spineView;
	private Button stepsBtn,calBtn,ageBtn;
	private int stepsToday, caloriesToday;
	private int timer;
	private Double bmr,weight,height;
	private Controller cont;
	private String authCode,refToken,header,userName,birthday;
	private SwipeRefreshLayout swipeContainer;
	private AndroidApplicationConfiguration config;
	private Controller.SPINEVIEW selectedView;
	private boolean ageIs = false,stepsIs = false,calIs = false;
	private float centerX,centerY,screenH,screenW;
	private AndroidApplicationConfiguration cfg;


	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		extractInfo();
		setupController();
		initiateComp();
		initiateSwipe();
		initiateSpineView(selectedView);


	}

	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	public void initiateComp(){
		spineView = (RelativeLayout) findViewById(R.id.spineViewID);
		stepsBtn = (Button) findViewById(R.id.stepsBTN);
		calBtn = (Button) findViewById(R.id.calBTN);
		ageBtn = (Button) findViewById(R.id.ageBTN);
		stepsBtn.setOnClickListener(new OnStepsClick());
		calBtn.setOnClickListener(new OnCalorieClick());
		ageBtn.setOnClickListener(new OnAgeClick());
		cfg = new AndroidApplicationConfiguration();
		cfg.r = cfg.g = cfg.b = cfg.a = 8;

		getCenter();
		setTexts();

	}

	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	public void getCenter(){
		Display display = getWindowManager().getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		screenH = size.y;
		screenW = size.x;
		centerY=screenH/2;
		centerX=screenW/2;
	}

	public void initiateSpineView(Controller.SPINEVIEW toPlay){
		spineView.removeAllViews();

		switch(toPlay){
			case NORMAL:
				Boggi idle = new Boggi(centerX,centerY);
				spineView.addView(initializeForView(idle,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;
			case FAT:
				spineView.addView(initializeForView(new SimpleTestLog(),cfg));
				break;
		}

	}

	public void initiateSwipe(){
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.d("Refresh status before: " , stepsIs + "" + calIs + ageIs + "");
				cont.updateInfo();
			}
		});
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	public void setTexts(){
		Log.d("Setting Texts", "Steps " + cont.getTodaySteps());
		stepsBtn.setText("Steps: " + cont.getTodaySteps());
		calBtn.setText("Cal Burnt: " + cont.getTodayCals());
		ageBtn.setText("Char is: " + cont.getCharacterAge() + " days");
	}

	public void setupController(){
		cont = new Controller(this,userName,height,weight,bmr,birthday);
		cont.setupUpdate(authCode,refToken,header);
		cont.setSteps(stepsToday);
		cont.setBurntCalories(caloriesToday);
		cont.setBmr(bmr);
		cont.initiateChar();
	}

	public void extractInfo(){
		selectedView = (Controller.SPINEVIEW) getIntent().getSerializableExtra("view");
		stepsToday = getIntent().getExtras().getInt("steps");
		caloriesToday = getIntent().getExtras().getInt("calories");
		authCode = getIntent().getExtras().getString("auth");
		refToken = getIntent().getExtras().getString("ref");
		header = getIntent().getExtras().getString("header");
		bmr = getIntent().getExtras().getDouble("bmr");
		userName = getIntent().getExtras().getString("user");
		birthday = getIntent().getExtras().getString("birthday");
		height = getIntent().getExtras().getDouble("height");
		weight = getIntent().getExtras().getDouble("weight");
		Log.d(" IS THIS NULL?!" , caloriesToday + "");
	//	Log.d("Extract from intent", stepsToday + "  " + "/// " + header);
	}

	public void updateInfo(int stepsTotal, int calories){
		this.stepsToday = stepsTotal;
		this.caloriesToday = calories;

		Log.d("Refresh status is: " , stepsIs + "" + calIs + ageIs + "");

		if(stepsIs == false){
			stepsBtn.setText("Steps: " + stepsToday);
		}else {
			stepsBtn.setText("Total Steps: " + cont.getTotalSteps());
		}
		if(calIs == false) {
			calBtn.setText("Cal Burnt: " + calories);
		}else {
			calBtn.setText("Total Cals: " + cont.getTotalCals());
		}
		if(ageIs == false) {
			ageBtn.setText("Char is: " + cont.getCharacterAge() + " days");
		}else {
			ageBtn.setText("Born: " + cont.getCharacterBirth());
		}

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

	//TODO CREATE REVERSE WHEN CLICKED

	private class OnStepsClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			if(stepsIs){
				stepsBtn.setText("Steps: " + cont.getTodaySteps());
				stepsIs = false;
			}else{
				stepsBtn.setText("Total Steps: " + cont.getTotalSteps());
				stepsIs = true;
			}
		}
	}

	private class OnCalorieClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			if(calIs){
				calBtn.setText("Cal Burnt: " + cont.getTodayCals());
				calIs = false;
			}else{
				calBtn.setText("Total Cals: " + cont.getTotalCals());
				calIs = true;
			}
		}
	}

	private class OnAgeClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			if(ageIs){
				ageBtn.setText("Char is: " + cont.getCharacterAge() + " days");
				ageIs = false;
			}else{
				ageBtn.setText("Born: " + cont.getCharacterBirth());
				ageIs = true;
			}
		}
	}

}
