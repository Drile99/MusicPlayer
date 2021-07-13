package com.example.musicplayerproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.example.musicplayerproject.R;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class RadioActivity extends AppCompatActivity
{

    //Inicijalizacija
    ImageButton btnPlay;

    boolean prepared = false;

    MediaPlayer mediaPlayer;
    //Link do radio stanice
    String stream = "http://stream.radioreklama.bg:80/radio1rock128";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        //Trazenje elementa po id-u
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Postavljanje "osluskivaca" na navigacioni menu i postavlja se sta se desava kada
        // se klinke na svaki item iz menija
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //sta se desava kada je pritisnuta neka stavka navigacije (menija)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    //sta se desava ako se klikne stavka About
                    case R.id.nav_about:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RadioActivity.this);
                        builder.setTitle(getString(R.string.about))
                                .setMessage(getString(R.string.about_text))
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                        //sta se desava ako se klikne stavka Radio
                    case R.id.nav_radio:
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                        //sta se desava ako se klikne stavka Youtube
                    case R.id.youtube:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com"));
                        startActivity(browserIntent);
                        break;
                }
                return true;

            }
        });


        btnPlay = findViewById(R.id.btnPlay);
        //kreiranje instance klase MediaPlayer
        mediaPlayer = new MediaPlayer();
        //AudioManger se podesava da moze da "pusta" muziku
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);

        //Sta se desava kada korisnik pritisne play dugme ikonica se menja na play ili pause
        // u slucaju da li se muzika pusta ili pauzira
        btnPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mediaPlayer.isPlaying())
                {
                    btnPlay.setImageResource(R.drawable.pause_icon);
                    mediaPlayer.pause();
                }
                else
                {
                    btnPlay.setImageResource(R.drawable.play_icon);
                    mediaPlayer.start();
                }
            }
        });
    }

    //Klasa koja nasledjuje klasu za AsyncTask da moze da se pripremi za pustanje radio stanice
    class PlayerTask extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings)
        {
            try
            {
                mediaPlayer.setDataSource(stream);
                mediaPlayer.prepareAsync();
                prepared = true;
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
            return prepared;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            btnPlay.setImageResource(R.drawable.play_icon);
            mediaPlayer.start();

        }
    }

    //Metoda onPause proverava da li je pauzirana muzika iz mediaPlayera
    @Override
    protected void onPause()
    {
        super.onPause();
        if(mediaPlayer.isPlaying())
        {
            btnPlay.setImageResource(R.drawable.pause_icon);
            mediaPlayer.pause();
        }
        else
        {
            btnPlay.setImageResource(R.drawable.play_icon);
            mediaPlayer.start();
        }
    }

    //Metoda onPause proverava da li je pustena muzika iz mediaPlayera
    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        if(!mediaPlayer.isPlaying())
        {
            btnPlay.setImageResource(R.drawable.play_icon);
            mediaPlayer.start();
        }
        else
        {
            btnPlay.setImageResource(R.drawable.pause_icon);
            mediaPlayer.pause();
        }
    }

    //Prekida pripremu streama (ukoliko se zatvori aplikacija)
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(prepared)
        {
            mediaPlayer.release();
        }
    }

}