package com.harunuyar.studentassistant.ÖsymHelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Harun on 24.01.2017.
 */

public class CustomTask extends AsyncTask<Activity, Integer, ArrayList<Exam>> {
    @Override
    protected ArrayList<Exam> doInBackground(Activity... params) {
        ArrayList<Exam> al = null;
        try {
            al = ÖsymParser.getParser().getList();
        } catch (Exception e) {
            Toast.makeText(params[0], e.toString(), Toast.LENGTH_SHORT).show();
        }
        return al;
    }
}
