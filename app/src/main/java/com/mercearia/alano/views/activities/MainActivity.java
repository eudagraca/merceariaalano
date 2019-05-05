package com.mercearia.alano.views.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mercearia.alano.R;
import com.mercearia.alano.database.CurrentUser;
import com.mercearia.alano.database.FirebaseConnect;
import com.mercearia.alano.utils.Helper;
import com.mercearia.alano.views.fragments.BankFragment;
import com.mercearia.alano.views.fragments.DebitarFragment;
import com.mercearia.alano.views.fragments.HistoryFragment;
import com.mercearia.alano.views.fragments.ProdutoFragment;
import com.mercearia.alano.views.fragments.RegistoFragment;

import java.util.Objects;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionToogle;
    private RegistoFragment registoFragment;
    private ProdutoFragment produtoFragment;
    private DebitarFragment debitarFragment;
    private HistoryFragment historyFragment;
    private BankFragment bankFragment;
    private DrawerLayout mDrawable;

    //Components
    private FragmentManager fragmentManager;
    private int fl_framelayout;
    private Context context;
    private TextView text_name;
    @Nullable
    private String nameOfUser;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();


        fragmentManager = getSupportFragmentManager();
        fl_framelayout = R.id.framelayout_container;
        NavigationView mNavegation = findViewById(R.id.nav_view);

        text_name = mNavegation.getHeaderView(0).findViewById(R.id.text_name);

        registoFragment = new RegistoFragment();
        produtoFragment = new ProdutoFragment();
        debitarFragment = new DebitarFragment();
        historyFragment = new HistoryFragment();
        bankFragment = new BankFragment();

        mDrawable = findViewById(R.id.drawer_layout);
        mActionToogle = new ActionBarDrawerToggle(MainActivity.this, mDrawable, R.string.open, R.string.close);
        mDrawable.addDrawerListener(mActionToogle);
        mActionToogle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Helper.isConnected(this);

        Helper.changeFragment(fl_framelayout, produtoFragment, fragmentManager);
        mNavegation.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.novo_prduto:
                    Helper.changeFragment(fl_framelayout, registoFragment, fragmentManager);
                    mDrawable.closeDrawers();
                    return true;
                case R.id.productos:
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
//                case R.id.logout:
//
//                    KAlertDialog dialog = new KAlertDialog(MainActivity.this, KAlertDialog.PROGRESS_TYPE);
//                    dialog.setTitleText("");
//                    dialog.setCancelable(false);
//                    dialog.show();
//                    if (auth != null) {
//
//                        auth.signOut();
//                        dialog.dismiss();
//                        startActivity(new Intent(this, AccessActivity.class));
//                        finish();
//                    }
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = CurrentUser.currentUser(context);

        Fabric.with(this, new Crashlytics());

        Helper.isConnected(this);
        if (user != null) {

            FirebaseFirestore firestore = FirebaseConnect.getFireStore(context);

            firestore.collection(Helper.COLLECTION_USER).document(Objects.requireNonNull(user.getUid()))
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    nameOfUser = Objects.requireNonNull(snapshot).getString("username");
                    text_name.setText(nameOfUser);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.destroyInternetDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}