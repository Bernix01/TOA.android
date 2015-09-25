/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import toa.toa.Objects.MrCommunity;
import toa.toa.R;
import toa.toa.adapters.CollectionPagerComunityAdapter;

public class ComunityActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comuity);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        final MrCommunity com = getIntent().getParcelableExtra("sport");
        final TextView sportName = (TextView) findViewById(R.id.sport_name_txtv);
        final ImageView sportImage = (ImageView) findViewById(R.id.sport_imgv);
        if (!com.getComunityImg().isEmpty())
            Picasso.with(getApplicationContext()).load(com.getComunityImg()).into(sportImage);
        sportName.setText(com.getComunityName());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        final ImageView hback = (ImageView) findViewById(R.id.himgbackcom);
        if (!com.getComunityBack().isEmpty())
            Picasso.with(getApplicationContext()).load(com.getComunityBack()).fit().centerCrop().transform(new BlurTransformation(getApplicationContext(), 10)).into(hback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager pager = (ViewPager) findViewById(R.id.sportPager);
        pager.setAdapter(new CollectionPagerComunityAdapter(getSupportFragmentManager(), com));
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setIcon(R.drawable.menbers_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.comunidades_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.calendario_white);
        tabLayout.getTabAt(3).setIcon(R.drawable.ubication_white);
        tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
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
