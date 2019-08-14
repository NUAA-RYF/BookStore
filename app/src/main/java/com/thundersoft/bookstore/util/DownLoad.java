package com.thundersoft.bookstore.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class DownLoad extends AsyncTask<Integer,Integer, Boolean> {

    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
