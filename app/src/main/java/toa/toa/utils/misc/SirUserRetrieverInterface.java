/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import toa.toa.Objects.MrUser;

/**
 * Created by Guillermo on 7/18/2015.
 */
public interface SirUserRetrieverInterface {

    void goIt(MrUser user);

    void failure(String error);
}
