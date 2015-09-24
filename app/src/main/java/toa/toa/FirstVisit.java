package toa.toa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class FirstVisit extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("",
                getResources().getString(R.string.msg_first_slider),
                R.drawable.introicono1,
                ContextCompat.getColor(getApplicationContext(), R.color.first_slide)));
        addSlide(AppIntroFragment.newInstance("",
                getResources().getString(R.string.msg_second_slider),
                R.drawable.introicono2,
                ContextCompat.getColor(getApplicationContext(), R.color.second_slide)));
        addSlide(AppIntroFragment.newInstance("",
                getResources().getString(R.string.msg_third_slider),
                R.drawable.introicono3,
                ContextCompat.getColor(getApplicationContext(), R.color.third_slide)));
        setDepthAnimation();
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }
}
