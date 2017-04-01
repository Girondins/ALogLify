package com.examen.aloglife;

import android.app.Activity;

/**
 * Created by Girondins on 2017-03-30.
 */

public class Item {
    private String name,description,count,ending;
    private int imgSource;
    private boolean isBlurred;

    public Item(String name, String description, String count, String ending, String itemPic, Activity activity, boolean isBlurred){
        int id = activity.getResources().getIdentifier(itemPic, "drawable", activity.getPackageName());
        imgSource = id;
        this.name = name;
        this.count = count;
        this.description = description;
        this.ending = ending;
        this.isBlurred = isBlurred;
    }

    public int getItemPic(){
        return this.imgSource;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean getIsBlurred(){
        return isBlurred;
    }

    public String getCount(){
        return this.count;
    }

    public String getEnding(){
        return this.ending;
    }

}
