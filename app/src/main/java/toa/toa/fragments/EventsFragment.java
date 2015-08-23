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
import toa.toa.Objects.MrEvent;
import toa.toa.R;
import toa.toa.adapters.EventsAdapter;
import toa.toa.utils.TOA.SirEventsRetriever;
import toa.toa.utils.TOA.SirHandler;

public class EventsFragment extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "sport";
    private MrComunity com;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        handler.getSportEvents(com.getComunityName(), new SirEventsRetriever() {
            @Override
            public void gotIt(ArrayList<MrEvent> events) {
                recyclerView.setAdapter(new EventsAdapter(events, getActivity().getApplicationContext()));
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
