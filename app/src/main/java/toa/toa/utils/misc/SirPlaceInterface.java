/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrPlace;

/**
 * Created by Guillermo on 7/22/2015.
 */
public interface SirPlaceInterface {
    void gotIt(ArrayList<MrPlace> places);

    void failure(String error);

}
