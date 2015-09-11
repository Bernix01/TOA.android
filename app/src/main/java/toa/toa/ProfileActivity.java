/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa;

import android.content.Intent;
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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrUser;
import toa.toa.adapters.ProfileSportsAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SimpleCallbackClass;
import toa.toa.utils.misc.SirSportsListRetriever;

public class ProfileActivity extends AppCompatActivity {
    ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MrUser _user = (SirHandler.getCurrentUser(getApplicationContext()));
        TextView name = (TextView) findViewById(R.id.profile_name_txtv);
        TextView bio = (TextView) findViewById(R.id.profle_bio_txtv);
        ImageView pic = (ImageView) findViewById(R.id.profile_person_imgv);
        bg = (ImageView) findViewById(R.id.profile_bg_imgv);
        final RecyclerView sportsrecycler = (RecyclerView) findViewById(R.id.profile_sports_recycler);
        sportsrecycler.setHasFixedSize(true);
        if (!_user.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(_user.get_pimage()).transform(new CropCircleTransformation()).into(pic);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pic);
        }
        name.setText(_user.get_uname());
        bio.setText(_user.get_bio());
        LinearLayout friendsIcn = (LinearLayout) findViewById(R.id.friendv_cnt);
        LinearLayout agendaIcn = (LinearLayout) findViewById(R.id.agenda_cnt);
        TextView editSports = (TextView) findViewById(R.id.profile_edit_coms);
        editSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FirstTime.class));
            }
        });
        friendsIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FriendsActivity.class));
            }
        });
        agendaIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AgendaActivity.class));
            }
        });
        Picasso.with(getApplicationContext()).load("http://www.resortvillarosa.it/img/top/sport1.jpg").fit().centerCrop().transform(new BlurTransformation(getApplicationContext(), 15)).into(bg);
        SirHandler handler = new SirHandler(getApplicationContext());
        SirHandler.getUserSports(_user, new SirSportsListRetriever() {
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

    @Override
    protected void onPause() {
        super.onPause();
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
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(intent);
                return true;
            case R.id.prof_action_logout:
                SirHandler.logout(new SimpleCallbackClass() {
                    @Override
                    public void goIt() {
                        Intent i = new Intent(getApplicationContext(), Splash_Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

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
