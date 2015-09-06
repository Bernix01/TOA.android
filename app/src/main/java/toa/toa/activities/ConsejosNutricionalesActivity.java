package toa.toa.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.ProfileActivity;
import toa.toa.R;
import toa.toa.fragments.ConsejosNutricionalesFragment;
import toa.toa.utils.ConsejosNutricionales.Constantes;
import toa.toa.utils.SirHandler;

public class ConsejosNutricionalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos_nutricionales);

        // Creación del fragmento principal
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ConsejosNutricionalesFragment(), "ConsejosNutricionalesFragment")
                    .commit();
            if (Build.VERSION.SDK_INT > 19) {
                LinearLayout view = (LinearLayout) findViewById(R.id.container);
                view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
            }
            final TextView name_txtv = (TextView) findViewById(R.id.textView8);
            final ImageView pimage_imgv = (ImageView) findViewById(R.id.imageView9);
            final SirHandler handler = new SirHandler(getApplicationContext());
            final MrUser currentUser = SirHandler.getCurrentUser(getApplicationContext());
            name_txtv.setText(currentUser.get_uname());
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
            if (!currentUser.get_pimage().isEmpty()) {
                Picasso.with(getApplicationContext()).load(currentUser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
            } else {
                Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.CODIGO_DETALLE || requestCode == 3) {
            if (resultCode == RESULT_OK || resultCode == 203) {
                ConsejosNutricionalesFragment fragment = (ConsejosNutricionalesFragment) getSupportFragmentManager().
                        findFragmentByTag("ConsejosNutricionalesFragment");
                fragment.cargarAdaptador();
            }
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

}
