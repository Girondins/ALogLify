package com.examen.aloglife;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Girondins on 2017-03-30.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ItemDialog extends DialogFragment {
    private LayoutInflater inflater;
    private View v;
    private String title;
    private ListView itemsView;
    private Item[] items;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.itemdialog, null);
        itemsView = (ListView) v.findViewById(R.id.itemListViewID);
        itemsView.setAdapter(new ListViewAdapter(getActivity(),items));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void setup(String title,Item[] items){
        this.title = title;
        this.items = items;
    }
}
