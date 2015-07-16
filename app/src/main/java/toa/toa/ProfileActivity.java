package toa.toa;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;

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
        RecyclerView sports = (RecyclerView) findViewById(R.id.profile_sports_recycler);
        if (!MrUser.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(MrUser.get_pimage()).transform(new CropCircleTransformation()).into(pic);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pic);
        }
        name.setText(MrUser.get_uname());
        bio.setText(MrUser.get_bio());

        Picasso.with(getApplicationContext()).load("http://www.resortvillarosa.it/img/top/sport1.jpg").fit().centerCrop().transform(new BlurTransformation(getApplicationContext(), 15)).into(bg);
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
