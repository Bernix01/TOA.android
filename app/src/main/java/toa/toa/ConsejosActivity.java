package toa.toa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrConsejo;
import toa.toa.adapters.MetaAdapter;

public class ConsejosActivity extends AppCompatActivity {


    /*    Adaptador del recycler view    */
    private MetaAdapter adapter;

    /*    Instancia global del recycler view     */
    private SuperRecyclerView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lista = (SuperRecyclerView) findViewById(R.id.list);


        lista.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Cargar datos en el adaptador
        cargarAdaptador();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void cargarAdaptador() {
        // Petición GET

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.mundotoa.co/api/obtener_consejo.php", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                procesarRespuesta(response);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("cargarAdaptador error", responseString);
                Log.e("cargarAdaptador error", throwable.getMessage());
            }
        });
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    JSONArray mensaje = response.getJSONArray("consejo");
                    // Parsear con Gson
                    ArrayList<MrConsejo> consejos = new ArrayList<>();
                    for (int i = 0; i < mensaje.length(); i++) {
                        JSONObject consejo = mensaje.getJSONObject(i);
                        consejos.add(new MrConsejo(consejo.getString("idConsejo"),
                                consejo.getString("titulo"),
                                consejo.getString("descripcion"),
                                consejo.getString("fechaLim"),
                                consejo.getString("categoria"),
                                consejo.getString("descripcion")));
                    }

                    // Inicializar adaptador
                    adapter = new MetaAdapter(consejos, getApplicationContext());
                    // Setear adaptador a la lista
                    lista.setAdapter(adapter);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            getApplicationContext(),
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
