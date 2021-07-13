package com.example.musicplayerproject.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.musicplayerproject.Adapter.SongAdapter;
import com.example.musicplayerproject.Model.SongsList;
import com.example.musicplayerproject.R;


import java.util.ArrayList;

public class AllSongFragment extends ListFragment
{
    //Deklarisanje promenljive Contet redolver koja sluzi za prikazivanje kolona direktno u fragmentu
    private static ContentResolver contentResolver1;
    //Deklarisanje array liste
    public ArrayList<SongsList> songsList;
    public ArrayList<SongsList> newList;

    private ListView listView;

    private createDataParse createDataParse;
    private ContentResolver contentResolver;

    //Instancu klase AllSongFragment i klase Bundle
    public static Fragment getInstance(int position, ContentResolver mcontentResolver)
    {
        //bundle sluzi za prosledjivanje podataka iz aktivnosti u aktivnost ili izmedju fragmenta
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        AllSongFragment tabFragment = new AllSongFragment();
        tabFragment.setArguments(bundle);
        contentResolver1 = mcontentResolver;
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDataParse = (createDataParse) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    //Trazi se element po idu
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.list_playlist);
        contentResolver = contentResolver1;
        setContent();
    }

    //Postavljanje contenta u ListView i slanje podataka u Activity
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setContent() {
        boolean searchedList;
        songsList = new ArrayList<>();
        newList = new ArrayList<>();
        getMusic();
        SongAdapter adapter = new SongAdapter(getContext(), songsList);
        //uslov za pretragu (tj. proverava se da li je prazno polje za pretragu)
        if (!createDataParse.queryText().equals("")) {
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            searchedList = true;
        } else {
            searchedList = false;
        }
        createDataParse.getLength(songsList.size());
        listView.setAdapter(adapter);

        //Na klik prosledjuje podatke o pesmi u listu
        final boolean finalSearchedList = searchedList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!finalSearchedList) {
                    createDataParse.onDataPass(songsList.get(position).getTitle(), songsList.get(position).getPath());
                    createDataParse.fullSongList(songsList, position);
                } else {
                    createDataParse.onDataPass(newList.get(position).getTitle(), newList.get(position).getPath());
                    createDataParse.fullSongList(songsList, position);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(position);
                return true;
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    //Postavjlanje cursora na prvu pesmu
    public void getMusic() {
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        //proverava da li je songcursor razlicit od 0  i da li je na vrhu (prvom mestu)
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //dodaje pesmu u listu dok ima pesama i na kraju zatvara cursor
            do {
                songsList.add(new SongsList(songCursor.getString(songTitle), songCursor.getString(songArtist), songCursor.getString(songPath)));
            } while (songCursor.moveToNext());
            songCursor.close();
        }
    }

    //pretraga pesme po naslovu i dodavanje u listu pesama
    public SongAdapter onQueryTextChange() {
        String text = createDataParse.queryText();
        for (SongsList songs : songsList) {
            String title = songs.getTitle().toLowerCase();
            if (title.contains(text)) {
                newList.add(songs);
            }
        }
        return new SongAdapter(getContext(), newList);

    }
    //kreiranje alertDialoga i prikazivanje dialoga
    //setovanje naslova i texta, i ne moze da se skloini kada se ide nazad (.setCancelable)
    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.play_next))
                .setCancelable(true)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createDataParse.currentSong(songsList.get(position));
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface createDataParse {
        void onDataPass(String name, String path);

        void fullSongList(ArrayList<SongsList> songList, int position);

        String queryText();

        void currentSong(SongsList songsList);
        void getLength(int length);
    }

}
