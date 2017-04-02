package com.examen.aloglife;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girondins on 2017-03-29.
 */

public class ListViewAdapter extends ArrayAdapter <Item>{
    private LayoutInflater inflater;
    private Item[] itemList;

    public ListViewAdapter(Context context, Item[] itemList) {
        super(context, R.layout.listviewadapter,itemList);
        this.itemList = itemList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView itemName,itemDesc;
        ImageView itemImg;
        Typeface tp = Typeface.createFromAsset(getContext().getAssets(), "fonts/soft.otf");
        if(convertView==null){
            convertView = inflater.inflate(R.layout.listviewadapter,parent,false);
        }

        itemName = (TextView) convertView.findViewById(R.id.itemNameID);
        itemDesc = (TextView) convertView.findViewById(R.id.itemDescID);
        itemImg = (ImageView) convertView.findViewById(R.id.itemViewID);

        itemName.setTypeface(tp);
        itemDesc.setTypeface(tp);

        itemImg.setImageResource(itemList[position].getItemPic());
        itemName.setText(itemList[position].getName());
        itemDesc.setText(itemList[position].getDescription() + itemList[position].getCount() + itemList[position].getEnding());

        if(itemList[position].getIsBlurred()){
            itemImg.setAlpha((float) 0.2);
            Log.d("Blurring", itemList[position].getName());
        }


        return convertView;
    }
}
