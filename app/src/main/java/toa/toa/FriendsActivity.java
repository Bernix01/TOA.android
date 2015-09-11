/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import toa.toa.Objects.MrUser;
import toa.toa.adapters.FriendsAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirFriendsRetriever;


public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setSupportActionBar((Toolbar) findViewById(R.id.my_awesome_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friends_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SirHandler handler = new SirHandler(getApplicationContext());
        handler.getUserFriends(SirHandler.getCurrentUser(getApplicationContext()), new SirFriendsRetriever() {
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
        if (id == R.id.action_addFriend) {
            startActivity(new Intent(getApplicationContext(), AddfriendActivty.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
