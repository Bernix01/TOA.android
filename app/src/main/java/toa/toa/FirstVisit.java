package toa.toa;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.paolorotolo.appintro.AppIntro;

import toa.toa.fragments.FirstSlide;
import toa.toa.fragments.SecondSlide;
import toa.toa.fragments.ThirdSlide;


public class FirstVisit extends AppIntro {

    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {
        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new FirstSlide(), getApplicationContext());
        addSlide(new SecondSlide(), getApplicationContext());
        addSlide(new ThirdSlide(), getApplicationContext());

        if (Build.VERSION.SDK_INT > 19) {
            LinearLayout viewa = (LinearLayout) findViewById(R.id.bottom);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewa.getLayoutParams();
            layoutParams.height = getNavigationBarHeight() + dpToPx(64);
            Log.e("nh", layoutParams.height + "");
            viewa.setPadding(0, 0, 0, getNavigationBarHeight());
            viewa.setLayoutParams(layoutParams);
            viewa.setMinimumHeight(getNavigationBarHeight() + viewa.getHeight());
            Log.e("h", viewa.getHeight() + "");
        }
        // You can override bar/separator color if you want.
        setBarColor(Color.parseColor("#05A8A8A8"));
        setSeparatorColor(Color.parseColor("#00000000"));

        // You can also hide Skip button
        showSkipButton(true);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        startActivity(new Intent(getApplicationContext(), Splash_Activity.class));
        finish();
    }


    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(), Splash_Activity.class));
        finish();
    }
}
