package com.examen.aloglife;

/**
 * Created by Girondins on 2017-03-06.
 */

public class Character {
    private String username,dayofbirth,lastLogin,birthTimeZone;
    private int totalSteps,totalCal,age;
    private long birthFromMidnight;

    public Character(String username,String dayofbirth, int totalCal, int totalSteps, long birthFromMidnight, String lastLogin,String birthTimeZone){
        this.username = username;
        this.dayofbirth = dayofbirth;
        this.totalCal = totalCal;
        this.totalSteps = totalSteps;
        this.birthFromMidnight = birthFromMidnight;
        this.lastLogin = lastLogin;
        this.birthTimeZone = birthTimeZone;
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
}
