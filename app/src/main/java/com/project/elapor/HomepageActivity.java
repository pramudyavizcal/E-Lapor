package com.project.elapor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.project.elapor.databinding.ActivityHomepageBinding;
import com.project.elapor.ui.dashboard.DashboardFragment;
import com.project.elapor.ui.dashboard.ResponseTimeFragment;
import com.project.elapor.ui.pengaduan_masuk.PengaduanFragment;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanFragmentTerkirim;
import com.project.elapor.ui.sign_out.SignOutFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_homepage);
        //NavigationUI.setupWithNavController(binding.navView, navController);

        animatedBottomBar = findViewById(R.id.nav_view);

//        if (savedInstanceState == null) {
//            animatedBottomBar.selectTabById(R.id.navigation_dashboard, true);
//            fragmentManager = getSupportFragmentManager();
//            DashboardFragment homeFragment = new DashboardFragment();
//            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_homepage, homeFragment)
//                    .commit();
//        }
        animatedBottomBar.selectTabById(R.id.navigation_dashboard, true);
        animatedBottomBar.setOnTabSelectListener((lastIndex, lastTab, newIndex, newTab) -> {
            Fragment fragment = null;
            switch (newTab.getId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_response_time:
                    fragment = new ResponseTimeFragment();
                    break;
                case R.id.navigation_pengaduan_masuk:
                    fragment = new PengaduanFragment();
                    break;
                case R.id.navigation_pengaduan_terkirim:
                    fragment = new PengaduanFragmentTerkirim();
                    break;
                case R.id.navigation_sign_out:
                    fragment = new SignOutFragment();
                    break;
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment)
                        .commit();
            } else {
                Log.e(TAG, "Error in creating Fragment");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void exitByBackKey() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                HomepageActivity.this);


        alertDialogBuilder.setTitle("Exit");


        alertDialogBuilder
                .setMessage("Apakah anda yakin ingin keluar aplikasi?")

                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
        ;


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}