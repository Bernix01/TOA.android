package toa.toa;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by gbern on 10/17/2015.
 */
public class TOA extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}