/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import toa.toa.Objects.MrCommunity;
import toa.toa.R;
import toa.toa.fragments.EventsFragment;
import toa.toa.fragments.MembersFragment;
import toa.toa.fragments.NoticiasFragment;
import toa.toa.fragments.PlacesFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerComunityAdapter extends FragmentStatePagerAdapter {

    private final int[] iconsTOA = {R.drawable.comunidades_white, R.drawable.ubication_white, R.drawable.calendario_white, R.drawable.shop_white};
    private String[] titles = {"Comunidades", "bllablabla", "balblalbalba", ":V"};
    private MrCommunity com;

    public CollectionPagerComunityAdapter(FragmentManager fm, MrCommunity com) {
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
                return new NoticiasFragment();
            case 2:
                EventsFragment events = new EventsFragment();
                Bundle bundleE = new Bundle();
                bundleE.putParcelable("sport", com);
                events.setArguments(bundleE);
                return events;
            case 3:
                PlacesFragment places = new PlacesFragment();
                Bundle bundleP = new Bundle();
                bundleP.putParcelable("sport", com);
                places.setArguments(bundleP);
                return places;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }


}