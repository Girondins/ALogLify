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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.examen.aloglife.normal.NormalHappy;
import com.examen.aloglife.normal.NormalHappyGSP;
import com.examen.aloglife.normal.NormalHappyGlasses;
import com.examen.aloglife.normal.NormalHappyPhone;
import com.examen.aloglife.normal.NormalHappyPhoneGlasses;
import com.examen.aloglife.normal.NormalHappyShoes;
import com.examen.aloglife.normal.NormalHappyShoesPhone;
import com.examen.aloglife.normal.NormalHappyShoesnGlasses;
import com.examen.aloglife.normal.NormalSad;
import com.examen.aloglife.normal.NormalSadGlasses;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout spineView;
	private Button stepsBtn,calBtn,ageBtn,comBtn,intBtn;
	private TextView infoTextview;
	private int stepsToday, caloriesToday,communicationToday,browsingToday;
	private int timer,fontColor;
	private Double bmr,weight,height;
	private Controller cont;
	private String authCode,refToken,header,userName,birthday;
	private SwipeRefreshLayout swipeContainer;
	private AndroidApplicationConfiguration config;
	private Controller.SPINEVIEW selectedView;
	private boolean ageIs = false,stepsIs = false,calIs = false, commIs = false;
	private float centerX,centerY,screenH,screenW;
	private AndroidApplicationConfiguration cfg;
	private boolean isPause = false;
	private LinearLayout starView,infoView;
	private ListView infoListView;
	private int infoToView = 0;


	//For InfoView
	private TextView todayTextView,totalTextView,todaySumView,totalSumView,headerView,avgView,avgSumView;
	private RelativeLayout boogieView;
	private Button returnBtn,shoeBtn,glassesBtn,bikeBtn,cameraBtn,prevBtn,nextBtn;



	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		extractInfo();
		setupController();
		initiateComp();
		initiateSwipe();
		initiateSpineView();


	}

	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	public void initiateComp(){
		starView = (LinearLayout) findViewById(R.id.startViewID);
		infoView = (LinearLayout) findViewById(R.id.infoViewID);
		spineView = (RelativeLayout) findViewById(R.id.spineViewID);
		stepsBtn = (Button) findViewById(R.id.stepsBTN);
		calBtn = (Button) findViewById(R.id.calBTN);
		ageBtn = (Button) findViewById(R.id.ageBTN);
		comBtn = (Button) findViewById(R.id.commBtnID);
		intBtn = (Button) findViewById(R.id.interactBtnID);
		infoTextview = (TextView) findViewById(R.id.infoTextID);
		stepsBtn.setOnClickListener(new OnStepsClick());
		calBtn.setOnClickListener(new OnCalorieClick());
		ageBtn.setOnClickListener(new OnAgeClick());
		comBtn.setOnClickListener(new OnComClick());
		intBtn.setOnClickListener(new OnIntClick());
		cfg = new AndroidApplicationConfiguration();
		cfg.r = cfg.g = cfg.b = cfg.a = 8;
		fontColor = cont.getFontColor();
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

	public void initiateSpineView(){
		spineView.removeAllViews();
		boolean happy = cont.getMood();
		int size = cont.getIsSize();

		switch(size){
			case 0:
				if(happy == true){
					setNormalHappy();
				}else{
					setNormalSad();
				}
				break;
			case 1:
				if(happy == true){
					setFatHappy();
				}else{
					setFatSad();
				}
				break;
			case 2:
				if(happy == true){
					setXXLHappy();
				}else{
					setXXLSad();
				}
				break;
		}

	}

	public void setNormalHappy(){

		switch(selectedView){

			case NORMALHAPPY:
				NormalHappy normalhappy = new NormalHappy(centerX,centerY);
				spineView.addView(initializeForView(normalhappy,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYGLASSES:
				NormalHappyGlasses normalHappyGlasses = new NormalHappyGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYGSP:
				NormalHappyGSP normalHappyGSP = new NormalHappyGSP(centerX,centerY);
				spineView.addView(initializeForView(normalHappyGSP,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYPHONE:
				NormalHappyPhone normalHappyPhone = new NormalHappyPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYPHONENGLASSES:
				NormalHappyPhoneGlasses normalHappyPhoneGlasses = new NormalHappyPhoneGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhoneGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYSHOES:
				NormalHappyShoes normalhappyshoes = new NormalHappyShoes(centerX,centerY);
				spineView.addView(initializeForView(normalhappyshoes,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYSHOESNGLASSES:
				NormalHappyShoesnGlasses normalHappyShoesnGlasses = new NormalHappyShoesnGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesnGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYSHOESNPHONE:
				NormalHappyShoesPhone normalHappyShoesPhone = new NormalHappyShoesPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			//TRAIN MISSING
		}

	}

	public void setNormalSad(){

	}

	public void setFatHappy(){

	}

	public void setFatSad(){

	}

	public void setXXLHappy(){

	}

	public void setXXLSad(){

	}

	public void initiateBoogieView(){
		spineView.removeAllViews();

				NormalHappyShoesPhone normalhappy = new NormalHappyShoesPhone(centerX,centerY);
				boogieView.addView(initializeForView(normalhappy,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
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
		Log.d("Setting Texts", "Steps " + cont.getTodaySteps() + "FONT COLOR: " + fontColor);
		calBtn.setTextColor(fontColor);
		stepsBtn.setText("Steps: " + cont.getTodaySteps());
		calBtn.setText("Cal Burnt: " + cont.getTodayCals());
		comBtn.setText("Com: " + cont.getTodayComm());
		ageBtn.setText("Char is: " + cont.getCharacterAge() + " days");
		infoTextview.setText("Character for account: " + userName);
	}

	public void setSpineView(Controller.SPINEVIEW selectedView){
		this.selectedView = selectedView;
	}

	public void setupController(){
		cont = new Controller(this,userName,height,weight,bmr,birthday);
		cont.setupUpdate(authCode,refToken,header);
		cont.setSteps(stepsToday);
		cont.setBurntCalories(caloriesToday);
		cont.setCommunication(communicationToday);
		cont.setBrowsing(browsingToday);
		cont.setBmr(bmr);
		cont.initiateChar();
	}

	public void extractInfo(){
		selectedView = (Controller.SPINEVIEW) getIntent().getSerializableExtra("view");
		stepsToday = getIntent().getExtras().getInt("steps");
		caloriesToday = getIntent().getExtras().getInt("calories");
		communicationToday = getIntent().getExtras().getInt("communication");
		browsingToday = getIntent().getExtras().getInt("browsing");
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

	public void updateInfo(){

		Log.d("Refresh status is: " , stepsIs + "" + calIs + ageIs + " COLOR FONT NOW" + cont.getFontColor());
		calBtn.setTextColor(cont.getFontColor());

		if(stepsIs == false){
			stepsBtn.setText("Steps: " + cont.getTodaySteps());
		}else {
			stepsBtn.setText("Total Steps: " + cont.getTotalSteps());
		}
		if(calIs == false) {
			calBtn.setText("Cal Burnt: " + cont.getTodayCals());
			infoTextview.setText("Have Burnt/Need to burn: " + cont.getTotalCals() + " / " + cont.getToHaveBurntCal() + " cal");
		}else {
			calBtn.setText("Total Cals: " + cont.getTotalCals());
			infoTextview.setText("Have Burnt/Need to burn: " + cont.getTotalCals() + " / " + cont.getToHaveBurntCal() + " cal");
		}
		if(ageIs == false) {
			ageBtn.setText("Char is: " + cont.getCharacterAge() + " days");
		}else {
			ageBtn.setText("Born: " + cont.getCharacterBirth());
		}

		if(commIs == false) {
			comBtn.setText("Com: " + cont.getTodayComm());
		}else {
			comBtn.setText("Com Total: " + cont.getTotalComm());
		}

	}

	public void updateTimer(int time){
		intBtn.setText("Time spent: " + time);
	}

	public void refreshComplete(){
		swipeContainer.setRefreshing(false);
	}

	protected void launchApp() {
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(
				"com.sonymobile.lifelog");
		if (mIntent != null) {
			try {
				startActivity(mIntent);
			} catch (ActivityNotFoundException err) {
				Toast t = Toast.makeText(getApplicationContext(),
						"App not found", Toast.LENGTH_SHORT);
				t.show();
			}
		}
	}

	public void initiateInfoViewComp(){
	//	infoListView = (ListView) findViewById(R.id.listViewID);
		boogieView = (RelativeLayout) findViewById(R.id.boogieViewID);
		returnBtn = (Button) findViewById(R.id.returnBtnID);
		prevBtn = (Button) findViewById(R.id.prevBtnID);
		nextBtn = (Button) findViewById(R.id.nextBtnID);
		shoeBtn = (Button) findViewById(R.id.shoesBtnID);
		glassesBtn = (Button) findViewById(R.id.glassesBtnID);
		bikeBtn = (Button) findViewById(R.id.bikeBtnID);
		cameraBtn = (Button) findViewById(R.id.cameraBtnID);
		totalTextView = (TextView) findViewById(R.id.overViewTotalID);
		todayTextView = (TextView) findViewById(R.id.overViewTodayID);
		todaySumView = (TextView) findViewById(R.id.overViewTodaySumID);
		totalSumView = (TextView) findViewById(R.id.overViewTotalSumID);
		headerView = (TextView) findViewById(R.id.overViewHeaderID);
		avgView = (TextView) findViewById(R.id.overViewAvgID);
		avgSumView = (TextView) findViewById(R.id.overViewAvgSumID);

		headerView.setText("Boogie");
		todayTextView.setText("Born: ");
		todaySumView.setText(cont.getCharacterBirth());
		totalTextView.setText("Age: ");
		totalSumView.setText(cont.getCharacterAge() + " days");
		avgView.setText("Time with Boogie: ");
		avgSumView.setText(cont.getTimeSpent());

		nextBtn.setOnClickListener(new OnNextClick());
		prevBtn.setOnClickListener(new OnPrevClick());
		returnBtn.setOnClickListener(new OnRtnClick());
		shoeBtn.setOnClickListener(new OnShoeClick());
		glassesBtn.setOnClickListener(new OnGlassesClick());
		bikeBtn.setOnClickListener(new OnNotImpClick());
		cameraBtn.setOnClickListener(new OnNotImpClick());





		initiateBoogieView();




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
				infoTextview.setText("Have Burnt/Need to burn: " + cont.getTotalCals() + " / " + cont.getToHaveBurntCal() + " cal");
				calIs = false;
			}else{
				calBtn.setText("Total Cals: " + cont.getTotalCals());
				infoTextview.setText("Have Burnt/Need to burn: " + cont.getTotalCals() + " / " + cont.getToHaveBurntCal() + " cal");
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

	private class OnComClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			spineView.removeAllViews();
			initiateInfoViewComp();
			starView.setVisibility(View.GONE);
			infoView.setVisibility(View.VISIBLE);
		}
	}

	private class OnIntClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
		}
	}

	private class OnRtnClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			boogieView.removeAllViews();
			infoView.setVisibility(View.GONE);
			starView.setVisibility(View.VISIBLE);
			initiateSpineView();
		}
	}

	private class OnShoeClick implements View.OnClickListener{
		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(View view) {
			cont.openItemDialog(0);
		}
	}

	private class OnGlassesClick implements View.OnClickListener{
		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(View view) {
			cont.openItemDialog(1);
		}
	}

	private class OnNotImpClick implements View.OnClickListener{
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplication(),"NOT YET TO BE IMPLEMENTED :(", Toast.LENGTH_SHORT).show();
		}
	}

	private class OnPrevClick implements View.OnClickListener{
		@Override
		public void onClick(View view) {

			if(infoToView == 0){
				infoToView = 4;
			}else
				infoToView --;



			switch(infoToView){


				case 0:
					headerView.setText("Boogie");
					todayTextView.setText("Born: ");
					todaySumView.setText(cont.getCharacterBirth());
					totalTextView.setText("Age: ");
					totalSumView.setText(cont.getCharacterAge() + " days");
					avgView.setText("Time with Boogie: ");
					avgSumView.setText(cont.getTimeSpent());

					break;

				case 1:
					headerView.setText("Calorie Burnt");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodayCals() + " cals");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalCals() + " cals");
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgCal() + " cals");
					break;

				case 2:
					headerView.setText("Walked");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodaySteps() + " steps");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalSteps() + " steps");
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgSteps() + " steps");
					break;

				case 3:
					headerView.setText("Communication");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getCommTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getCommTimeTotal());
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgComm());
					break;

				case 4:
					headerView.setText("Browsing");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getBrowTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getBrowTimeTotal());
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgComm());
					break;

			}

		}
	}

	private class OnNextClick implements View.OnClickListener{
		@Override
		public void onClick(View view) {

			if(infoToView == 4){
				infoToView = 0;
			}else
			infoToView ++;


			switch(infoToView){
				case 0:
					headerView.setText("Boogie");
					todayTextView.setText("Born: ");
					todaySumView.setText(cont.getCharacterBirth());
					totalTextView.setText("Age: ");
					totalSumView.setText(cont.getCharacterAge() + " days");
					avgView.setText("Time with Boogie: ");
					avgSumView.setText(cont.getTimeSpent());
					break;

				case 1:
					headerView.setText("Calorie Burnt");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodayCals() + " cals");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalCals() + " cals");
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgCal() + " cals");
					break;

				case 2:
					headerView.setText("Walked");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodaySteps() + " steps");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalSteps() + " steps");
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgSteps() + " steps");
					break;

				case 3:
					headerView.setText("Communication");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getCommTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getCommTimeTotal());
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgComm());
					break;

				case 4:
					headerView.setText("Browsing");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getBrowTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getBrowTimeTotal());
					avgView.setText("Average: ");
					avgSumView.setText(cont.getAvgComm());
					break;

			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Is Resume ", "RESUMING");
		if(isPause == true){
			cont.updateInfo();
			cont.resumeTimer();
			isPause = false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPause = true;
		cont.stopTimer();
		Log.d("Is Pause", "PAUSING");
	}

}
