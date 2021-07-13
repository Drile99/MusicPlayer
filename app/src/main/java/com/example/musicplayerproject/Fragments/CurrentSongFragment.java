package com.example.musicplayerproject.Fragments;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.musicplayerproject.Adapter.SongAdapter;
import com.example.musicplayerproject.Model.SongsList;
import com.example.musicplayerproject.R;


import java.util.ArrayList;

public class CurrentSongFragment extends ListFragment
{
    //Kreiranje ArrayListe tipa SongListe
    public ArrayList<SongsList> songsList = new ArrayList<>();

    private ListView listView;

    private createDataParsed createDataParsed;

    //Kreiranje instance klase bundle i kreiranje instancu klase CurrentSongFragment
    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        CurrentSongFragment tabFragment = new CurrentSongFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        createDataParsed = (createDataParsed) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        setContent();
    }


      //Postavljanje contenta u ListView i slanje podataka u Activity

    public void setContent() {
        if (createDataParsed.getSong() != null)
            songsList.add(createDataParsed.getSong());

        SongAdapter adapter = new SongAdapter(getContext(), songsList);
        //provera da li postoji vise od jedne pesme
        if (songsList.size() > 1)
            if (createDataParsed.getPlaylistFlag()) {
                songsList.clear();
            }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDataParsed.onDataPass(songsList.get(position).getTitle(), songsList.get(position).getPath());
                createDataParsed.fullSongList(songsList, position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }

    public interface createDataParsed {
        void onDataPass(String name, String path);

        void fullSongList(ArrayList<SongsList> songList, int position);

        SongsList getSong();

        boolean getPlaylistFlag();
    }


}

