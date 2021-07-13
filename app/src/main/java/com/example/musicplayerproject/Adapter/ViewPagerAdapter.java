package com.example.musicplayerproject.Adapter;

import android.content.ContentResolver;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//importi iz celog foldera za fragmente
import com.example.musicplayerproject.Fragments.AllSongFragment;
import com.example.musicplayerproject.Fragments.CurrentSongFragment;
import com.example.musicplayerproject.Fragments.FavSongFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{

    // Inicijalizacija niza za tabove i klase contentResolver
    private ContentResolver contentResolver;
    private String title[] = {"All SONGS","CURRENT PLAYLIST", "FAVORITES"};
    //Konstruktor klase ViewPagerAdapter
    public ViewPagerAdapter(FragmentManager fm, ContentResolver contentResolver)
    {
        super(fm);
        this.contentResolver = contentResolver;
    }

    //Koji se fragment se otvara na osnovu id-a (kada se klikne na neki tab, otvara se fragment koji se nalazi na tom tabu)
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllSongFragment.getInstance(position, contentResolver);
            case 1:
                return CurrentSongFragment.getInstance(position);
            case 2:
                return FavSongFragment.getInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
