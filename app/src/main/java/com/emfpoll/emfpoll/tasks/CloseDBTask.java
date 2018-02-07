package com.emfpoll.emfpoll.tasks;

import android.os.AsyncTask;

import com.emfpoll.emfpoll.wrk.WrkDB;

/**
 * Created by SchnieperN on 07.02.2018.
 */

public class CloseDBTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        return WrkDB.getInstance().close();
    }

}
