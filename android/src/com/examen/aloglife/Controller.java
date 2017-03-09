package com.examen.aloglife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.text.DateFormat;
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
    private String todaysDate,yesterdayDate;
    private String theTime;
    private String userName;
    private Double height;
    private Double weight;
    private Double bmr, calorieBurnDay, burnRateHour;
    private String birthday;
    private String headerVal;
    private int steps,calories,totalCals,totalSteps;
    private int age;
    private Double aee,hoursSinceMidnight, toHaveBeenBurnt;
    private DatabaseConnect db;
    private AndroidLauncher al;
    private Character userCharacter;


    public static enum SPINEVIEW{
        IDLE,FAT,FIT,NORMAL
    }

    private SPINEVIEW selectedView;


    public Controller(Activity activity){
        this.activity = activity;
        setDate();
        setYesterday();
        setTime();
        hoursMidnight();
        db = new DatabaseConnect(activity);
    }

    public Controller(Activity activity, String userName,Double height,Double weight, Double bmr, String birthday){
        al = (AndroidLauncher) activity;
        db = new DatabaseConnect(activity);
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.birthday = birthday;
        setDate();
        setTime();
        getCharacter();
        hoursMidnight();
     //   setPersonalInfo(userName,height,weight,bmr,birthday);
      //  al.initiateSpineView(1);
    }


    public void enterLifeLog(){
        initiateChar();
        Intent i = new Intent(activity, AndroidLauncher.class);
        Bundle info = new Bundle();
        info.putInt("steps",steps);
        info.putInt("calories",calories);
        info.putString("user",userName);
        info.putDouble("height",height);
        info.putDouble("weight",weight);
        info.putDouble("bmr",bmr);
        info.putString("birthday",birthday);
        info.putString("auth",authCode);
        info.putString("ref",refToken);
        info.putString("header",headerVal);
        info.putSerializable("view",selectedView);
        Log.d("Renew", calories + "");
        i.putExtras(info);
        activity.startActivity(i);
        activity.finish();
    }

    public void setupUpdate(String authCode,String refToken, String headerVal){
        Log.d(" AUTHCODE    /", headerVal);
        api = new ApiConnector(authCode,refToken,headerVal,this, ApiConnector.FETCH.UPDATE);
        getAge();
      //  toHaveBurntCalToday();
    }

    public void setDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.todaysDate = df.format(c.getTime());
        Log.d("Date is ", " " + todaysDate);
    }

    public void setYesterday(){
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        yesterdayDate = dateFormat.format(cal.getTime());
        Log.d("Yesteday was ", " " + yesterdayDate);
    }

    public void setTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        this.theTime = df.format(c.getTime());
        Log.d("Time is ", " " + TimeZone.getDefault());
    }

    public void setHeader(String headerVal){
        this.headerVal = headerVal;
    }

    public void setSteps(int steps){
        Log.d("SETTING STEPS", " " + steps);
        this.steps = steps;
    }

    public void setAee(Double aee){
        this.aee = aee;
        Log.d("AEE IS", aee + "");
    }

    public void setBmr(Double bmr){
        this.bmr = bmr;
    }

    public void setBurntCalories(int calories){
        this.calories = calories;
    }

    public void setRefreshToken(String refToken){
        this.refToken = refToken;
    }

    public void setAuthCode(String authCode){
        this.authCode = authCode;
        api = new ApiConnector(authCode,this, ApiConnector.FETCH.LOGIN);
        getUserToken();
    }

    public void setPersonalInfo(String userName,Double height,Double weight, Double bmr, String birthday){
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.birthday = birthday;
        getCharacter();
        checkIfUpload();

    }

    public void refreshComplete(){
        al.runOnUiThread(new Runnable() {
            @Override
            public void run() {
        //        calculateCharsAge();
        //        calculateTotalSteps();
                al.initiateSpineView(SPINEVIEW.NORMAL);
                al.updateInfo(steps,calories);
                al.refreshComplete();
            }
        });
    }

    public void updateInfo(){
       // api.renewToken();
        api.forRefresh();
        Log.d("Uppp", "Op");
    }

    public void calcCalories(){
        calories = (int) (aee + (bmr * hoursSinceMidnight));
        Log.d("CALORIES BURNT  ", calories + "" );
    }

    public void hoursMidnight(){
        Calendar c = Calendar.getInstance(); //now
        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        long diff = (c.getTimeInMillis() - m.getTimeInMillis());
        hoursSinceMidnight = 1 + ((double)diff / (double)(1000*60*60));
        Log.d("HOURS SINCE MIDNIGHT ", hoursSinceMidnight + " ");
    }

    public String getDate(){
        return this.todaysDate;
    }

    public String getYesterday(){
        return this.yesterdayDate;
    }

    public void getAge(){
        int year,month,day;
        String[] split = birthday.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]);
        day = Integer.parseInt(split[2]);

        LocalDate birthdate = new LocalDate (year, month, day);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        this.age = age.getYears();
        Log.d(" You are: ", this.age + "" );
    }

    public void getUserToken(){
        api.authorize(authCode);
    }

    public void calculateCalorieIntake(){
        double addingExercise = 1.375;
        calorieBurnDay = addingExercise * (bmr * 24);
        burnRateHour = calorieBurnDay/24;
        Log.d(" Burn: ", " "+ calorieBurnDay);
        Log.d(" To Burn/h: ", "" + burnRateHour);
    }
    /**
    public void toHaveBurntCalToday(){
        accumulatedCals = (int) (hoursSinceMidnight * burnRateHour);
    }
**/
    public void getCharacter(){
        calculateCalorieIntake();
        if(checkExistingChar()){
            userCharacter = db.getCharacter(userName);
            Log.d("Characther exists", userCharacter.getTotalCal() + " steps: " + userCharacter.getTotalCal());
        }else{
            createCharacter();
        }
        calculateCharsAge();
    }


    public void initiateChar(){
        db.setLastLogin(todaysDate,userName);
        calculateTotalCals();
        calculateTotalSteps();
        setCalToBeBurnt();
        setSpineToLoad();
    }

    public boolean checkExistingChar(){
        return db.checkExistChar(userName);
    }

    public void createCharacter(){
            userCharacter = db.createCharacter(userName,todaysDate,hoursSinceMidnight);
            Log.d("Recieved Charac: ", "\nSteps: " + userCharacter.getTotalSteps() + "\nCals: " + userCharacter.getTotalCal());
    }

    public void calculateCharsAge(){
        String splitToday[] = todaysDate.split("-");
        String splitBirth[] = userCharacter.getDayofbirth().split("-");
        DateTime birth = new DateTime(Integer.parseInt(splitBirth[0]),Integer.parseInt(splitBirth[1]),Integer.parseInt(splitBirth[2]),0,0);
        DateTime today = new DateTime(Integer.parseInt(splitToday[0]),Integer.parseInt(splitToday[1]),Integer.parseInt(splitToday[2]),0,0);
        Days day = Days.daysBetween(birth,today);
        userCharacter.setAge(day.getDays());
        Log.d("Chars Age is: " , userCharacter.getAge() + " Days");
    }

    public void calculateTotalCals(){
        Log.d("Total Cals: ", userCharacter.getTotalCal() + " and " + calories);
        totalCals = userCharacter.getTotalCal() + calories;
    }

    public void calculateTotalSteps(){
        Log.d("Total Steps: ", userCharacter.getTotalSteps() + " and " + steps);
        totalSteps = userCharacter.getTotalSteps() + steps;
    }

    public int getCharacterAge(){
        return this.userCharacter.getAge();
    }

    public String getCharacterBirth(){
        return this.userCharacter.getDayofbirth();
    }

    public int getTotalCals(){
        return this.totalCals;
    }

    public int getTotalSteps(){
        return this.totalSteps;
    }

    public void setCalToBeBurnt(){
        Log.d("What is NULL?", " AGE? " + userCharacter.getAge() + " CALSBURN?" + calorieBurnDay + " HSM? " + hoursSinceMidnight + "brH?" + burnRateHour+ "BFM? " + userCharacter.getBirthFromMidnight());
        toHaveBeenBurnt = ((userCharacter.getAge()) * calorieBurnDay) + (hoursSinceMidnight * burnRateHour) - (userCharacter.getBirthFromMidnight() * burnRateHour);
        Log.d("Calories To have Burnt:", " " + toHaveBeenBurnt);
        Log.d("Calories you have burn:", " " + totalCals);
    }

    //TODO FIXA UPLOAD IF AGE = 1 ATT ENDAST UPLOAD FRÅN HOURS FROM MIDNIGHT NEGATIVE
    public void checkIfUpload(){
        Log.d("Checking Upload: ", "Age: " + userCharacter.getAge() + " LastLogin: " + userCharacter.getLastLogin() + " Today Is: " + todaysDate );
        if(userCharacter.getAge()!=0 && !userCharacter.getLastLogin().equals(todaysDate)) {
            Log.d("Uploading data", " from: " + yesterdayDate);
            api.getYesterdayActivties();
        }else{
            api.renewToken();
        }

    }

    public void uploadYesterday(int ySteps, double yAee){
        int yCals = (int) (yAee + (bmr * 24));
        Log.d("Uploading: " , yCals + " and " + ySteps);
        db.uploadToDatabase(userName,yCals,ySteps);
     //   api.setWait(false);


    }

    //TODO Determine which Spine animation to load on start and update
    // Determined by total calories burnt factor
    public void setSpineToLoad(){
        selectedView = SPINEVIEW.NORMAL;
    }

}
