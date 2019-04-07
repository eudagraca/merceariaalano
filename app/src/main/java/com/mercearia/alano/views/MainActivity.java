package com.mercearia.alano.views;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.mercearia.alano.R;
import com.mercearia.alano.utils.MyUtils;
import java.util.Objects;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionToogle;
    private RegistoFragment registoFragment;
    private ProdutoFragment produtoFragment;

    //Components
    private FragmentManager fragmentManager;
    private int fl_framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fl_framelayout = R.id.framelayout_container;
        NavigationView mNavegation = findViewById(R.id.nav_view);
        registoFragment = new RegistoFragment();
        produtoFragment = new ProdutoFragment();

        DrawerLayout mDrawable = findViewById(R.id.drawer_layout);
        mActionToogle = new ActionBarDrawerToggle(MainActivity.this, mDrawable, R.string.open, R.string.close);
        mDrawable.addDrawerListener(mActionToogle);
        mActionToogle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        MyUtils.changeFragment(fl_framelayout, produtoFragment, fragmentManager);

        mNavegation.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()){
                case R.id.novo_prduto:
                    MyUtils.changeFragment(fl_framelayout, registoFragment, fragmentManager);
                    mDrawable.closeDrawers();
                    return true;
                case R.id.produtos:
                    MyUtils.changeFragment(fl_framelayout, produtoFragment, fragmentManager);
                    mDrawable.closeDrawers();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}