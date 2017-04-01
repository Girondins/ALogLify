package com.examen.aloglife;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
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
        infotv.setText(" \n - You can unlock items for Boogie depending on your average \n \n - For example: Depending on your average step, you might unlock an item \n" +
                " \n" +
                " - You'll not know what the average is for each item until you have reached it \n \n - Items unlocked only stay unlocked as long as you meet the requirements");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView title = new TextView(getActivity());
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

}
