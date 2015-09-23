/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.R;
import toa.toa.adapters.MetaAdapter;


public class ConsejosNutricionalesFragment extends android.support.v4.app.Fragment {
    /*      Etiqueta de depuracion       */
    private static final String TAG = ConsejosNutricionalesFragment.class.getSimpleName();

    /*    Adaptador del recycler view    */
    private MetaAdapter adapter;

    /*    Instancia global del recycler view     */
    private RecyclerView lista;

    /*    instancia global del administrador     */
    private RecyclerView.LayoutManager lManager;


    // private Gson gson = new Gson();

    public ConsejosNutricionalesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_consejos_nutricionales, container, false);

        lista = (RecyclerView) v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);

        // Cargar datos en el adaptador
        cargarAdaptador();

        // Obtener instancia del FAB


        return v;
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
                    // MrConsejo[] metas = gson.fromJson(mensaje.toString(), MrConsejo[].class);
                    // Inicializar adaptador
                    // adapter = new MetaAdapter(Arrays.asList(metas), getActivity());
                    // Setear adaptador a la lista
                    lista.setAdapter(adapter);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            getActivity(),
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
