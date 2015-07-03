package com.github.paolorotolo.gitty_reporter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends GittyReporter {

    @Override
    protected void init() {
        setTargetRepository("paolorotolo", "TestRepo");
        setGitHubCredentials("numix-ood", "n1u2m3i4x5");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
