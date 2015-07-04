package toa.toa.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.R;
import toa.toa.adapters.ComunityAdapter;

/**
 * Created by Junior on 18/06/2015.
 */
public class ComunityFragment extends Fragment {
    public ComunityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.comunity_layout, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<MrComunity> mrComunityArrayList = new ArrayList<MrComunity>();

        MrComunity comunities = new MrComunity();
        comunities.setComunityName("Ciclismo");

        MrComunity comunities2 = new MrComunity();
        comunities.setComunityName("Ciclismo");

        MrComunity comunities3 = new MrComunity();
        comunities.setComunityName("Ciclismo");

        MrComunity comunities4 = new MrComunity();
        comunities.setComunityName("Ciclismo");

        mrComunityArrayList.add(comunities);
        mrComunityArrayList.add(comunities2);
        mrComunityArrayList.add(comunities3);
        mrComunityArrayList.add(comunities4);

        RecyclerView recyclerComunities = (RecyclerView) getActivity().findViewById(R.id.my_recycler_comunity_view);
        recyclerComunities.setHasFixedSize(true);

        recyclerComunities.setAdapter(new ComunityAdapter(mrComunityArrayList, R.layout.comunity_row));
        recyclerComunities.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerComunities.setItemAnimator(new DefaultItemAnimator());


    }
}
