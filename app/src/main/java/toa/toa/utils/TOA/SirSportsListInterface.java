/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.TOA;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;

/**
 * Created by Guillermo on 7/19/2015.
 */
public interface SirSportsListInterface {
    void goIt(ArrayList<MrComunity> sports);

    void failure(String error);
}
