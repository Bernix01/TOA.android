package toa.toa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.Objects.MrUser;
import toa.toa.utils.RestApi;

public class MainActivity extends AppCompatActivity {
    private static int __n_id;
    private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    newText = TextUtils.isEmpty(newText) ? "" : "Query so far: " + newText;
                    //mSearchText.setText(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this,
                            "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
                    return true;
                }
            };
    private MrUser __user = new MrUser();
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        return super.onCreateOptionsMenu(menu);
    }


    public int tryGetInt(JSONObject j, String name) {
        int r = -1;
        try {
            r = j.getInt(name);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public String tryGetString(JSONObject j, String name) {
        String r = "";
        try {
            r = j.getString(name);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    private int getId() {
        return __n_id;
    }

    private void setId(int id) {
        __n_id = id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout comunidades = (LinearLayout) findViewById(R.id.comunidadesDeportivasLayout);
        LinearLayout nutricion = (LinearLayout) findViewById(R.id.nutricionLayout);
        LinearLayout noticias = (LinearLayout) findViewById(R.id.noticiasLayout);
        LinearLayout tienda = (LinearLayout) findViewById(R.id.tiendaLayout);


        comunidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Activity ac = this;
        setContentView(R.layout.activity_main2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        toolbar.getBackground().setAlpha(90);
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("u_data", MODE_PRIVATE);
        setId(userDetails.getInt("n_id", -1));
        if (getId() == -1) {
            Intent firstVisit = new Intent(getApplicationContext(), FirstVisit.class);
            firstVisit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(firstVisit);
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        RestApi.get("/node/" + getId(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject data = new JSONObject();
                try {
                    data = response.getJSONObject("data");
                    MrUser.set_email(tryGetString(data, "email"));
                    MrUser.set_id(tryGetInt(response.getJSONObject("metadata"), "id"));
                    MrUser.set_name(tryGetString(data, "name"));
                    MrUser.set_uname(tryGetString(data, "u_name"));
                    MrUser.set_bio(tryGetString(data, "bio"));
                    MrUser.set_gender(tryGetInt(data, "gender"));
                    final IProfile profile3 = new ProfileDrawerItem().withEmail(MrUser.get_email()).withName(MrUser.get_name());

                    // Create the AccountHeader
                    headerResult = new AccountHeaderBuilder()
                            .withActivity(ac)
                            .withHeaderBackground(new ColorDrawable(getResources().getColor(R.color.primary_dark))).addProfiles(
                                    profile3
                            )
                            .withSavedInstance(savedInstanceState)
                            .build();
                    result = new DrawerBuilder()
                            .withActivity(ac)
                            .withToolbar(toolbar)
                            .withAccountHeader(headerResult)
                            .withFullscreen(true).addDrawerItems(
                                    new PrimaryDrawerItem().withName("Inicio").withIcon(R.mipmap.ic_launcher).withIdentifier(1)
                            ).withSavedInstance(savedInstanceState).build();
                    if (savedInstanceState == null) {
                        // set the selection to the item with the identifier 10
                        result.setSelectionByIdentifier(1, false);

                        //set the active profile
                        headerResult.setActiveProfile(profile3);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please connect to a network", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }


    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}