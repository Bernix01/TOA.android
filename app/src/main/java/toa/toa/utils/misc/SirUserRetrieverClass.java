/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import android.util.Log;

import toa.toa.Objects.MrUser;

/**
 * Created by Guillermo on 7/18/2015.
 */
public class SirUserRetrieverClass implements SirUserRetrieverInterface {
    @Override
    public void goIt(MrUser user) {

    }

    @Override
    public void failure(String error) {
        Log.e("UsrRetErr", error);
    }
}
