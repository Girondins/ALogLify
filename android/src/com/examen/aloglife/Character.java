package com.examen.aloglife;

/**
 * Created by Girondins on 2017-03-06.
 */

public class Character {
    private String username,dayofbirth,lastLogin,birthTimeZone;
    private int totalSteps,totalCal,age,firstday,timeSpent,communication,browsing;
    private long birthFromMidnight;

    public Character(String username,String dayofbirth, int totalCal, int totalSteps, long birthFromMidnight, String lastLogin,String birthTimeZone , int firstday, int timeSpent , int communication,int browsing){
        this.username = username;
        this.dayofbirth = dayofbirth;
        this.totalCal = totalCal;
        this.totalSteps = totalSteps;
        this.birthFromMidnight = birthFromMidnight;
        this.lastLogin = lastLogin;
        this.birthTimeZone = birthTimeZone;
        this.firstday = firstday;
        this.timeSpent = timeSpent;
        this.communication = communication;
        this.browsing = browsing;
    }

    public void setAge(int age){
        this.age = age;
    }

    public String getLastLogin(){
        return this.lastLogin;
    }

    public int getAge(){
        return this.age;
    }

    public String getBirthTimeZone(){
        return this.birthTimeZone;
    }

    public long getBirthFromMidnight() {
        return birthFromMidnight;
    }

    public String getUsername(){
        return this.username;
    }

    public String getDayofbirth(){
        return this.dayofbirth;
    }

    public int getTotalSteps(){
        return this.totalSteps;
    }

    public int getTotalCal(){
        return this.totalCal;
    }

    public int getFirstday() {
        return this.firstday;
    }

    public int getTimeSpent(){
        return this.timeSpent;
    }

    public int getCommunication(){
        return this.communication;
    }

    public int getBrowsing(){
        return this.browsing;
    }
}
