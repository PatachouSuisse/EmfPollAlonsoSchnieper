package com.emfpoll.emfpoll.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.emfpoll.emfpoll.beans.Vote;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 30.01.2018.
 */

public class VoteTask extends AsyncTask<ArrayList<Vote>, Void, Boolean> {

    @Override
    protected Boolean doInBackground(ArrayList<Vote>... votes) {
        return WrkDB.getInstance().vote(votes[0]);
    }

}
