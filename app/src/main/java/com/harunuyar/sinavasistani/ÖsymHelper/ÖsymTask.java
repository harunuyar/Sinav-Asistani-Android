package com.harunuyar.sinavasistani.ÖsymHelper;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Harun on 24.01.2017.
 */

public class ÖsymTask extends AsyncTask<Activity, Integer, ArrayList<Exam>> {
    @Override
    protected ArrayList<Exam> doInBackground(Activity... params) {
        ArrayList<Exam> al = null;

        try {
            al = ÖsymParser.getParser().getList();
        } catch (Exception e) { }

        return al;
    }
}
