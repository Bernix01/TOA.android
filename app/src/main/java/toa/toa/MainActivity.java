/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.CollectionPagerAdapter;
import toa.toa.utils.NotificationsHandlerT;
import toa.toa.utils.RestApi;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirSportsListRetriever;

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
    private void registerWithNotificationHubs(final String[] tags) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                    Log.i("Registered Successfully", "RegId : " +
                            hub.register(regid, tags).getRegistrationId());
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
            return;
        } else if (__user.get_id() == -1) {
            Intent Splash = new Intent(getApplicationContext(), RegisterActivity.class);
            Splash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(Splash);
            finish();
            return;
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
        obtenerConsejoTOA();
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("postition scrolled", position + " " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("page selected", position + " ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("state", String.valueOf(state));
            }
        });
        final TextView name_txtv = (TextView) findViewById(R.id.main_ui_name_txtv);
        final ImageView pimage_imgv = (ImageView) findViewById(R.id.main_ui_pimage_imv);
        if (!__user.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(__user.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
        }
        name_txtv.setText(__user.get_uname().split(" ")[0]);
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


        NotificationsHandlerT.mainActivity = this;
        NotificationsManager.handleNotifications(this, SENDER_ID, NotificationsHandlerT.class);
        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(HubName, HubListenConnectionString, this);
        SirHandler.getUserSports(__user, new SirSportsListRetriever() {
            @Override
            public void gotString(ArrayList<String> list) {
                String[] a = new String[list.size()];
                registerWithNotificationHubs(list.toArray(a));
            }
        });
    }

    private void obtenerConsejoTOA() {
        RestApi.get("/node/58", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    TextView txt = (TextView) findViewById(R.id.consejo_txt);
                    TextView aut = (TextView) findViewById(R.id.consejo_autor);
                    txt.setText(data.getString("cnst"));
                    aut.setText(data.getString("cnstxt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", statusCode + throwable.getLocalizedMessage());
                Log.e("error2", responseString);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}