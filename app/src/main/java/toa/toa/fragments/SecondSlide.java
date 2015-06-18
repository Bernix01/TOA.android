package toa.toa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import toa.toa.R;


public class SecondSlide extends Fragment {

    private int resID = R.drawable.intro2;

    public SecondSlide() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ResID image.
     * @return A new instance of fragment FirstSlide.
     */
    public static SecondSlide newInstance(int ResID) {
        SecondSlide fragment = new SecondSlide();
        Bundle args = new Bundle();
        args.putInt("resid", ResID);
        Log.d("argid", "" + ResID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resID = getArguments().getInt("resid");
            Log.d("imgID", "" + resID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first_slide, container, false);
        ImageView imv = (ImageView) root.findViewById(R.id.imv_intro);
        imv.setImageResource(resID);
        Log.d("setted", "" + resID);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
