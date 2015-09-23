package toa.toa.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import toa.toa.R;
import toa.toa.fragments.ComunityFragment;
import toa.toa.fragments.NoticiasFragment;
import toa.toa.fragments.NutricionFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    public final int[] iconsTOA = {R.drawable.triatlontab, R.drawable.nutriciontab, R.drawable.noticiastab};
    private String[] titles = {"Deportes", "Nutrición", "Noticias"};
    private int id;

    public CollectionPagerAdapter(FragmentManager fm, int id) {
        super(fm);
        Log.e("id", id + "");
        this.id = id;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        switch (i) {
            case 0:
                ComunityFragment fr = new ComunityFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                fr.setArguments(bundle);
                return fr;
            case 1:
                NutricionFragment frn = new NutricionFragment();
                Bundle bundlen = new Bundle();
                bundlen.putInt("id", id);
                frn.setArguments(bundlen);
                return frn;
            case 2:
                return new NoticiasFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

}