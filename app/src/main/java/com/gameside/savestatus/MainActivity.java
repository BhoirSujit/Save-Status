package com.gameside.savestatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.gameside.savestatus.databinding.ActivityMainBinding;
import com.gameside.savestatus.fragments.SavedFragment;
import com.gameside.savestatus.fragments.StatusFragment;
import com.gameside.savestatus.fragments.ToolsFragment;
import com.gameside.savestatus.adapters.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //call important methods
        setupViewPagerWithTabLayout();

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