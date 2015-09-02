/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrUser;
import toa.toa.ProfileActivity;
import toa.toa.R;
import toa.toa.adapters.CollectionPagerComunityAdapter;
import toa.toa.utils.SirHandler;

public class ComunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfit);
        final MrComunity com = getIntent().getParcelableExtra("sport");
        final TextView sportName = (TextView) findViewById(R.id.sport_name_txtv);
        final ImageView sportImage = (ImageView) findViewById(R.id.sport_imgv);
        if (!com.getComunityImg().isEmpty())
            Picasso.with(getApplicationContext()).load(com.getComunityImg()).into(sportImage);
        sportName.setText(com.getComunityName());
        final TextView name_txtv = (TextView) findViewById(R.id.main_ui_name_txtv);
        final ImageView pimage_imgv = (ImageView) findViewById(R.id.main_ui_pimage_imv);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.crossfitactivityLayout);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SirHandler handler = new SirHandler(getApplicationContext());
        final MrUser currentUser = handler.getCurrentUser();
        ViewPager pager = (ViewPager) findViewById(R.id.sportPager);
        pager.setAdapter(new CollectionPagerComunityAdapter(getSupportFragmentManager(), com));
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.sportTabs);
        tabs.setViewPager(pager);
        name_txtv.setText(currentUser.get_uname());
        name_txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("user", handler.getCurrentUser());
                startActivity(i);
            }
        });
        pimage_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("user", handler.getCurrentUser());
                startActivity(i);
            }
        });
        if (!currentUser.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(currentUser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
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
