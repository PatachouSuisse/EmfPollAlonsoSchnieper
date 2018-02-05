package com.emfpoll.emfpoll.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.emfpoll.emfpoll.VoteActivity;
import com.emfpoll.emfpoll.beans.Vote;
import com.emfpoll.emfpoll.exceptions.AlreadyVotedException;
import com.emfpoll.emfpoll.wrk.WrkDB;

import java.util.ArrayList;

/**
 * Created by SchnieperN on 30.01.2018.
 */

public class VoteTask extends AsyncTask<ArrayList<Vote>, Void, Boolean> {

    private static final String LOG_TAG = VoteTask.class.getSimpleName();

    @Override
    protected Boolean doInBackground(ArrayList<Vote>... votes) {
        Boolean b = null;
        try {
            b = WrkDB.getInstance().vote(votes[0]);
        } catch (AlreadyVotedException e) {
            Log.w(LOG_TAG, "L'utilisateur a déjà voté !");
        }
        return b;
    }

}
