/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.utils.misc;

import java.util.ArrayList;

import toa.toa.Objects.MrEvent;

/**
 * Created by programador on 7/23/15.
 */
public interface SirEventsInterface {
    void gotIt(ArrayList<MrEvent> events);

    void failure(String err);
}
