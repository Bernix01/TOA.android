/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.utils.TOA;

import android.util.Log;

import java.util.ArrayList;

import toa.toa.Objects.MrUser;

/**
 * Created by programador on 7/20/15.
 */
public class SirFriendsRetriever implements SirFriendsInterface {
    @Override
    public void goIt(ArrayList<MrUser> friends) {

    }

    @Override
    public void failure(String error) {
        Log.e("SirFriendsRetrieverError", error);
    }
}
