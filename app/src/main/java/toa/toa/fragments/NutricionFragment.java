package toa.toa.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import toa.toa.R;
import toa.toa.activities.ConsejosNutricionalesActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NutricionFragment extends android.support.v4.app.Fragment {


    public NutricionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nutricion, container, false);
        TextView nutritionActivity;
        nutritionActivity = (TextView) root.findViewById(R.id.expecialistastxtv_nutrition_fragment);
        nutritionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callNutritionActivity = new Intent(getActivity().getApplicationContext(), ConsejosNutricionalesActivity.class);
                getActivity().startActivity(callNutritionActivity);
            }
        });
        return root;
    }


}
