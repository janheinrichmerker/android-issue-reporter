package com.github.paolorotolo.gitty_reporter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;

public class reportIssue extends AsyncTask<String, Integer, String> {

    Context mContext;
    ProgressDialog progress;

    public reportIssue (Context context){
        mContext = context;
    }

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(mContext, "Please wait",
                "Uploading report on GitHub", true);
    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {
        // get the string from params, which is an array
        String user = params[0];
        String password = params[1];
        String bugTitle = params[2];
        String bugDescription = params[3];
        String deviceInfo = params[4];
        String targetUser = params[5];
        String targetRepository = params[6];
        String extraInfo = params[7];
        String gitToken = params[8];

        IssueService service;

        if (user.equals("")) {
            service = new IssueService(new GitHubClient().setOAuth2Token(gitToken));
        } else {
            service = new IssueService(new GitHubClient().setCredentials(user, password));
        }

        Issue issue = new Issue().setTitle(bugTitle).setBody(bugDescription + "\n\n" + deviceInfo + "\n\nExtra Info: " + extraInfo);
        try {
            issue = service.createIssue(targetUser, targetRepository, issue);
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.equals("ok")) {
            progress.dismiss();
            ((Activity)mContext).finish();
        } else if (result.equals("org.eclipse.egit.github.core.client.RequestException: Bad credentials (401)")){
            progress.dismiss();
            new AlertDialog.Builder(mContext)
                    .setTitle("Unable to send report")
                    .setMessage("Wrong username or password or invalid access token.")
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.ic_mood_bad_black_24dp)
                    .show();
        } else {
            progress.dismiss();
            new AlertDialog.Builder(mContext)
                    .setTitle("Unable to send report")
                    .setMessage("An unexpected error occurred. If the problem persists, contact the app developer.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity)mContext).finish();
                        }
                    })
                    .setIcon(R.drawable.ic_mood_bad_black_24dp)
                    .show();
        }
    }
}