package com.examen.aloglife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Girondins on 2017-01-30.
 */

public class Controller {
    private String authCode,refToken;
    private ApiConnector api;
    private Activity activity;
    private String todaysDate;
    private String theTime;
    private String userName;
    private Double height;
    private Double weight;
    private Double bmr;
    private String birthday;
    private String headerVal;
    private int steps;


    public Controller(Activity activity){
        this.activity = activity;
        setDate();
        setTime();
    }

    public void setAuthCode(String authCode){
        this.authCode = authCode;
        api = new ApiConnector(authCode,this, ApiConnector.FETCH.LOGIN);
        getUserToken();
    }

    public void setRefreshToken(String refToken){
        this.refToken = refToken;
    }

    public void setupUpdate(String authCode,String refToken, String headerVal){
        Log.d(" AUTHCODE    /", headerVal);
        api = new ApiConnector(authCode,refToken,headerVal,this, ApiConnector.FETCH.UPDATE);
     //   api.renewToken();
     //   Log.d("Renew", refToken);
    }

    public void getUserToken(){
        api.authorize(authCode);
    }

    public void enterLifeLog(){
        Intent i = new Intent(activity, AndroidLauncher.class);
        Bundle info = new Bundle();
        info.putInt("steps",steps);
        info.putString("auth",authCode);
        info.putString("ref",refToken);
        info.putString("header",headerVal);
        Log.d("Renew", refToken);
        i.putExtras(info);
        activity.startActivity(i);
        activity.finish();
    }

    public void setDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.todaysDate = df.format(c.getTime());
        Log.d("Date is ", " " + todaysDate);
    }

    public void setTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        this.theTime = df.format(c.getTime());
        Log.d("Time is ", " " + theTime);
    }

    public String getDate(){
        return this.todaysDate;
    }

    public void setPersonalInfo(String userName,Double height,Double weight, Double bmr, String birthday){
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.birthday = birthday;

    }
    public void refreshComplete(){
        final AndroidLauncher al = (AndroidLauncher) activity;
        al.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                al.refreshComplete();
            }
        });
    }

    public void updateInfo(){
      //  api.renewToken();
        api.getToday();
        Log.d("Uppp", "Op");
    }

    public void setHeader(String headerVal){
        this.headerVal = headerVal;
    }

    public void setSteps(int steps){
        this.steps = steps;
    }

}
