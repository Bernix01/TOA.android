/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrUser;

/**
 * Created by programador on 7/20/15.
 */
interface SirFriendsInterface {

    void goIt(ArrayList<MrUser> friends);

    void failure(String error);
}
