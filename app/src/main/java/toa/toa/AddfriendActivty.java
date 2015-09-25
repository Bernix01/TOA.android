/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import toa.toa.Objects.MrUser;
import toa.toa.adapters.FriendsAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirFriendsRetriever;

public class AddfriendActivty extends AppCompatActivity {

    private SuperRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend_activty);
        setSupportActionBar((Toolbar) findViewById(R.id.my_awesome_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (SuperRecyclerView) findViewById(R.id.search_friend_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addfriend_activty, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) AddfriendActivty.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(AddfriendActivty.this.getComponentName()));
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("search", "searching for: " + query);
            //use the query to search your data somehow
            SirHandler.initialize(getApplicationContext());
            SirHandler.searchPeople(query, new SirFriendsRetriever() {
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
