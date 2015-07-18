package toa.toa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.CollectionPagerAdapter;
import toa.toa.utils.RestApi;

public class MainActivity extends AppCompatActivity {
    private static int __n_id;
    private MrUser __user = new MrUser();
    private AccountHeader headerResult = null;
    private Drawer result = null;

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
        final TextView name_txtv = (TextView) findViewById(R.id.main_ui_name_txtv);
        final ImageView pimage_imgv = (ImageView) findViewById(R.id.main_ui_pimage_imv);

        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.mainActivityLayout);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
                    MrUser.set_pimage(tryGetString(data, "pimageurl"));
                    ViewPager pager = (ViewPager) findViewById(R.id.pager);
                    pager.setAdapter(new CollectionPagerAdapter(getSupportFragmentManager(), MrUser.get_id()));
                    PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                    tabs.setViewPager(pager);
                    name_txtv.setText(MrUser.get_uname());
                    if (!MrUser.get_pimage().isEmpty()) {
                        Picasso.with(getApplicationContext()).load(MrUser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
                    } else {
                        Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
                    }
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
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}