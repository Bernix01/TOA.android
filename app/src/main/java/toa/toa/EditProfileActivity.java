/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.utils.TOA.SirHandler;

public class EditProfileActivity extends AppCompatActivity {

    ImageView pimage_imgv;
    private TextView username;
    private EditText name, bio, age, email;
    private Spinner sex;
    private MrUser _cuser;
    private SirHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.cnt);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = (TextView) findViewById(R.id.editProf_uname_txtv);
        name = (EditText) findViewById(R.id.editProf_name_etxtv);
        bio = (EditText) findViewById(R.id.editProf_bio_etxt);
        pimage_imgv = (ImageView) findViewById(R.id.editProf_pimage_imv);
        age = (EditText) findViewById(R.id.editProf_age_etxt);
        email = (EditText) findViewById(R.id.editProf_email_etxt);
        sex = (Spinner) findViewById(R.id.editProf_sex_spinner);
        handler = new SirHandler(getApplicationContext());
        _cuser = handler.getCurrentUser();
        username.setText(_cuser.get_name());
        name.setText(_cuser.get_uname());
        bio.setText(_cuser.get_bio());
        email.setText(_cuser.get_email());
        if (!_cuser.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(_cuser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
        }
        //age.setText(MrUser.get_age());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sex.setAdapter(adapter);
        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        _cuser.set_gender(-1);
                        break;
                    default:
                        _cuser.set_gender(position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editprof_done) {
            do_the_thing();
            handler.updateUserAsync(_cuser);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void do_the_thing() {
        if (!age.getText().toString().isEmpty())
        _cuser.set_age(Integer.parseInt(age.getText().toString()));
        _cuser.set_bio(bio.getText().toString());
        _cuser.set_name(name.getText().toString());
        _cuser.set_email(email.getText().toString());
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
}
