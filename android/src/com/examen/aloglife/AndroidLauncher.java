package com.examen.aloglife;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.examen.aloglife.libgdxrun;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout spineView;
	private Button steps;
	private int stepTotal;
	private int timer;
	private Controller cont;
	private String authCode,refToken,header;
	private SwipeRefreshLayout swipeContainer;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		spineView = (RelativeLayout) findViewById(R.id.spineViewID);
		steps = (Button) findViewById(R.id.stepsBTN);

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




		steps.setText("Steps: " + stepTotal);
		Log.d("STEEPY", " " + stepTotal);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		spineView.addView(initializeForView(new SimpleTest1(),config));
		//initialize(new SimpleTest1(), config);
	}

	public void extractInfo(){
		stepTotal = getIntent().getExtras().getInt("steps");
		authCode = getIntent().getExtras().getString("auth");
		refToken = getIntent().getExtras().getString("ref");
		header = getIntent().getExtras().getString("header");
		Log.d("Extract from intent", authCode + "  " + "/// " + header);
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
	public void refreshComplete(){
		swipeContainer.setRefreshing(false);
	}

}
