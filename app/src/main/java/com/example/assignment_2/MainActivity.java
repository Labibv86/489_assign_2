package com.example.assignment_2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // IMPORTANT: The fix for your errors!
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Open Broadcast screen as default on app start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BroadcastFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_broadcast);
        }

        // Modern back gesture dispatcher
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Close the drawer first so the animation slides away completely smoothly
        drawerLayout.closeDrawer(GravityCompat.START);

        // Wait until the drawer is completely shut before swapping fragments to prevent UI lag
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                Fragment selectedFragment = null;
                if (id == R.id.nav_broadcast) {
                    selectedFragment = new BroadcastFragment();
                } else if (id == R.id.nav_image) {
                    selectedFragment = new ImageFragment();
                } else if (id == R.id.nav_video) {
                    selectedFragment = new VideoFragment();
                } else if (id == R.id.nav_audio) {
                    selectedFragment = new AudioFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commitAllowingStateLoss();
                }

                // Remove the listener so it doesn't pile up on subsequent clicks
                drawerLayout.removeDrawerListener(this);
            }
        });

        return true;
    }
}