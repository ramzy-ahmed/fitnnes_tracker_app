package com.fitnnestracker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fitnnestracker.fragments.GoalsFragment;
import com.fitnnestracker.fragments.ProgressFragment;
import com.fitnnestracker.fragments.WorkoutsFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WorkoutsFragment();
            case 1:
                return new ProgressFragment();
            case 2:
                return new GoalsFragment();
            default:
                throw new IllegalStateException("Invalid tab position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
