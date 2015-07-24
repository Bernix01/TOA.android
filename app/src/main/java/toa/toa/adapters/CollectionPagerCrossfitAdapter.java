package toa.toa.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

import toa.toa.R;
import toa.toa.fragments.MembersFragment;
import toa.toa.fragments.NoticiasFragment;
import toa.toa.fragments.NutricionFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerCrossfitAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private final int[] iconsTOA = {R.drawable.comunidades_white, R.drawable.ubication_white, R.drawable.calendario_white, R.drawable.shop_white};
    private String[] titles = {"Comunidades", "bllablabla", "balblalbalba", ":V"};
    private int id;

    public CollectionPagerCrossfitAdapter(FragmentManager fm, int id) {
        super(fm);
        Log.e("id", id + "");
        this.id = id;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        switch (i) {
            case 0:
                MembersFragment fr = new MembersFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                fr.setArguments(bundle);
                return fr;
            case 1:
                return new NutricionFragment();
            case 2:
                return new NoticiasFragment();
            case 3:
                return new NoticiasFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public int getPageIconResId(int position) {
        return iconsTOA[position];
    }
}