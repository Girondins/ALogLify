package com.examen.aloglife;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.examen.aloglife.fat.FatHappy;
import com.examen.aloglife.fat.FatHappyGSP;
import com.examen.aloglife.fat.FatHappyGlasses;
import com.examen.aloglife.fat.FatHappyPhone;
import com.examen.aloglife.fat.FatHappyPhoneGlasses;
import com.examen.aloglife.fat.FatHappyShoes;
import com.examen.aloglife.fat.FatHappyShoesPhone;
import com.examen.aloglife.fat.FatHappyShoesnGlasses;
import com.examen.aloglife.fat.FatSad;
import com.examen.aloglife.fat.FatSadGSP;
import com.examen.aloglife.fat.FatSadGlasses;
import com.examen.aloglife.fat.FatSadPhone;
import com.examen.aloglife.fat.FatSadPhoneGlasses;
import com.examen.aloglife.fat.FatSadShoes;
import com.examen.aloglife.fat.FatSadShoesPhone;
import com.examen.aloglife.fat.FatSadShoesnGlasses;
import com.examen.aloglife.normal.NormalHappy;
import com.examen.aloglife.normal.NormalHappyGSP;
import com.examen.aloglife.normal.NormalHappyGSPTrain;
import com.examen.aloglife.normal.NormalHappyGlasses;
import com.examen.aloglife.normal.NormalHappyPhone;
import com.examen.aloglife.normal.NormalHappyPhoneGlasses;
import com.examen.aloglife.normal.NormalHappyShoes;
import com.examen.aloglife.normal.NormalHappyShoesPhone;
import com.examen.aloglife.normal.NormalHappyShoesPhoneTrain;
import com.examen.aloglife.normal.NormalHappyShoesTrain;
import com.examen.aloglife.normal.NormalHappyShoesnGlasses;
import com.examen.aloglife.normal.NormalHappyShoesnGlassesTrain;
import com.examen.aloglife.normal.NormalSad;
import com.examen.aloglife.normal.NormalSadGSP;
import com.examen.aloglife.normal.NormalSadGSPTrain;
import com.examen.aloglife.normal.NormalSadGlasses;
import com.examen.aloglife.normal.NormalSadPhone;
import com.examen.aloglife.normal.NormalSadPhoneGlasses;
import com.examen.aloglife.normal.NormalSadShoes;
import com.examen.aloglife.normal.NormalSadShoesPhone;
import com.examen.aloglife.normal.NormalSadShoesPhoneTrain;
import com.examen.aloglife.normal.NormalSadShoesTrain;
import com.examen.aloglife.normal.NormalSadShoesnGlasses;
import com.examen.aloglife.normal.NormalSadShoesnGlassesTrain;

