/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrPlace;
import toa.toa.R;
import toa.toa.adapters.PlacesAdapter;
import toa.toa.utils.TOA.SirHandler;
import toa.toa.utils.TOA.SirPlacesRetriever;

public class PlacesFragment extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "sport";
    private MrComunity com;

    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = this.getArguments();
            com = bundle.getParcelable("sport");
        } else {
            com = savedInstanceState.getParcelable("sport");
        }
        // Inflate the layout for this fragment
        View holder = inflater.inflate(R.layout.fragment_events, container, false);
        final SuperRecyclerView recyclerView = (SuperRecyclerView) holder.findViewById(R.id.events_srecyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        SirHandler handler = new SirHandler(getActivity().getApplicationContext());
        handler.getPlaces(com, new SirPlacesRetriever() {
            @Override
            public void gotIt(ArrayList<MrPlace> places) {
                recyclerView.setAdapter(new PlacesAdapter(places, getActivity().getApplicationContext()));
            }

            @Override
            public void failure(String err) {
                Log.e("errorEvent", err);
            }
        });
        return holder;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("sport", com);
    }
}