package toa.toa.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import toa.toa.R;


public class MapActivity extends AppCompatActivity {
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_activity);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        if (savedInstanceState == null) {

            displayView(0);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SchoolsFragment();
                break;
            case 1:
                // fragment = new DistrictsFragment();
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();


        } else {

            Log.e("MapActivity", "Error al Crear el Fragment");
        }
    }
}