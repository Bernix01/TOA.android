/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.R;
import toa.toa.utils.ConsejosNutricionales.Constantes;

//import com.google.gson.Gson;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    /**
     * Etiqueta de depuración
     */
    private static final String TAG = DetailFragment.class.getSimpleName();

    /*
    Instancias de Views
     */
    private ImageView cabecera;
    private TextView titulo;
    private TextView descripcion;
    private TextView autor;
    private TextView fechaLim;
    private TextView categoria;
    private ImageButton editButton;
    private String extra;
    // private Gson gson = new Gson();

    public DetailFragment() {
    }

    public static DetailFragment createInstance(String idMeta) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.EXTRA_ID, idMeta);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        // Obtención de views
        cabecera = (ImageView) v.findViewById(R.id.cabecera);
        titulo = (TextView) v.findViewById(R.id.titulo);
        descripcion = (TextView) v.findViewById(R.id.descripcion);
        autor = (TextView) v.findViewById(R.id.autor);
        fechaLim = (TextView) v.findViewById(R.id.fecha);
        categoria = (TextView) v.findViewById(R.id.categoria);

        // Obtener extra del intent de envío
        extra = getArguments().getString(Constantes.EXTRA_ID);

        // Cargar datos desde el web service
        cargarDatos();

        return v;
    }

    public void cargarDatos() {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_ID + "?idMeta=" + extra;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constantes.GET_BY_ID + "?idMeta=" + extra, new JsonHttpResponseHandler() {
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
     * Obtiene los datos desde el servidor
     */

    /**
     * Procesa cada uno de los estados posibles de la
     * respuesta enviada desde el servidor
     *
     * @param response Objeto Json
     */
    private void procesarRespuesta(JSONObject response) {

        try {
            // Obtener atributo "mensaje"
            String mensaje = response.getString("estado");

            switch (mensaje) {
                case "1":
                    // Obtener objeto "meta"
                    JSONObject object = response.getJSONObject("meta");

                    //Parsear objeto
                  /* MrConsejo meta = gson.fromJson(object.toString(), MrConsejo.class);

                    // Asignar color del fondo
                    switch (meta.getCategoria()) {
                        case "Natación":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.saludColor));
                            break;
                        case "Crossfit":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.finanzasColor));
                            break;
                        case "Ciclismo":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.espiritualColor));
                            break;
                        case "Running":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.profesionalColor));
                            break;
                        case "Fútbol":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.materialColor));
                            break;
                    }

                    // Seteando valores en los views
                    titulo.setText(meta.getTitulo());
                    descripcion.setText(meta.getDescripcion());
                    autor.setText(meta.getAutor());
                    fechaLim.setText(meta.getFechaLim());
                    categoria.setText(meta.getCategoria());*/
                    break;

                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            getActivity(),
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            getActivity(),
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
