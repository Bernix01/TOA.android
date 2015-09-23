/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.CollectionPagerAdapter;
import toa.toa.utils.NotificationsHandlerT;
import toa.toa.utils.SirHandler;

public class MainActivity extends AppCompatActivity {
    private static Boolean isVisible = false;
    private MrUser __user = new MrUser();
    private String SENDER_ID = "324550711569";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "toa.app";
    private TabLayout tabLayout;
    private String HubListenConnectionString = "Endpoint=sb://toa-notifiy.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=mUFec72yDO6aiOTSkxR3Kkyo+mHg/BVnb06G3D1f5LM=";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                    Log.i("Registered Successfully", "RegId : " +
                            hub.register(regid).getRegistrationId());
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SirHandler.initialize(getApplicationContext());
        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("appData", MODE_PRIVATE);
        int firstTime = userDetails.getInt("firstTme", 1);
        __user = SirHandler.getCurrentUser(getApplicationContext());
        if (firstTime != 0) {
            SharedPreferences.Editor editor = userDetails.edit();
            editor.putInt("firstTme", 0);
            editor.apply();
            Intent firstVisit = new Intent(getApplicationContext(), FirstVisit.class);
            firstVisit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(firstVisit);
            finish();
        } else if (__user.get_id() == -1) {
            Intent Splash = new Intent(getApplicationContext(), Splash_Activity.class);
            Splash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(Splash);
            finish();
        }
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        CollectionPagerAdapter adapter = new CollectionPagerAdapter(getSupportFragmentManager(), __user.get_id());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setIcon(adapter.iconsTOA[0]);
        tabLayout.getTabAt(1).setIcon(adapter.iconsTOA[1]);
        tabLayout.getTabAt(2).setIcon(adapter.iconsTOA[2]);
        NotificationsHandlerT.mainActivity = this;
        NotificationsManager.handleNotifications(this, SENDER_ID, NotificationsHandlerT.class);
        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(HubName, HubListenConnectionString, this);
        registerWithNotificationHubs();
        final TextView name_txtv = (TextView) findViewById(R.id.main_ui_name_txtv);
        final ImageView pimage_imgv = (ImageView) findViewById(R.id.main_ui_pimage_imv);
        name_txtv.setText(__user.get_uname());
        name_txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
        pimage_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
        if (!__user.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(__user.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
        }
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