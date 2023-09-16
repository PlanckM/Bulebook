package com.guet.demo_android;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.guet.demo_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static String FragmentSetting="setting_fragment";
    public static String FragmentEditInfo="edit_info_fragment";
    public static String FragmentLiked="liked_fragment";
    public static String FragmentSaved="saved_fragment";
    public static String FragmentCollected="collected_fragment";
    public static String FragmentShared="shared_fragment";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntent();
        Toast.makeText(MainActivity.this, "Login success !", Toast.LENGTH_SHORT).show();
        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

//        NavigationView navigationView = binding.navView;
//        if (navigationView != null) {
//            mAppBarConfiguration = new AppBarConfiguration.Builder(
//                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings)
//                    .setOpenableLayout(binding.drawerLayout)
//                    .build();
//            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//            NavigationUI.setupWithNavController(navigationView, navController);
//        }

        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        // Using findViewById because NavigationView exists in different layout files
        // between w600dp and w1240dp
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            // The navigation drawer already has the items including the items in the overflow menu
            // We only inflate the overflow menu if the navigation drawer isn't visible
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //注意：这个我用来切换fragment的，使用方法，先创建fragment再在mobile_navigation中注册fragment,定义常量指定切换具体的fragment
    public void navigateF(String fragment){
        switch (fragment){
            case "setting_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_settings);
                break;
            case "edit_info_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.edit_info);
                break;
            case "shared_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.shared);
                break;
            case "collected_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.collected);
                break;
            case "saved_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.saved);
                break;
            case "liked_fragment":
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.liked);
                break;
        }
        hideBottomNavigationView();
    }
    public void hideBottomNavigationView() {
        binding.appBarMain.contentMain.bottomNavView.setVisibility(View.GONE);
        // 调整底部导航栏的高度为零
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) binding.appBarMain.contentMain.bottomNavView.getLayoutParams();
        layoutParams.height = 0;
        binding.appBarMain.contentMain.bottomNavView.setLayoutParams(layoutParams);
    }
    public void setBottomVisible(){
        binding.appBarMain.contentMain.bottomNavView.setVisibility(View.VISIBLE);
//         恢复底部导航栏的高度
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) binding.appBarMain.contentMain.bottomNavView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 或您希望的高度
        binding.appBarMain.contentMain.bottomNavView.setLayoutParams(layoutParams);
    }
}