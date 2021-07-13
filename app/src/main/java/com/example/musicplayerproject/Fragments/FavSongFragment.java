package com.example.musicplayerproject.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.musicplayerproject.Adapter.SongAdapter;
import com.example.musicplayerproject.DB.FavoritesOperations;
import com.example.musicplayerproject.Model.SongsList;
import com.example.musicplayerproject.R;

import java.util.ArrayList;

public class FavSongFragment extends ListFragment
{
    //Deklaracija
    private FavoritesOperations favoritesOperations;

    //Incijalizacija nizova i ListView-a
    public ArrayList<SongsList> songsList;
    public ArrayList<SongsList> newList;

    private ListView listView;

    private createDataParsed createDataParsed;

    //Instanca fragmenta i primena singleton patterna
    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        FavSongFragment tabFragment = new FavSongFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //Sluzi da zakaci ceo fragment za aktivnost
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createDataParsed = (createDataParsed) context;
        favoritesOperations = new FavoritesOperations(context);
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


     //Setting the content in the listView and sending the data to the Activity
    //Kreiranje dve array liste i kreiranje instance song adaptera
    public void setContent() {
        boolean searchedList = false;
        songsList = new ArrayList<>();
        newList = new ArrayList<>();
        songsList = favoritesOperations.getAllFavorites();
        SongAdapter adapter = new SongAdapter(getContext(), songsList);
        //da li je nesto uneseno u search
        if (!createDataParsed.queryText().equals("")) {
            adapter = onQueryTextChange();
            adapter.notifyDataSetChanged();
            searchedList = true;
        } else {
            searchedList = false;
        }

        listView.setAdapter(adapter);

        final boolean finalSearchedList = searchedList;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Na klik prosledjuje podatke o pesmi u listu
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!finalSearchedList) {
                    createDataParsed.onDataPass(songsList.get(position).getTitle(), songsList.get(position).getPath());
                    createDataParsed.fullSongList(songsList, position);
                } else {
                    createDataParsed.onDataPass(newList.get(position).getTitle(), newList.get(position).getPath());
                    createDataParsed.fullSongList(songsList, position);
                }
            }
        });
        //Brisanje pesme iz liste na long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteOption(position);
                return true;
            }
        });
    }

    //Brisanje pesme i pojavljivanje alert dialoga
    private void deleteOption(int position) {
        if (position != createDataParsed.getPosition())
            showDialog(songsList.get(position).getPath(), position);
        else
            Toast.makeText(getContext(), "You Can't delete the Current Song", Toast.LENGTH_SHORT).show();
    }
    //Interfejs
    public interface createDataParsed {
        public void onDataPass(String name, String path);
        //Lista pesama
        public void fullSongList(ArrayList<SongsList> songList, int position);
        //Pozicija pesme
        public int getPosition();
        //Pretraga
        public String queryText();
    }
    //Pretraga/Search
    public SongAdapter onQueryTextChange() {
        String text = createDataParsed.queryText();
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
    private void showDialog(final String index, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.delete_text))
                .setCancelable(true)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoritesOperations.removeSong(index);
                        createDataParsed.fullSongList(songsList, position);
                        setContent();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
