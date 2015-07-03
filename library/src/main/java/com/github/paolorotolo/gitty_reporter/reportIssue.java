package com.github.paolorotolo.gitty_reporter;

import android.os.AsyncTask;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;

public class reportIssue extends AsyncTask<String, Integer, String> {

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Do something like display a progress bar
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


        final IssueService service = new IssueService(new GitHubClient().setCredentials(user, password));

        Issue issue = new Issue().setTitle(bugTitle).setBody(bugDescription + "\n\n" + deviceInfo);
        try {
            issue = service.createIssue(targetUser, targetRepository, issue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "this string is passed to onPostExecute";
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Do things like hide the progress bar or change a TextView
    }
}