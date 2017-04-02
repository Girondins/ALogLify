package com.examen.aloglife;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
    private String timeZ;
    private int steps,calories,communication,browsing,totalBrows,totalCals,totalSteps,totalComm;
    private int fontColor;
    private int age,timer = 0;
    private long milisecondsMidnight;
    private Double aee,hoursSinceMidnight, toHaveBeenBurnt, midnightTime;
    private DatabaseConnect db;
    private AndroidLauncher al;
    private Character userCharacter;
    private String lastLoginBefore;
    private volatile boolean isFirst = false;
    private TakeTime tTime;
    private boolean hasShoes,hasGlasses,hasPhone;
    private int stepsTakenPerHourAvg,commPerHourAvg,calPerHourAvg,browPerHourAvg;
    private int isSize;
    private boolean isHappy;
    private Item[] shoeList,glassesList,handList;
    private int backgroundToLoad;





    public static enum SPINEVIEW{
        NORMALHAPPY,NORMALHAPPYSHOES,NORMALHAPPYSHOESTRAIN,NORMALHAPPYGLASSES,NORMALHAPPYPHONE,NORMALHAPPYSHOESNGLASSES,NORMALHAPPYSHOESNPHONE,NORMALHAPPYSHOENPHONETRAIN,NORMALHAPPYSHOENGLASSESTRAIN,NORMALHAPPYPHONENGLASSES,NORMALHAPPYGSP,NORMALHAPPYGSPTRAIN,
        NORMALSAD,NORMALSADSHOES,NORMALSADSHOESTRAIN,NORMALSADGLASSES,NORMALSADPHONE,NORMALSADSHOESNGLASSES,NORMALSADSHOESNPHONE,NORMALSADSHOENPHONETRAIN,NORMALSADSHOENGLASSESTRAIN,NORMALSADPHONENGLASSES,NORMALSADGSP,NORMALSADGSPTRAIN,

        FATHAPPY,FATHAPPYSHOES,FATHAPPYGLASSES,FATHAPPYPHONE,FATHAPPYSHOESNGLASSES,FATHAPPYSHOESNPHONE,FATHAPPYPHONENGLASSES,FATHAPPYGSP,
        FATSAD,FATSADSHOES,FATSADGLASSES,FATSADPHONE,FATSADSHOESNGLASSES,FATSADSHOESNPHONE,FATSADPHONENGLASSES,FATSADGSP,

        XXLHAPPY,XXLHAPPYGLASSES,XXLHAPPYPHONE,XXLHAPPYPHONENGLASSES,
        XXLSAD,XXLSADGLASSES,XXLSADPHONE,XXLSADPHONENGLASSES,
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
        this.activity = activity;
        db = new DatabaseConnect(activity);
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.birthday = birthday;
        tTime = new TakeTime();
        tTime.start();
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
        info.putInt("communication",communication);
        info.putInt("browsing",browsing);
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
        Log.d("LAST BEFORE CRASH", " YOLO");
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


    // Sony Life Log accepterar ej +? Måste konvertera + till %2B?

    public void setTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        this.theTime = df.format(c.getTime());
        DateTimeZone theTimeZone = DateTimeZone.forID( DateTimeZone.forTimeZone(TimeZone.getDefault()).toString());
        DateTime now = DateTime.now( theTimeZone );
        timeZ = now.toString().substring(23);
        timeZ = timeZ.replace(":","");
        timeZ = timeZ.replace("+","%2B");
        Log.d("Time is ", " " + timeZ);
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

    public void setCommunication(int communication){
        this.communication = communication;
        Log.d("COM IS", communication + "");
    }

    public void setBrowsing(int browsing){
        this.browsing = browsing;
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
                calculateTotalSteps();
                calculateTotalCals();
                calculateTotalComm();
                calculateTotalBrow();
                setParametersToLoad();
                al.setSpineView(selectedView);
                al.updateInfo();
                al.refreshComplete();
              //  al.initiateSpineView();
            }
        });
    }

    public void updateInfo(){
       // api.renewToken();
        if(userCharacter.getAge() == 0){
            Log.d("Update First", "IS first day");
            isFirst = true;
            api.isFirstDay(true);
            api.forRefresh();
        }else{
            Log.d("Update Not First", "Not first day");
            isFirst = false;
            api.isFirstDay(false);
            api.forRefresh();
        }
        Log.d("Uppp", "Op");
    }

    public void calcCalories(){
        if(isFirst == true){
            double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
            calories = (int) (aee + (bmr * (hoursSinceMidnight - fromMidnight)));
            Log.d("Calcing First", calories + " Time: " + (fromMidnight) + "Diff: " + hoursSinceMidnight);
        }else {
            calories = (int) (aee + (bmr * hoursSinceMidnight));
            Log.d("CALORIES BURNT  ", calories + "" + " SINCE: " + hoursSinceMidnight);
        }
    }

    public void hoursMidnight(){
        Calendar c = Calendar.getInstance(); //now
        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        long diff = (c.getTimeInMillis() - m.getTimeInMillis());
        milisecondsMidnight = diff;
        Log.d(" Differnece", diff + "");
        hoursSinceMidnight = ((double)diff / (double)(1000*60*60));
        Log.d("HOURS SINCE MIDNIGHT " + hoursSinceMidnight, " " + " TOTAL  MIN : " + milisecondsMidnight + " MILIS : " + diff);
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
            Log.d("Characther exists", userCharacter.getTotalCal() + " steps: " + userCharacter.getTotalSteps() + " MILI " + milisecondsMidnight);
        }else{
            createCharacter();
        }
        calculateCharsAge();
    }


    public void initiateChar(){
        calculateTotalCals();
        calculateTotalSteps();
        calculateTotalComm();
        calculateTotalBrow();
        setCalToBeBurnt();
        setParametersToLoad();
    }

    public boolean checkExistingChar(){
        return db.checkExistChar(userName);
    }

    public void createCharacter(){
            Log.d(" MILI  ", milisecondsMidnight + "");
            userCharacter = db.createCharacter(userName,todaysDate,milisecondsMidnight,timeZ);
            Log.d("Recieved Charac: ", "\nSteps: " + userCharacter.getTotalSteps() + "\nCals: " + userCharacter.getTotalCal());
    }

    public void calculateCharsAge(){
        String splitToday[] = todaysDate.split("-");
        String splitBirth[] = userCharacter.getDayofbirth().split("-");
        DateTime birth = new DateTime(Integer.parseInt(splitBirth[0]),Integer.parseInt(splitBirth[1]),Integer.parseInt(splitBirth[2]),0,0);
        DateTime today = new DateTime(Integer.parseInt(splitToday[0]),Integer.parseInt(splitToday[1]),Integer.parseInt(splitToday[2]),0,0);
        Days day = Days.daysBetween(birth,today);
        userCharacter.setAge(day.getDays());
        Log.d("Chars Age is: " , userCharacter.getAge() + " Days" + todaysDate);
    }

    public void calculateTotalCals(){
        Log.d("Total Cals: ", userCharacter.getTotalCal() + " and " + calories);
        totalCals = userCharacter.getTotalCal() + calories;
    }

    public void calculateTotalSteps(){
        Log.d("Total Steps: ", userCharacter.getTotalSteps() + " and " + steps);
        totalSteps = userCharacter.getTotalSteps() + steps;
    }

    public void calculateTotalComm(){
        Log.d("Total Comm: ", userCharacter.getCommunication() + " and " + communication);
        totalComm = userCharacter.getCommunication() + communication;
    }

    public void calculateTotalBrow(){
        Log.d("Total Comm: ", userCharacter.getBrowsing() + " and " + browsing);
        totalBrows = userCharacter.getBrowsing() + browsing;
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

    public int getTotalComm(){
        return this.totalComm;
    }

    public int getTotalBrows(){
        return this.totalBrows;
    }

    public int getTodaySteps(){
        return this.steps;
    }

    public int getTodayCals(){
        return  this.calories;
    }

    public int getTodayComm(){
        return this.communication;
    }

    public int getTodayBrow(){
        return this.browsing;
    }


    public String getTimeSpent(){
        String toReturn;
        int totalTime;
        totalTime = userCharacter.getTimeSpent() + timer;

        int hours = (int) totalTime / 3600;
        int remainder = (int) totalTime - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
        toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public String getCommTimeToday(){
        String toReturn;
        int hours = (int) getTodayComm() / 3600;
        int remainder = (int) getTodayComm() - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public String getCommTimeTotal(){
        String toReturn;
        int hours = (int) getTotalComm() / 3600;
        int remainder = (int) getTotalComm() - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public String getBrowTimeToday(){
        String toReturn;
        int hours = (int) getTodayBrow() / 3600;
        int remainder = (int) getTodayBrow() - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public String getBrowTimeTotal(){
        String toReturn;
        int hours = (int) getTotalBrows() / 3600;
        int remainder = (int) getTotalBrows() - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public String getBrowTimeAvg(){
        String toReturn;
        int hours = (int) (browPerHourAvg*24) / 3600;
        int remainder = (int) (browPerHourAvg*24) - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";


        return toReturn;
    }

    public String getBrowFirstToday(){
        String toReturn;

        int hours = (int) getTodayBrow() / 3600;
        int remainder = (int) getTodayBrow() - hours * 3600;
        int mins = remainder / 60;

        String textHour = (hours < 10 ? "0" : "") + hours;
        String textMintue = (mins < 10 ? "0" : "") + mins;


        toReturn = textHour + ":" + textMintue + " h";

        return toReturn;
    }

    public String getBrowFirstTotal(){
        String toReturn;

        int hours = (int) getTotalBrows() / 3600;
        int remainder = (int) getTotalBrows() - hours * 3600;
        int mins = remainder / 60;

        String textHour = (hours < 10 ? "0" : "") + hours;
        String textMintue = (mins < 10 ? "0" : "") + mins;


        toReturn = textHour + ":" + textMintue + " h";

        return toReturn;
    }

    public String getCommFirstToday(){
        String toReturn;

        int hours = (int) getTodayComm() / 3600;
        int remainder = (int) getTodayComm() - hours * 3600;
        int mins = remainder / 60;

        String textHour = (hours < 10 ? "0" : "") + hours;
        String textMintue = (mins < 10 ? "0" : "") + mins;


        toReturn = textHour + ":" + textMintue + " h";

        return toReturn;
    }

    public String getCommFirstTotal(){
        String toReturn;

        int hours = (int) getTotalComm() / 3600;
        int remainder = (int) getTotalComm() - hours * 3600;
        int mins = remainder / 60;

        String textHour = (hours < 10 ? "0" : "") + hours;
        String textMintue = (mins < 10 ? "0" : "") + mins;


        toReturn = textHour + ":" + textMintue + " h";

        return toReturn;
    }

    public void setCalToBeBurnt(){
        Calendar c = Calendar.getInstance();
        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        Log.d("What is NULL?", " AGE? " + userCharacter.getAge() + " CALSBURN?" + calorieBurnDay + " HSM? " + hoursSinceMidnight + "brH?" + burnRateHour+ "BFM? " + userCharacter.getBirthFromMidnight());
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
        Log.d("Check Midnight", "Mid: " + hoursSinceMidnight + " Night: " + fromMidnight );
        Log.d("Second Timer", fromMidnight + "");
        toHaveBeenBurnt = ((userCharacter.getAge()) * calorieBurnDay) + (hoursSinceMidnight * burnRateHour) - (fromMidnight * burnRateHour);

        Log.d("Calories To have Burnt:", " " + toHaveBeenBurnt);
        Log.d("Calories you have burn:", " " + totalCals);
    }

    public void checkIfUpload(){

        Log.d("Checking Upload: ", "Age: " + userCharacter.getAge() + " LastLogin: " + userCharacter.getLastLogin() + " Today Is: " + todaysDate );
        lastLoginBefore = userCharacter.getLastLogin();

        if(userCharacter.getAge()!=0 && !userCharacter.getLastLogin().equals(todaysDate)) {
            Log.d("Uploading data", " from: " + yesterdayDate);
            if(userCharacter.getFirstday() == 0 && userCharacter.getAge() > 0){
                Log.d("Uploading Spec", "Specii");
                api.getSpecYesterday();
                db.setFirstday(userCharacter.getUsername());
            }else
            api.getYesterdayActivties();
        }else{
            if(userCharacter.getAge() == 0) {
                isFirst = true;
                api.isFirstDay(true);
                api.renewToken();
                Log.d("Is First Day" , "WOOO");
            }else {
                isFirst = false;
                api.isFirstDay(false);
                api.renewToken();
            }
        }

        db.setLastLogin(todaysDate,userName);

    }

    public void uploadYesterday(int ySteps, double yAee, int yComm,int yBrow){
        int yCals = (int) (yAee + (bmr * 24));
        Log.d("Uploading: " , yCals + " and " + ySteps);
        db.uploadToDatabase(userName,yCals,ySteps,yComm,yBrow);
     //   api.setWait(false);
    }

    public void uploadSpeciYesteday(int ySteps, double yAee, int yComm, int yBrow){
        long hoursToUpload = 24 - (userCharacter.getBirthFromMidnight() / (1000*60*60));
        int yCals = (int) (yAee + (bmr * hoursToUpload));
        db.uploadToDatabase(userName,yCals,ySteps,yComm,yBrow);
        Log.d("Uploaded Spec: " , yCals + " and hours " + hoursToUpload);
    }

    public String firstTimer(){
        int minutes = (int) ((userCharacter.getBirthFromMidnight())/60000);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, minutes/60);
        cal.set(Calendar.MINUTE, minutes % 60);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Log.d(" Check Upload First ", sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public String getTimeZone(){
        return this.timeZ;
    }

    public int getFontColor(){
        return this.fontColor;
    }

    public int getToHaveBurntCal(){
        return this.toHaveBeenBurnt.intValue();
    }

    public String getLastLoginSession(){
        return this.lastLoginBefore;
    }

    public int getBackgroundToLoad(){
        return backgroundToLoad;
    }

    //TODO Determine which Spine animation to load on start and update
    // Determined by total calories burnt factor
    public void setParametersToLoad(){
        int checkSize,checkMood,checkShoes,checkComm, checkBrow;

        checkSize = determineSize();
        checkMood = determineMood();
        checkShoes = determineSteps();
        checkComm = determineComm();
        checkBrow = determineBrow();
        backgroundToLoad = determineTimeOfDay();

        checkInventory(checkShoes,checkBrow,checkComm);

        setupItemDesc();

        switch (checkSize){
            //Normal
            case 0:
                switch(checkMood){
                    //Happy
                    case 0:
                        Log.d("CHAR AAAGE:" , getCharacterAge() + "");
                        if(getCharacterAge() != 0) {
                            //HAR SKOR
                            if (hasShoes && !hasGlasses && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOES;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOESTRAIN;
                                        break;
                                }

                            }
                            //HAR GLASÖGON
                            else if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.NORMALHAPPYGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.NORMALHAPPYPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.NORMALHAPPYPHONENGLASSES;
                            }

                            //HAR GLASÖGON OCH SKOR
                            else if (hasGlasses && hasShoes && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOESNGLASSES;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOENGLASSESTRAIN;
                                        break;
                                }
                            }

                            //HAR SKOR OCH TELEFON
                            else if (!hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOESNPHONE;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALHAPPYSHOENPHONETRAIN;
                                        break;
                                }

                            }

                            //HAR ALLT
                            else if (hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALHAPPYGSP;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALHAPPYGSPTRAIN;
                                        break;
                                }
                            } else {
                                selectedView = SPINEVIEW.NORMALHAPPY;
                            }
                        }else
                            selectedView = SPINEVIEW.NORMALHAPPY;
                        break;
                    //Sad
                    case 1:
                        if(getCharacterAge() != 0) {
                            //HAR SKOR
                            if (hasShoes && !hasGlasses && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALSADSHOES;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALSADSHOESTRAIN;
                                        break;
                                }

                            }
                            //HAR GLASÖGON
                            else if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.NORMALSADGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.NORMALSADPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.NORMALSADPHONENGLASSES;
                            }

                            //HAR GLASÖGON OCH SKOR
                            else if (hasGlasses && hasShoes && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALSADSHOESNGLASSES;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALSADSHOENGLASSESTRAIN;
                                        break;
                                }
                            }

                            //HAR SKOR OCH TELEFON
                            else if (!hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALSADSHOESNPHONE;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALSADSHOENPHONETRAIN;
                                        break;
                                }

                            }

                            //HAR ALLT
                            else if (hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.NORMALSADGSP;
                                        break;
                                    //Train
                                    case 2:
                                        selectedView = SPINEVIEW.NORMALSADGSPTRAIN;
                                        break;
                                }
                            } else {
                                selectedView = SPINEVIEW.NORMALSAD;
                            }
                        }else
                            selectedView = SPINEVIEW.NORMALSAD;
                        break;
                }
                break;


            //Fat
            case 1:
                switch(checkMood){
                    //Happy
                    case 0:
                        if(getCharacterAge() != 0) {
                            //HAR SKOR
                            if (hasShoes && !hasGlasses && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATHAPPYSHOES;
                                        break;
                                }

                            }
                            //HAR GLASÖGON
                            else if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.FATHAPPYGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.FATHAPPYPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.FATHAPPYPHONENGLASSES;
                            }

                            //HAR GLASÖGON OCH SKOR
                            else if (hasGlasses && hasShoes && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATHAPPYSHOESNGLASSES;
                                        break;
                                }
                            }

                            //HAR SKOR OCH TELEFON
                            else if (!hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATHAPPYSHOESNPHONE;
                                        break;
                                }

                            }

                            //HAR ALLT
                            else if (hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATHAPPYGSP;
                                        break;
                                }
                            } else {
                                selectedView = SPINEVIEW.FATHAPPY;
                            }
                        }else
                            selectedView = SPINEVIEW.FATHAPPY;
                        break;
                    //Sad
                    case 1:
                        if(getCharacterAge() != 0) {
                            //HAR SKOR
                            if (hasShoes && !hasGlasses && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATSADSHOES;
                                        break;
                                }

                            }
                            //HAR GLASÖGON
                            else if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.FATSADGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.FATSADPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.FATSADPHONENGLASSES;
                            }

                            //HAR GLASÖGON OCH SKOR
                            else if (hasGlasses && hasShoes && !hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATSADSHOESNGLASSES;
                                        break;
                                }
                            }

                            //HAR SKOR OCH TELEFON
                            else if (!hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATSADSHOESNPHONE;
                                        break;
                                }

                            }

                            //HAR ALLT
                            else if (hasGlasses && hasShoes && hasPhone) {
                                switch (checkShoes) {
                                    //Adidas
                                    case 1:
                                        selectedView = SPINEVIEW.FATSADGSP;
                                        break;
                                }
                            } else {
                                selectedView = SPINEVIEW.FATSAD;
                            }
                        }else
                            selectedView = SPINEVIEW.FATSAD;
                        break;
                }
                break;
            //XXL
            case 2:
                switch(checkMood){
                    //Happy
                    case 0:
                        if(getCharacterAge() != 0) {
                            //HAR GLASÖGON
                            if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.XXLHAPPYGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.XXLHAPPYPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.XXLHAPPYPHONENGLASSES;
                            } else {
                                selectedView = SPINEVIEW.XXLHAPPY;
                            }
                        }else
                            selectedView = SPINEVIEW.XXLHAPPY;
                        break;
                    //Sad
                    case 1:

                        if(getCharacterAge() != 0) {
                            //HAR GLASÖGON
                            if (hasGlasses && !hasShoes && !hasPhone) {
                                selectedView = SPINEVIEW.XXLSADGLASSES;
                            }
                            //HAR TELEFON
                            else if (!hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.XXLSADPHONE;
                            }

                            //HAR TELEFON OCH GLASÖGON
                            else if (hasGlasses && !hasShoes && hasPhone) {
                                selectedView = SPINEVIEW.XXLSADPHONENGLASSES;
                            } else {
                                selectedView = SPINEVIEW.XXLSAD;
                            }
                        }else
                            selectedView = SPINEVIEW.XXLSAD;
                        break;
                }
                break;
        }
        Log.d(" PARA NULL" , totalCals + "  OR : " + toHaveBeenBurnt);

    }

    public int determineTimeOfDay(){
        if(hoursSinceMidnight>=6 && hoursSinceMidnight<=10){
            return  0;

        }

        if(hoursSinceMidnight>=10 && hoursSinceMidnight<=19){
            return  1;
        }

        if(hoursSinceMidnight>=19 && hoursSinceMidnight <= 21){
            return  2;
        }

        if(hoursSinceMidnight >= 21 && hoursSinceMidnight <= 23.99){
            return 3;
        }

        if(hoursSinceMidnight >= 0 && hoursSinceMidnight <= 6){
            return 3;
        }
            return 2;
    }

    public int determineSize(){
        int calorieFactor = (int) (bmr*3);
        int calDiff = (int) (totalCals - toHaveBeenBurnt);
        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));

        calPerHourAvg = (int) (totalCals / (((userCharacter.getAge()) * 24) + hoursSinceMidnight - fromMidnight));


        if(calDiff <= calorieFactor || -calorieFactor <= calDiff){
            fontColor = Color.rgb(236,234,62);
            isSize = 0;
            return 0;
        }

        if(calDiff > calorieFactor){
            fontColor = Color.rgb(141, 199, 63);
            isSize = 0;
            return 0;
        }

        if(calDiff < -calorieFactor){
            fontColor = Color.rgb(242,106,63);
            isSize = 1;
            return 1;
        }

        if(calDiff < -calorieFactor-(bmr*1.5)){
            fontColor = Color.rgb(255,51,51);
            isSize = 2;
            return 2;
        }

        return 0;
    }

    public int determineMood(){
        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
        int timeShouldSpent = (int) (((userCharacter.getAge()) * 380) + (hoursSinceMidnight * 16) - (fromMidnight * 20));


        Log.d("Time Shoud Spent: " + timeShouldSpent, " Time have Spent: " + userCharacter.getTimeSpent() +" Time: " + timer);

        if(timeShouldSpent<userCharacter.getTimeSpent()+timer){
            isHappy = true;
            return 0;
        }else{
            isHappy = false;
            return 1;
        }

    }

    public int determineSteps(){
        double low = 312.5,medium = 416.6;

        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
   //     int lowStepsCount = (int) (((userCharacter.getAge()) * 7500) + (hoursSinceMidnight * low) - (fromMidnight * low));
   //     int mediumStepsCount = (int) (((userCharacter.getAge()) * 10000) + (hoursSinceMidnight * medium) - (fromMidnight * medium));


        stepsTakenPerHourAvg = (int) (totalSteps / (((userCharacter.getAge()) * 24) + hoursSinceMidnight - fromMidnight));


        Log.d("TOTAL STEPPIS", totalSteps + " " + hoursSinceMidnight + " SINCE " + fromMidnight);
        Log.d("Steps per hour avg: " , stepsTakenPerHourAvg + " STEPS");
    //    Log.d("Steps per Hour low: ", lowStepsCount + " LOW");
    //    Log.d("Steps per Hour med: ", mediumStepsCount + " med");

        if(stepsTakenPerHourAvg>= medium){
            return 2;
        }

        if(stepsTakenPerHourAvg>=low){
            return 1;
        }


        return 0;


    }

    public int determineComm(){
        int avg = 190;

        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
    //    int avgSpentTime = (int) (((userCharacter.getAge()) * 4560) + (hoursSinceMidnight * avg) - (fromMidnight * avg));


        commPerHourAvg = (int) (totalComm / (((userCharacter.getAge()) * 24) + hoursSinceMidnight - fromMidnight));


        if(avg <= commPerHourAvg){
            return 1;
        }

        return 0;
    }

    public int determineBrow(){
        int avg = 25;

        Calendar m = Calendar.getInstance(); //midnight
        m.set(Calendar.HOUR_OF_DAY, 0);
        m.set(Calendar.MINUTE, 0);
        m.set(Calendar.SECOND, 0);
        m.set(Calendar.MILLISECOND, 0);
        double fromMidnight = ((double)userCharacter.getBirthFromMidnight() / (double)(1000*60*60));
     //   int avgSpentTime = (int) (((userCharacter.getAge()) * 600) + (hoursSinceMidnight * avg) - (fromMidnight * avg));

        browPerHourAvg = (int) (totalBrows / (((userCharacter.getAge()) * 24) + hoursSinceMidnight - fromMidnight));

        if(avg<= browPerHourAvg){
            return 1;
        }

        return 0;

    }

    public void checkInventory(int shoeCheck, int glassesCheck, int phoneCheck){

        if(shoeCheck > 0){
            hasShoes = true;
        }

        if(glassesCheck > 0){
            hasGlasses = true;
        }

        if(phoneCheck > 0){
            hasPhone = true;
        }

    }

    public boolean getMood(){
        return this.isHappy;
    }

    public int getIsSize(){
        return this.isSize;
    }

    public int getAvgSteps(){
        return this.stepsTakenPerHourAvg;
    }

    public String getAvgComm(){
        String toReturn;
        int hours = (int) (this.commPerHourAvg*24) / 3600;
        int remainder = (int) (this.commPerHourAvg*24) - hours * 3600;
        int mins = remainder / 60;

        if(hours == 0) {
            toReturn = mins + " minutes";

        }else
            toReturn = hours + " hours " + mins + " minutes";

        return toReturn;
    }

    public int getAvgCal(){
        return this.calPerHourAvg;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void openItemDialog(int toOpen){
        ItemDialog itemDialog = new ItemDialog();


        switch(toOpen){
            case 0:
                itemDialog.setup("Shoes",shoeList);
                break;
            case 1:
                itemDialog.setup("Glasses",glassesList);
                break;
            case 2:
                itemDialog.setup("Hands",handList);
                break;

        }

        itemDialog.show(activity.getFragmentManager(), "Items");
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void openInfoDialog(){
        InfoDialog info = new InfoDialog();
        info.show(activity.getFragmentManager(), "Info");

    }

    //TODO SET PICS WHEN HAVE
    public void setupItemDesc(){

        if(hasShoes){
            if(2 == determineSteps()){
                shoeList = new Item[]{new Item("Sneakers:", "Have an average of ","7500" ," steps per day","shoes2",activity,false),
                        new Item("Trainers:","Have an average of "," 10000 ", " steps per day","juanshoe",activity,false)};
            }else {
                shoeList = new Item[]{new Item("Sneakers",  "Have an average of ","7500", " steps per day", "shoes2", activity, false),
                        new Item("Trainers:",  "Have an average of "," ***** ", " steps per day", "juanshoe", activity, true)};
            }
        }else
            shoeList = new Item[]{new Item("Sneakers", "Have an average of ","****" ," steps per day","shoes2",activity,true),
                    new Item("Trainers:","Have an average of "," **** ", " steps per day","juanshoe",activity,true)};

        if(hasGlasses){
            glassesList = new Item[]{new Item("Readers:","Have browsing on a average of ","600"," seconds a day", "glasses", activity,false)};
        }else
            glassesList = new Item[]{new Item("Readers:","Have browsing on a average of ","****"," seconds a day", "glasses" , activity,true)};


        if(hasPhone){
            handList = new Item[]{new Item("Xperia:",  "Have communication on a average of ","4560"," seconds a day", "xperia", activity,false)};
        }else
            handList = new Item[]{new Item("Xperia:", "Have communication on a average of ", "****"," seconds a day", "xperia", activity,true)};

    }




    public void stopTimer(){
        tTime.stopTimer();
        db.updateTimeSpent(userCharacter.getUsername(),timer);
    }

    public void resumeTimer(){
        tTime.resumeTimer();
        timer = 0;
        tTime.start();
    }

    private class TakeTime extends Thread{
        private volatile boolean running = true;

        public void stopTimer(){
            running = false;
            Log.d("Timer: ", "Stop");
        }

        public void resumeTimer(){
            running = true;
            Log.d("Timer: ", "Start");
        }

        @Override
        public void run() {
            while(running){
                timer++;
                al.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        al.updateTimer(timer);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
