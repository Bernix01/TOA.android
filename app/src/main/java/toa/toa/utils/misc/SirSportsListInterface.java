/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;

/**
 * Created by Guillermo on 7/19/2015.
 */
public interface SirSportsListInterface {

    /**
     * @param comunities
     */
    void goIt(ArrayList<MrComunity> comunities);

    /**
     * @param error
     */
    void failure(String error);


}
