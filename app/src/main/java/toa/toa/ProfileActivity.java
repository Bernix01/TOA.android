/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.ProfileSportsAdapter;
import toa.toa.utils.TOA.SimpleCallbackClass;
import toa.toa.utils.TOA.SirHandler;
import toa.toa.utils.TOA.SirSportsListRetriever;

public class ProfileActivity extends AppCompatActivity {
    ImageView bg;
    private MrUser _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout cnt = (RelativeLayout) findViewById(R.id.container);
            cnt.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }

        _user = getIntent().getParcelableExtra("user");
        TextView name = (TextView) findViewById(R.id.profile_name_txtv);
        TextView bio = (TextView) findViewById(R.id.profle_bio_txtv);
        ImageView pic = (ImageView) findViewById(R.id.profile_person_imgv);
        bg = (ImageView) findViewById(R.id.profile_bg_imgv);
        final RecyclerView sportsrecycler = (RecyclerView) findViewById(R.id.profile_sports_recycler);
        sportsrecycler.setHasFixedSize(true);
        if (!MrUser.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(MrUser.get_pimage()).transform(new CropCircleTransformation()).into(pic);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pic);
        }
        name.setText(MrUser.get_uname());
        bio.setText(MrUser.get_bio());
        LinearLayout friendsIcn = (LinearLayout) findViewById(R.id.friendv_cnt);
        friendsIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
            }
        });
        Picasso.with(getApplicationContext()).load("http://www.resortvillarosa.it/img/top/sport1.jpg").fit().centerCrop().transform(new BlurTransformation(getApplicationContext(), 15)).into(bg);
        SirHandler handler = new SirHandler(getApplicationContext());
        handler.getUserSports(_user, new SirSportsListRetriever() {
            @Override
            public void goIt(ArrayList<MrComunity> sports) {
                ProfileSportsAdapter adapter = new ProfileSportsAdapter(sports, getApplicationContext());
                sportsrecycler.setAdapter(adapter);

            }

            @Override
            public void failure(String error) {
                Log.e("profile_sports_error", error);
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
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.prof_action_edit:
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                return true;
            case R.id.prof_action_logout:
                SirHandler handler = new SirHandler(getApplicationContext());
                handler.logout(new SimpleCallbackClass() {
                    @Override
                    public void goIt() {
                        Intent i = new Intent(getApplicationContext(), Splash_Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
