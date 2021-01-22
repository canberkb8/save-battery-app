package com.canberkbbc.savebattery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.canberkbbc.savebattery.databinding.ActivityMainBinding;
import com.canberkbbc.savebattery.services.ApiClient;
import com.canberkbbc.savebattery.services.IApiService;
import com.canberkbbc.savebattery.utils.AppUtils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding mainBinding;
    NavController navController;
    public static IApiService iApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        iApiService= ApiClient.getClient().create(IApiService.class);
        navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.deleteCache(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.deleteCache(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationViewListener();
    }

    private void setNavigationViewListener() {
        mainBinding.drawerNavView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.installed_app:
                changeFragment(R.id.homePageFragment);
                Log.i("TAG", "onNavigationItemSelected: case homePageFragment");
                break;
            case R.id.usage_stats:
                changeFragment(R.id.timeUsedFragment);
                Log.i("TAG", "onNavigationItemSelected: case timeUsedFragment");
                break;
            case R.id.usage_network:
                changeFragment(R.id.networkUsedFragment);
                Log.i("TAG", "onNavigationItemSelected: case networkUsedFragment");
                break;
            case R.id.battery_stats:
                changeFragment(R.id.batteryStatsFragment);
                Log.i("TAG", "onNavigationItemSelected: case batteryStatsFragment");
                break;
            case R.id.info_page:
                changeFragment(R.id.infoFragment);
                Log.i("TAG", "onNavigationItemSelected: case infoFragment");
                break;
            default:
                break;
        }
        mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void changeFragment(NavDirections navDirections) {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(navDirections);
    }

    public void changeFragment(int fragmentId) {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(fragmentId);
    }

}