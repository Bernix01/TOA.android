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

import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrUser;
import toa.toa.R;
import toa.toa.adapters.FriendsAdapter;
import toa.toa.utils.TOA.SirFriendsRetriever;
import toa.toa.utils.TOA.SirHandler;

/**
 * Created by Junior on 18/06/2015.
 */
public class MembersFragment extends android.support.v4.app.Fragment {
    private SuperRecyclerView recyclerComunities;

    public MembersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        MrComunity com = bundle.getParcelable("sport");
        View root = inflater.inflate(R.layout.comunity_layout, container, false);
        recyclerComunities = (SuperRecyclerView) root.findViewById(R.id.my_recycler_comunity_view);
        recyclerComunities.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Context contexto = getActivity().getApplicationContext();
        SirHandler handler = new SirHandler(contexto);
        handler.getSportMembers(com, new SirFriendsRetriever() {
            @Override
            public void goIt(ArrayList<MrUser> friends) {
                recyclerComunities.setAdapter(new FriendsAdapter(friends, contexto));
            }

            @Override
            public void failure(String error) {
                Log.e("SportMembersError", error);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
