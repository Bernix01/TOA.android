package toa.toa.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.FirstVisit;
import toa.toa.Objects.MrUser;
import toa.toa.ProfileActivity;
import toa.toa.R;
import toa.toa.adapters.CollectionPagerCrossfitAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.SirUserRetrieverUserRetrieverClass;

public class CrossFitActivity extends AppCompatActivity {
    private static int __n_id;
    private MrUser __user = new MrUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfit);
        final TextView name_txtv = (TextView) findViewById(R.id.main_ui_name_txtv);
        final ImageView pimage_imgv = (ImageView) findViewById(R.id.main_ui_pimage_imv);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.crossfitactivityLayout);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle("");
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
        handler.getUserById(__n_id, new SirUserRetrieverUserRetrieverClass() {
            @Override
            public void goIt(MrUser user) {
                __user = user;
                ViewPager pager = (ViewPager) findViewById(R.id.pagerCrossfit);
                pager.setAdapter(new CollectionPagerCrossfitAdapter(getSupportFragmentManager(), MrUser.get_id()));
                PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabsCrossfit);
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
    }

    private int getId() {
        return __n_id;
    }

    private void setId(int id) {
        __n_id = id;
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
        getMenuInflater().inflate(R.menu.menu_atletismo, menu);
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
