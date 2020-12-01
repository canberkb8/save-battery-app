package com.canberkbbc.savebattery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.canberkbbc.savebattery.databinding.ActivityMainBinding;
import com.canberkbbc.savebattery.ui.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View view;
    ActivityMainBinding mainBinding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        );
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
                Log.i("TAG", "onNavigationItemSelected: case installed_app");
                break;
            case R.id.usage_stats:
                changeFragment(R.id.timeUsedFragment);
                Log.i("TAG", "onNavigationItemSelected: case usage_stats");
                break;
            case R.id.nav_example3:
                //changeFragment(R.id.homePageFragment);
                Log.i("TAG", "onNavigationItemSelected: case nav_example3");
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