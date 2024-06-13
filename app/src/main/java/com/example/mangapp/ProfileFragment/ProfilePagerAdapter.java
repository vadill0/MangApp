package com.example.mangapp.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(Fragment fragment){
        super(fragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new PendingMangaFragment();
            case 2:
                return new FavoriteMangaFragment();
            case 3:
                return new ReadingMangaFragment();
            default:
                return new ReadMangaFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
