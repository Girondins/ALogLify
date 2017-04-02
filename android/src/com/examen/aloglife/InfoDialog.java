package com.examen.aloglife;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Girondins on 2017-03-30.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InfoDialog extends DialogFragment {
    private LayoutInflater inflater;

    private View v;
    private TextView infotv;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.infodialog, null);
        infotv = (TextView) v.findViewById(R.id.infoTVID);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.MyDialog);
        infotv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/soft.otf"));
        infotv.setText(" \n - You can unlock items for Boogie depending on your average \n \n - For example: Depending on your average step, you might unlock an item \n" +
                " \n" +
                " - You'll not know what the average is for each item until you have reached it \n \n - Items unlocked only stay unlocked as long as you meet the requirements \n \n" +
                " - Items can not be unlocked when character is 0 days old" );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView title = new TextView(getActivity());
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/soft.otf"));
        title.setText("Inventory Info: ");
        title.setBackgroundColor(Color.WHITE);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);


        builder.setView(v);
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
            }
        });


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/soft.otf"));
    }
}