import java.util.Timer;
import java.util.TimerTask;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout spineView;
	private Button stepsBtn,calBtn,ageBtn,comBtn,intBtn,lifeLogBtn,overViewBtn;
	private TextView infoTextview,ageTextView;
	private int stepsToday, caloriesToday,communicationToday,browsingToday;
	private int timer,fontColor;
	private Double bmr,weight,height;
	private Controller cont;
	private String authCode,refToken,header,userName,birthday;
	private SwipeRefreshLayout swipeContainer;
	private AndroidApplicationConfiguration config;
	private Controller.SPINEVIEW selectedView;
	private int backgroundToLoad;
	private boolean ageIs = false,stepsIs = false,calIs = false, commIs = false;
	private float centerX,centerY,screenH,screenW;
	private AndroidApplicationConfiguration cfg;
	private boolean isPause = false;
	private LinearLayout starView,infoView;
	private ListView infoListView;
	private int infoToView = 0;
	private Typeface glossFont;
	private Typeface pidgeFont;


	//For InfoView
	private TextView todayTextView,totalTextView,todaySumView,totalSumView,headerView,avgView,avgSumView,overviewTV,inventoryTV;
	private RelativeLayout boogieView;
	private Button returnBtn,shoeBtn,glassesBtn, handBtn,cameraBtn,prevBtn,nextBtn;
	private ImageButton infoBtn;



	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		glossFont = Typeface.createFromAsset(getApplication().getAssets(), "fonts/gloss.ttf");
		pidgeFont = Typeface.createFromAsset(getApplication().getAssets(), "fonts/pidge.ttf");
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
		lifeLogBtn = (Button) findViewById(R.id.lifelogBtnID);
		infoTextview = (TextView) findViewById(R.id.infoTextID);
		ageTextView = (TextView) findViewById(R.id.ageTextID);
		overViewBtn = (Button) findViewById(R.id.overviewBtnID);
		stepsBtn.setOnClickListener(new OnStepsClick());
		calBtn.setOnClickListener(new OnCalorieClick());
		ageBtn.setOnClickListener(new OnAgeClick());
		comBtn.setOnClickListener(new OnComClick());
		lifeLogBtn.setOnClickListener(new OnLifeLogClick());
		overViewBtn.setOnClickListener(new OnOverviewClick());
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

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void setBackground(){
		switch(backgroundToLoad){
			case 0:
				starView.setBackground(getResources().getDrawable(R.drawable.backrise,null));
				break;
			case 1:
				starView.setBackground(getResources().getDrawable(R.drawable.backsun,null));
				break;
			case 2:
				starView.setBackground(getResources().getDrawable(R.drawable.backlow,null));
				break;
			case 3:
				starView.setBackground(getResources().getDrawable(R.drawable.backnight,null));
				break;

		}
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void initiateSpineView(){

		backgroundToLoad = cont.getBackgroundToLoad();
		setBackground();

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

			case NORMALHAPPYSHOENPHONETRAIN:
				NormalHappyShoesPhoneTrain normalHappyShoesPhoneTrain = new NormalHappyShoesPhoneTrain(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesPhoneTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYSHOESTRAIN:
				NormalHappyShoesTrain normalHappyShoesTrain = new NormalHappyShoesTrain(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYSHOENGLASSESTRAIN:
				NormalHappyShoesnGlassesTrain normalHappyShoesnGlassesTrain = new NormalHappyShoesnGlassesTrain(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesnGlassesTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALHAPPYGSPTRAIN:
				NormalHappyGSPTrain normalHappyGSPTrain = new NormalHappyGSPTrain(centerX,centerY);
				spineView.addView(initializeForView(normalHappyGSPTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

		}

	}

	public void setNormalSad(){

		switch(selectedView){

			case NORMALSAD:
				NormalSad normalSad= new NormalSad(centerX,centerY);
				spineView.addView(initializeForView(normalSad,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADGLASSES:
				NormalSadGlasses normalSadGlasses = new NormalSadGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalSadGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADGSP:
				NormalSadGSP normalSadGSP = new NormalSadGSP(centerX,centerY);
				spineView.addView(initializeForView(normalSadGSP,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADPHONE:
				NormalSadPhone normalSadPhone = new NormalSadPhone(centerX,centerY);
				spineView.addView(initializeForView(normalSadPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADPHONENGLASSES:
				NormalSadPhoneGlasses normalSadPhoneGlasses = new NormalSadPhoneGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalSadPhoneGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOES:
				NormalSadShoes normalSadshoes = new NormalSadShoes(centerX,centerY);
				spineView.addView(initializeForView(normalSadshoes,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOESNGLASSES:
				NormalSadShoesnGlasses normalSadShoesnGlasses = new NormalSadShoesnGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalSadShoesnGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOESNPHONE:
				NormalSadShoesPhone normalSadShoesPhone = new NormalSadShoesPhone(centerX,centerY);
				spineView.addView(initializeForView(normalSadShoesPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOENPHONETRAIN:
				NormalSadShoesPhoneTrain normalSadShoesPhoneTrain = new NormalSadShoesPhoneTrain(centerX,centerY);
				spineView.addView(initializeForView(normalSadShoesPhoneTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOESTRAIN:
				NormalSadShoesTrain normalSadShoesTrain = new NormalSadShoesTrain(centerX,centerY);
				spineView.addView(initializeForView(normalSadShoesTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADSHOENGLASSESTRAIN:
				NormalSadShoesnGlassesTrain normalSadShoesnGlassesTrain = new NormalSadShoesnGlassesTrain(centerX,centerY);
				spineView.addView(initializeForView(normalSadShoesnGlassesTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case NORMALSADGSPTRAIN:
				NormalSadGSPTrain normalSadGSPTrain = new NormalSadGSPTrain(centerX,centerY);
				spineView.addView(initializeForView(normalSadGSPTrain,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

		}

	}

	public void setFatHappy(){

		switch(selectedView){

			case FATHAPPY:
				FatHappy Fathappy = new FatHappy(centerX,centerY);
				spineView.addView(initializeForView(Fathappy,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYGLASSES:
				FatHappyGlasses FatHappyGlasses = new FatHappyGlasses(centerX,centerY);
				spineView.addView(initializeForView(FatHappyGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYGSP:
				FatHappyGSP normalHappyGSP = new FatHappyGSP(centerX,centerY);
				spineView.addView(initializeForView(normalHappyGSP,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYPHONE:
				FatHappyPhone normalHappyPhone = new FatHappyPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYPHONENGLASSES:
				FatHappyPhoneGlasses normalHappyPhoneGlasses = new FatHappyPhoneGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhoneGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYSHOES:
				FatHappyShoes normalhappyshoes = new FatHappyShoes(centerX,centerY);
				spineView.addView(initializeForView(normalhappyshoes,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYSHOESNGLASSES:
				FatHappyShoesnGlasses normalHappyShoesnGlasses = new FatHappyShoesnGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesnGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATHAPPYSHOESNPHONE:
				FatHappyShoesPhone normalHappyShoesPhone = new FatHappyShoesPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

		}


	}

	public void setFatSad(){

		switch(selectedView){

			case FATSAD:
				FatSad Fathappy = new FatSad(centerX,centerY);
				spineView.addView(initializeForView(Fathappy,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADGLASSES:
				FatSadGlasses FatHappyGlasses = new FatSadGlasses(centerX,centerY);
				spineView.addView(initializeForView(FatHappyGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADGSP:
				FatSadGSP normalHappyGSP = new FatSadGSP(centerX,centerY);
				spineView.addView(initializeForView(normalHappyGSP,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADPHONE:
				FatSadPhone normalHappyPhone = new FatSadPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADPHONENGLASSES:
				FatSadPhoneGlasses normalHappyPhoneGlasses = new FatSadPhoneGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyPhoneGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADSHOES:
				FatSadShoes normalhappyshoes = new FatSadShoes(centerX,centerY);
				spineView.addView(initializeForView(normalhappyshoes,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADSHOESNGLASSES:
				FatSadShoesnGlasses normalHappyShoesnGlasses = new FatSadShoesnGlasses(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesnGlasses,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

			case FATSADSHOESNPHONE:
				FatSadShoesPhone normalHappyShoesPhone = new FatSadShoesPhone(centerX,centerY);
				spineView.addView(initializeForView(normalHappyShoesPhone,cfg));
				if (graphics.getView() instanceof SurfaceView) {
					SurfaceView glView = (SurfaceView) graphics.getView();
					glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
					glView.setZOrderOnTop(true);
				}
				break;

		}

	}

	public void setXXLHappy(){

	}

	public void setXXLSad(){

	}

	public void initiateBoogieView(){
		spineView.removeAllViews();

				FatSadShoesnGlasses normalhappy = new FatSadShoesnGlasses(centerX,centerY);
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
		comBtn.setText("Com: " + cont.getCommTimeToday());
		ageBtn.setText("Brow: " + cont.getBrowTimeToday());
		ageTextView.setText("Char age is: " + cont.getCharacterAge() + " days");
		calBtn.setTypeface(glossFont);
		ageBtn.setTypeface(glossFont);
		stepsBtn.setTypeface(glossFont);
		comBtn.setTypeface(glossFont);
		ageTextView.setTypeface(glossFont);
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
		}else {
			calBtn.setText("Total Cals: " + cont.getTotalCals());
		}
		if(ageIs == false) {
			ageBtn.setText("Brow: " + cont.getBrowTimeToday());
		}else {
			ageBtn.setText("Brow: " + cont.getBrowTimeTotal());
		}

		if(commIs == false) {
			comBtn.setText("Com: " + cont.getCommTimeToday());
		}else {
			comBtn.setText("Com Total: " + cont.getCommTimeTotal());
		}

	}

	public void updateTimer(int time){
//		intBtn.setText("Time spent: " + time);
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
		handBtn = (Button) findViewById(R.id.bikeBtnID);
		cameraBtn = (Button) findViewById(R.id.cameraBtnID);
		infoBtn = (ImageButton) findViewById(R.id.infoBtnID);
		totalTextView = (TextView) findViewById(R.id.overViewTotalID);
		todayTextView = (TextView) findViewById(R.id.overViewTodayID);
		todaySumView = (TextView) findViewById(R.id.overViewTodaySumID);
		totalSumView = (TextView) findViewById(R.id.overViewTotalSumID);
		headerView = (TextView) findViewById(R.id.overViewHeaderID);
		avgView = (TextView) findViewById(R.id.overViewAvgID);
		avgSumView = (TextView) findViewById(R.id.overViewAvgSumID);
		overviewTV = (TextView) findViewById(R.id.overviewID);
		inventoryTV = (TextView) findViewById(R.id.inventoryID);


		totalTextView.setTypeface(glossFont);
		todaySumView.setTypeface(glossFont);
		todayTextView.setTypeface(glossFont);
		totalSumView.setTypeface(glossFont);
		avgSumView.setTypeface(glossFont);
		avgView.setTypeface(glossFont);
		headerView.setTypeface(glossFont);
		shoeBtn.setTypeface(glossFont);
		glassesBtn.setTypeface(glossFont);
		handBtn.setTypeface(glossFont);
		cameraBtn.setTypeface(glossFont);
		overviewTV.setTypeface(glossFont);
		inventoryTV.setTypeface(glossFont);
		returnBtn.setTypeface(glossFont);

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
		handBtn.setOnClickListener(new OnHandClick());
		cameraBtn.setOnClickListener(new OnNotImpClick());
		infoBtn.setOnClickListener(new OnInfoClick());





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
				ageBtn.setText("Brow: " + cont.getBrowTimeToday());
				ageIs = false;
			}else{
				ageBtn.setText("Brow Total: " + cont.getBrowTimeTotal());
				ageIs = true;
			}
		}
	}

	private class OnComClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			if(commIs) {
				comBtn.setText("Com: " + cont.getCommTimeToday());
				commIs = false;
			}else {
				comBtn.setText("Com Total: " + cont.getCommTimeTotal());
				commIs = true;
			}
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

	private class OnHandClick implements View.OnClickListener{
		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(View view) {
			cont.openItemDialog(2);
		}
	}

	private class OnNotImpClick implements View.OnClickListener{
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplication(),"Not yet to be implemented :(", Toast.LENGTH_SHORT).show();
		}
	}

	private class OnInfoClick implements View.OnClickListener{

		@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(View view) {
			cont.openInfoDialog();

		}
	}

	private class OnLifeLogClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			launchApp();
		}
	}

	private class OnOverviewClick implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			spineView.removeAllViews();
			initiateInfoViewComp();
			starView.setVisibility(View.GONE);
			infoView.setVisibility(View.VISIBLE);
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
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgCal() + " cals");
					break;

				case 2:
					headerView.setText("Walked");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodaySteps() + " steps");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalSteps() + " steps");
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgSteps() + " steps");
					break;

				case 3:
					headerView.setText("Communication");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getCommTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getCommTimeTotal());
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgComm());
					break;

				case 4:
					headerView.setText("Browsing");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getBrowTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getBrowTimeTotal());
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getBrowTimeAvg());
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
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgCal() + " cals");
					break;

				case 2:
					headerView.setText("Walked");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getTodaySteps() + " steps");
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getTotalSteps() + " steps");
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgSteps() + " steps");
					break;

				case 3:
					headerView.setText("Communication");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getCommTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getCommTimeTotal());
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getAvgComm());
					break;

				case 4:
					headerView.setText("Browsing");
					todayTextView.setText("Today: ");
					todaySumView.setText(cont.getBrowTimeToday());
					totalTextView.setText("Total: ");
					totalSumView.setText(cont.getBrowTimeTotal());
					avgView.setText("Average/H: ");
					avgSumView.setText(cont.getBrowTimeAvg());
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
