package toa.toa;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import toa.toa.fragments.FirstSlide;
import toa.toa.fragments.SecondSlide;
import toa.toa.fragments.ThirdSlide;


public class FirstVisit extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(new FirstSlide());
        addSlide(AppIntroFragment.newInstance("Bienvenido a TOA",
                getResources().getString(R.string.msg_first_slider),
                R.drawable.introicono1,
                ContextCompat.getColor(getApplicationContext(), R.color.my_awesome_color)));
        addSlide(new SecondSlide());
        addSlide(new ThirdSlide());
        setDepthAnimation();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        return 0;
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(), Splash_Activity.class));
        finish();
    }
}
