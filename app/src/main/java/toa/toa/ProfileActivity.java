package toa.toa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import toa.toa.Objects.MrUser;

public class ProfileActivity extends AppCompatActivity {

    private MrUser _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _user = getIntent().getParcelableExtra("user");
        TextView name = (TextView) findViewById(R.id.profile_name_txtv);
        TextView bio = (TextView) findViewById(R.id.profle_bio_txtv);
        ImageView pic = (ImageView) findViewById(R.id.profile_person_imgv);
        RecyclerView sports = (RecyclerView) findViewById(R.id.profile_sports_recycler);
        name.setText(MrUser.get_uname());
        bio.setText(MrUser.get_bio());

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
