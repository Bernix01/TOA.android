package toa.toa.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import toa.toa.R;
import toa.toa.fragments.ComunityFragment;
import toa.toa.fragments.NoticiasFragment;
import toa.toa.fragments.NutricionFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private final int[] iconsTOA = {R.drawable.triatlontab, R.drawable.nutriciontab, R.drawable.noticiastab};
    private String[] titles = {"Deportes", "Nutrición", "Noticias"};

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ComunityFragment();
            case 1:
                return new NutricionFragment();
            case 2:
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