package toa.toa.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrNutrition;
import toa.toa.R;
import toa.toa.adapters.NutritionAdapter;
import toa.toa.utils.RestApi;

/**
 * A simple {@link Fragment} subclass.
 */
public class NutricionFragment extends android.support.v4.app.Fragment {
    ArrayList<MrNutrition> mrNutritionArrayList = new ArrayList<MrNutrition>();
    private int id;
    private SuperRecyclerView recyclerNutrition;

    public NutricionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        id = bundle.getInt("id", -1);
        View root = inflater.inflate(R.layout.comunity_layout, container, false);
        recyclerNutrition = (SuperRecyclerView) root.findViewById(R.id.my_recycler_comunity_view);
        recyclerNutrition.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Context contexto = getActivity().getApplicationContext();
        getData(contexto, id);

        return root;
    }

    private void getData(final Context contexto, int id) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        Log.e("idgetdata", id + "");
        try {
            subcmd.put("statement", "MATCH (a:user)-[r:Likes]-(n:Sport) WHERE id(a)=" + id + " return n.name, n.icnurl, n.bgurl");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = data.length();
                    for (int i = 0; i < datos; i++)
                        mrNutritionArrayList.add(new MrNutrition(data.getJSONObject(i).getJSONArray("row").getString(0), data.getJSONObject(i).getJSONArray("row").getString(1), data.getJSONObject(i).getJSONArray("row").getString(2)));


                    recyclerNutrition.setAdapter(new NutritionAdapter(mrNutritionArrayList, R.layout.nutrition_row, contexto));
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });

    }


}
