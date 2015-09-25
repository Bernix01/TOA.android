/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrCommunity;

/**
 * Created by Guillermo on 7/19/2015.
 */
interface SirSportsListInterface {

    /**
     * @param comunities
     */
    void goIt(ArrayList<MrCommunity> comunities);

    /**
     * @param error
     */
    void failure(String error);

    void gotString(ArrayList<String> list);

}
