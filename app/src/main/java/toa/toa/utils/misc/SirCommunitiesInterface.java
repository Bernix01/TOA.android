package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrCommunity;

/**
 * Created by gbern on 10/23/2015.
 */
public interface SirCommunitiesInterface {
    void gotIt(ArrayList<MrCommunity> coms);

    void failure(String err);
}
