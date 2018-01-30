package com.emfpoll.emfpoll.tasks;

import android.os.AsyncTask;

import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.sql.ResultSet;

/**
 * Created by SchnieperN on 30.01.2018.
 */

public class CreateSurveyTask extends AsyncTask<Survey, Void, Integer> {

    @Override
    protected Integer doInBackground(Survey... surveys) {
        return WrkDB.getInstance().createSurvey(surveys[0]);
    }

}
