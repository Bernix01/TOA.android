package toa.toa.fragments;

import android.app.Fragment;
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

import toa.toa.Objects.MrComunity;
import toa.toa.R;
import toa.toa.adapters.ComunityAdapter;
import toa.toa.utils.RestApi;

/**
 * Created by Junior on 18/06/2015.
 */
public class ComunityFragment extends Fragment {
    ArrayList<MrComunity> mrComunityArrayList = new ArrayList<MrComunity>();

    private SuperRecyclerView recyclerComunities;

    public ComunityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.comunity_layout, container, false);
        recyclerComunities = (SuperRecyclerView) root.findViewById(R.id.my_recycler_comunity_view);
        //recyclerComunities.setHasFixedSize(true);
        recyclerComunities.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerComunities.setItemAnimator(new DefaultItemAnimator());
        getData();
        return root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData() {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Sport) return n.name, n.icnurl, n.bgurl");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = data.length();
                    for (int i = 0; i < datos; i++)
                        mrComunityArrayList.add(new MrComunity(data.getJSONObject(i).getJSONArray("row").getString(0)));

                    recyclerComunities.setAdapter(new ComunityAdapter(mrComunityArrayList, R.layout.comunity_row));
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
