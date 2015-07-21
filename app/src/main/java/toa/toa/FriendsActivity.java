/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import toa.toa.Objects.MrUser;
import toa.toa.adapters.FriendsAdapter;
import toa.toa.utils.TOA.SirFriendsRetriever;
import toa.toa.utils.TOA.SirHandler;


public class FriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friends_recyclerview);
        if (Build.VERSION.SDK_INT > 19) {
            recyclerView.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SirHandler handler = new SirHandler(getApplicationContext());
        handler.getUserFriends(handler.getCurrentUser(), new SirFriendsRetriever() {
            @Override
            public void goIt(ArrayList<MrUser> friends) {
                recyclerView.setAdapter(new FriendsAdapter(friends, getApplicationContext()));
            }

            @Override
            public void failure(String error) {
                super.failure(error);
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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
