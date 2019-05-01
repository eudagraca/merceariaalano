package com.mercearia.alano.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.mercearia.alano.R;
import com.mercearia.alano.utils.Helper;
import com.mercearia.alano.views.fragments.BankFragment;
import com.mercearia.alano.views.fragments.DebitarFragment;
import com.mercearia.alano.views.fragments.HistoryFragment;
import com.mercearia.alano.views.fragments.ProdutoFragment;
import com.mercearia.alano.views.fragments.RegistoFragment;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionToogle;
    private RegistoFragment registoFragment;
    private ProdutoFragment produtoFragment;
    private DebitarFragment debitarFragment;
    private HistoryFragment historyFragment;
    private BankFragment bankFragment;
    private DrawerLayout mDrawable;
    private NavigationView mNavegation;

    //Components
    private FragmentManager fragmentManager;
    private int fl_framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fl_framelayout = R.id.framelayout_container;
        mNavegation = findViewById(R.id.nav_view);
        registoFragment = new RegistoFragment();
        produtoFragment = new ProdutoFragment();
        debitarFragment = new DebitarFragment();
        historyFragment = new HistoryFragment();
        bankFragment = new BankFragment();

        mDrawable = findViewById(R.id.drawer_layout);
        mActionToogle = new ActionBarDrawerToggle(MainActivity.this, mDrawable, R.string.open, R.string.close);
        mDrawable.addDrawerListener(mActionToogle);
        mActionToogle.syncState();
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Helper.changeFragment(fl_framelayout, produtoFragment, fragmentManager);


        SharedPreferences sp = getSharedPreferences(Helper.SP_NAME, Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }


        //  Declare a new thread to do a preference check
        Thread t = new Thread(() -> {
            //  Initialize SharedPreferences
            SharedPreferences getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());

            //  Create a new boolean and preference and set it to true
            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

            //  If the activity has never started before...
            if (isFirstStart) {
                //  Launch app intro
                final Intent i = new Intent(getBaseContext(), IntroActivity.class);

                runOnUiThread(() -> startActivity(i));

                //  Make a new preferences editor
                SharedPreferences.Editor e = getPrefs.edit();

                //  Edit preference to make it false because we don't want this to run again
                e.putBoolean("firstStart", false);

                //  Apply changes
                e.apply();
            } else {

                mNavegation.setNavigationItemSelectedListener(menuItem -> {

                    switch (menuItem.getItemId()) {
                        case R.id.novo_prduto:
                            Helper.changeFragment(fl_framelayout, registoFragment, fragmentManager);
                            mDrawable.closeDrawers();
                            return true;
                        case R.id.produtos:
                            Helper.changeFragment(fl_framelayout, produtoFragment, fragmentManager);
                            mDrawable.closeDrawers();
                            return true;
                        case R.id.vendas:
                            Helper.changeFragment(fl_framelayout, debitarFragment, fragmentManager);
                            mDrawable.closeDrawers();
                            return true;
                        case R.id.historico:
                            Helper.changeFragment(fl_framelayout, historyFragment, fragmentManager);
                            mDrawable.closeDrawers();
                            return true;
                        case R.id.bank:
                            Helper.changeFragment(fl_framelayout, bankFragment, fragmentManager);
                            mDrawable.closeDrawers();
                            return true;
                    }
                    return false;
                });

            }
        });

        // Start the thread
        t.start();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}