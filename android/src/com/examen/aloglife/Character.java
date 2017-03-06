package com.examen.aloglife;

/**
 * Created by Girondins on 2017-03-06.
 */

public class Character {
    private String username,dayofbirth,lastLogin;
    private int totalSteps,totalCal,age;
    private double birthFromMidnight;

    public Character(String username,String dayofbirth, int totalCal, int totalSteps, double birthFromMidnight, String lastLogin){
        this.username = username;
        this.dayofbirth = dayofbirth;
        this.totalCal = totalCal;
        this.totalSteps = totalSteps;
        this.birthFromMidnight = birthFromMidnight;
        this.lastLogin = lastLogin;
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

    public double getBirthFromMidnight() {
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
