package com.examen.aloglife;

import android.app.Activity;

/**
 * Created by Girondins on 2017-03-30.
 */

public class Item {
    private String name,description;
    private int imgSource;

    public Item(String name, String description, String itemPic, Activity activity){
     //   int id = activity.getResources().getIdentifier(itemPic, "drawable", activity.getPackageName());
   //     imgSource = id;
        this.name = name;
        this.description = description;
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

}
