package toa.toa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import toa.toa.R;
import toa.toa.fragments.ConsejosNutricionalesFragment;
import toa.toa.utils.ConsejosNutricionales.Constantes;

public class ConsejosNutricionalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creación del fragmento principal
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ConsejosNutricionalesFragment(), "CNFragment")
                    .commit();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.CODIGO_DETALLE || requestCode == 3) {
            if (resultCode == RESULT_OK || resultCode == 203) {
                ConsejosNutricionalesFragment fragment = (ConsejosNutricionalesFragment) getSupportFragmentManager().
                        findFragmentByTag("CNFragment");
                fragment.cargarAdaptador();
            }
        }
    }
}
