package toa.toa.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import toa.toa.R;


public class SecondSlide extends Fragment {

    private int resID = R.drawable.intro2;
    private int resImgCenter = R.drawable.introicono2;
    private String msg;
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
    public static SecondSlide newInstance(int ResID, int resImgCenter, String msg) {
        return new SecondSlide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first_slide, container, false);
        ImageView imv = (ImageView) root.findViewById(R.id.imv_intro);
        ImageView imgCenter = (ImageView) root.findViewById(R.id.ivCenter);
        TextView menssage = (TextView) root.findViewById(R.id.tvMsg);
        imv.setImageResource(resID);
        imgCenter.setImageResource(resImgCenter);
        menssage.setText(getResources().getString(R.string.msg_second_slider));
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
