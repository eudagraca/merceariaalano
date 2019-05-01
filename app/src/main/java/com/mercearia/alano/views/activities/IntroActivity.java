package com.mercearia.alano.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.mercearia.alano.R;
import com.mercearia.alano.views.fragments.AppIntroFragment;


public class IntroActivity extends AppIntro {

    // Please DO NOT override onCreate. Use init


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//adding the three slides for introduction app you can ad as many you needed
        addSlide(AppIntroFragment.newInstance(R.layout.intro1));
        addSlide(AppIntroFragment.newInstance(R.layout.intro2));

// Show and Hide Skip and Done buttons
        showStatusBar(false);
        showSkipButton(false);

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);

// Vibrate for 300 milliseconds
        v.vibrate(300);

//Add animation to the intro slider
        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}