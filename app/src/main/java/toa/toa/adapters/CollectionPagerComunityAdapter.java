/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import toa.toa.Objects.MrSport;
import toa.toa.fragments.CommunitiesFragment;
import toa.toa.fragments.EventsFragment;
import toa.toa.fragments.MembersFragment;
import toa.toa.fragments.PlacesFragment;

/**
 * Created by Junior on 11/07/2015.
 */
public class CollectionPagerComunityAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Comunidades", "bllablabla", "balblalbalba", ":V"};
    private MrSport com;

    public CollectionPagerComunityAdapter(FragmentManager fm, MrSport com) {
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
                CommunitiesFragment coms = new CommunitiesFragment();
                Bundle bundleC = new Bundle();
                bundleC.putParcelable("sport", com);
                coms.setArguments(bundleC);
                return coms;
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