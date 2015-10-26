package toa.toa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.Objects.List;
import toa.toa.Objects.MrSport;
import toa.toa.R;
import toa.toa.adapters.SportsAdapter;
import toa.toa.utils.RestApi;
import toa.toa.utils.UtilidadesExtras;

/**
 * Created by Junior on 18/06/2015.
 */
public class ComunityFragment extends android.support.v4.app.Fragment {
    private List mrCommunityArrayList = new List();
    private SuperRecyclerView recyclerComunities;

    public ComunityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int id = bundle.getInt("id", -1);
        View root = inflater.inflate(R.layout.comunity_layout, container, false);
        recyclerComunities = (SuperRecyclerView) root.findViewById(R.id.list);
        recyclerComunities.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Context contexto = getActivity().getApplicationContext();
        if (savedInstanceState != null) {
            Log.e("restored", "restored coms");
            mrCommunityArrayList = savedInstanceState.getParcelable("data");
            if (UtilidadesExtras.isOnline(contexto))
                recyclerComunities.setAdapter(new SportsAdapter(mrCommunityArrayList, contexto));
        } else {
            getData(contexto, id);
        }
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData(final Context contexto, final int id) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        Log.e("idgetdata", id + "");
        try {
            subcmd.put("statement", "MATCH (a:user)-[r:Likes]-(n:Sport) WHERE id(a)=" + id + " return n.name, n.icnurl, n.bgurl, n.icnurl_alt");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = data.length();
                    for (int i = 0; i < datos; i++)
                        mrCommunityArrayList.add(new MrSport(data.getJSONObject(i).getJSONArray("row").getString(0), data.getJSONObject(i).getJSONArray("row").getString(1), data.getJSONObject(i).getJSONArray("row").getString(2), data.getJSONObject(i).getJSONArray("row").getString(3)));
                    SportsAdapter adapter = new SportsAdapter(mrCommunityArrayList, contexto);
                    recyclerComunities.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", mrCommunityArrayList);
        Log.e("saver", "saved");
    }
}
