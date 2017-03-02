package com.examen.aloglife;

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
	private Double bmr;
	private Controller cont;
	private String authCode,refToken,header;
	private SwipeRefreshLayout swipeContainer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		spineView = (RelativeLayout) findViewById(R.id.spineViewID);
		stepsBtn = (Button) findViewById(R.id.stepsBTN);
		calBtn = (Button) findViewById(R.id.calBTN);

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				 cont.updateInfo();
			}
		});
		// Configure the refreshing colors
		Log.d("Logging Loop", "Loooping");
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);




		extractInfo();

		cont = new Controller(this);
		cont.setupUpdate(authCode,refToken,header);
		cont.setBmr(bmr);




		stepsBtn.setText("Steps: " + stepTotal);
		calBtn.setText("Cal Burnt: " + calories);
		Log.d("STEEPY", " " + stepTotal);

		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		spineView.addView(initializeForView(new SimpleTest1(),config));
		//initialize(new SimpleTest1(), config);

		calBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				spineView.removeAllViews();
				spineView.addView(initializeForView(new SimpleTestLog(),config));
			}
		});
	}

	public void extractInfo(){
		stepTotal = getIntent().getExtras().getInt("steps");
		authCode = getIntent().getExtras().getString("auth");
		refToken = getIntent().getExtras().getString("ref");
		header = getIntent().getExtras().getString("header");
		calories = getIntent().getExtras().getInt("calories");
		bmr = getIntent().getExtras().getDouble("bmr");
		Log.d("Extract from intent", stepTotal + "  " + "/// " + header);
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

	public void updateInfo(int stepsTotal, int calories){
		this.stepTotal = stepsTotal;
		this.calories = calories;
		stepsBtn.setText("Steps: " + stepsTotal);
		calBtn.setText("Cal Burnt: " + calories);

	}

	public void refreshComplete(){
		swipeContainer.setRefreshing(false);
	}

}
