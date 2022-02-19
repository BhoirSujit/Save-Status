package com.gameside.savestatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.gameside.savestatus.databinding.ActivityMainBinding;
import com.gameside.savestatus.fragments.SavedFragment;
import com.gameside.savestatus.fragments.StatusFragment;
import com.gameside.savestatus.fragments.ToolsFragment;
import com.gameside.savestatus.adapters.ViewPageAdapter;
import com.gameside.savestatus.utilities.AdmobUtility;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //admob initialize
        AdmobUtility admobUtility = new AdmobUtility(this);

        //banner ad
        adView = binding.adView;
        admobUtility.bannerAd(adView);


        //call important methods
        setupViewPagerWithTabLayout();

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_feedback_option:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("text/plain");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gamesidebuz@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Save Status Feedback");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
                return true;


            case R.id.menu_share_option:

                return true;

            case R.id.menu_how_to_use_option:

                return true;

            case R.id.menu_more_apps_option:

                return true;

            case R.id.menu_privacy_policy_option:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //tablayout with viewpager
    public void setupViewPagerWithTabLayout(){
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager2 = binding.viewPager2;

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new StatusFragment());
        fragmentArrayList.add(new SavedFragment());
        fragmentArrayList.add(new ToolsFragment());

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());
        viewPageAdapter.setFragmentArrayList(fragmentArrayList);
        viewPager2.setAdapter(viewPageAdapter);
        String[] tabTitle = {"STATUS", "SAVED", "TOOLS"};
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabTitle[position]);
        });
        tabLayoutMediator.attach();
    }

}