/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import toa.toa.Objects.MrCommunity;
import toa.toa.Objects.MrSport;
import toa.toa.R;
import toa.toa.adapters.CommunityAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirCommunitiesRetriever;

/**
 * Created by Guillermo Bernal on 23/10/2015.
 */
public class CommunitiesFragment extends android.support.v4.app.Fragment {
    private SuperRecyclerView recyclerComunities;

    public CommunitiesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        MrSport com = bundle.getParcelable("sport");
        View root = inflater.inflate(R.layout.comunity_layout, container, false);
        recyclerComunities = (SuperRecyclerView) root.findViewById(R.id.list);
        recyclerComunities.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Context contexto = getActivity().getApplicationContext();
        SirHandler handler = new SirHandler(contexto);
        handler.getSportCommunities(com, new SirCommunitiesRetriever() {
            @Override
            public void gotIt(ArrayList<MrCommunity> coms) {
                recyclerComunities.setAdapter(new CommunityAdapter(coms, contexto));
            }

            @Override
            public void failure(String error) {
                Log.e("ComsError", error);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
