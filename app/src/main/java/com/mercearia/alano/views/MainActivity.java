package com.mercearia.alano.views;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.kinda.alert.KAlertDialog;
import com.mercearia.alano.R;
import com.mercearia.alano.utils.Helper;

import java.util.Objects;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionToogle;
    private RegistoFragment registoFragment;
    private ProdutoFragment produtoFragment;
    private DebitarFragment debitarFragment;

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
        debitarFragment = new DebitarFragment();

        DrawerLayout mDrawable = findViewById(R.id.drawer_layout);
        mActionToogle = new ActionBarDrawerToggle(MainActivity.this, mDrawable, R.string.open, R.string.close);
        mDrawable.addDrawerListener(mActionToogle);
        mActionToogle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Helper.changeFragment(fl_framelayout, produtoFragment, fragmentManager);



        if (getIntent().getStringExtra("message") != null) {
            KAlertDialog dialog = new KAlertDialog(MainActivity.this, KAlertDialog.SUCCESS_TYPE);
            String produto = getIntent().getStringExtra("message");
            dialog.setTitleText("Apagou o produto "+produto);
            dialog.show();
        }

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
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}