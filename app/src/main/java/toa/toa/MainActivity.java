package toa.toa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.CollectionPagerAdapter;
import toa.toa.utils.SirClass;
import toa.toa.utils.SirHandler;

public class MainActivity extends AppCompatActivity {
    private static int __n_id;
    private MrUser __user = new MrUser();


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
        setSupportActionBar(toolbar);
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("u_data", MODE_PRIVATE);
        setId(userDetails.getInt("n_id", -1));
        if (getId() == -1) {
            Intent firstVisit = new Intent(getApplicationContext(), FirstVisit.class);
            firstVisit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(firstVisit);
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        SirHandler handler = new SirHandler(getApplicationContext());
        handler.getUserById(__n_id, new SirClass() {
            @Override
            public void goIt(MrUser user) {
                __user = user;
                ViewPager pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(new CollectionPagerAdapter(getSupportFragmentManager(), MrUser.get_id()));
                PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                tabs.setViewPager(pager);
                name_txtv.setText(MrUser.get_uname());
                name_txtv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        i.putExtra("user", __user);
                        startActivity(i);
                    }
                });
                if (!MrUser.get_pimage().isEmpty()) {
                    Picasso.with(getApplicationContext()).load(MrUser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
                }
            }

            @Override
            public void failure(String error) {
                Log.e("error", error);
            }
        });
       /* RestApi.get("/node/" + getId(), new RequestParams(), new JsonHttpResponseHandler() {
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
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please connect to a network", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });*/

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
            super.onBackPressed();
    }
}