package toa.toa.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import toa.toa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NutricionFragment extends android.support.v4.app.Fragment {


    public NutricionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutricion, container, false);
    }


}
