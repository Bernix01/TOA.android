package toa.toa.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import toa.toa.Objects.MrComunity;
import toa.toa.R;
import toa.toa.fragments.EventsFragment;
import toa.toa.fragments.MembersFragment;
import toa.toa.fragments.NoticiasFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerComunityAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private final int[] iconsTOA = {R.drawable.comunidades_white, R.drawable.ubication_white, R.drawable.calendario_white, R.drawable.shop_white};
    private String[] titles = {"Comunidades", "bllablabla", "balblalbalba", ":V"};
    private MrComunity com;

    public CollectionPagerComunityAdapter(FragmentManager fm, MrComunity com) {
        super(fm);
        this.com = com;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int i) {
        switch (i) {
            case 0:
                MembersFragment fr = new MembersFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("sport", com);
                fr.setArguments(bundle);
                return fr;
            case 1:
                EventsFragment events = new EventsFragment();
                Bundle bundleE = new Bundle();
                bundleE.putParcelable("sport", com);
                events.setArguments(bundleE);
                return events;
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