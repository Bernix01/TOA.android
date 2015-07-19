package toa.toa.utils;

import java.util.ArrayList;

import toa.toa.Objects.MrSport;

/**
 * Created by Guillermo on 7/19/2015.
 */
public interface SirSportsListInterface {
    void goIt(ArrayList<MrSport> sports);

    void failure(String error);
}
