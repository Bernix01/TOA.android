package toa.toa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.Objects.MrUser;
import toa.toa.fragments.ComunityFragment;
import toa.toa.fragments.NoticiasFragment;
import toa.toa.fragments.NutricionFragment;
import toa.toa.utils.RestApi;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
    private static int __n_id;
    private Fragment[] fragmentos = new Fragment[]
            {
            new ComunityFragment(),
            new NutricionFragment(),
            new NoticiasFragment()
            };

    private MrUser __user = new MrUser();
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);


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
        final Activity ac = this;
        setContentView(R.layout.activity_main);


        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("u_data", MODE_PRIVATE);
        setId(userDetails.getInt("n_id", -1));
        if (getId() == -1) {
            Intent firstVisit = new Intent(getApplicationContext(), FirstVisit.class);
            firstVisit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(firstVisit);
            finish();
        }

        ActionBar actionBarToaPrincipal = getActionBar();
        if (actionBarToaPrincipal != null) {
            actionBarToaPrincipal.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBarToaPrincipal.addTab(actionBarToaPrincipal.newTab().setIcon(R.drawable.logo).setTabListener(this));
            actionBarToaPrincipal.addTab(actionBarToaPrincipal.newTab().setIcon(R.drawable.logo).setTabListener(this));
            actionBarToaPrincipal.addTab(actionBarToaPrincipal.newTab().setIcon(R.drawable.logo).setTabListener(this));
        }

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
                   /* headerResult = new AccountHeaderBuilder()
                            .withActivity(ac)
                            .withHeaderBackground(new ColorDrawable(getResources().getColor(R.color.primary_dark))).addProfiles(
                                    profile3
                            )
                            .withSavedInstance(savedInstanceState)
                            .build();
                    result = new DrawerBuilder()
                            .withActivity(ac)
                            .withToolbar(toll)
                            .withAccountHeader(headerResult)
                            .withFullscreen(true).addDrawerItems(
                                    new PrimaryDrawerItem().withName("Inicio").withIcon(R.mipmap.ic_launcher).withIdentifier(1)
                            ).withSavedInstance(savedInstanceState).build();
                    if (savedInstanceState == null) {
                        // set the selection to the item with the identifier 10
                        result.setSelectionByIdentifier(1, false);

                        //set the active profile
                        headerResult.setActiveProfile(profile3);
                    }*/
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
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        for (Fragment fragment : fragmentos) {
            fragmentTransaction.add(R.id.mainActivityLayout, fragment).hide(fragment);
        }

        fragmentTransaction.show(fragmentos[0]).commit();


        //setTabs();

    }

   /* private void setTabs() {

    }*/


    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        for (Fragment fragment : fragmentos) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.show(fragmentos[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}