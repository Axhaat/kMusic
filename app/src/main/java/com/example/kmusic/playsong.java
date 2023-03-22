package com.example.kmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    TextView textView;
    ImageView pause,previous,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    SeekBar seekBar;
    Thread updateseek;
    int position;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        textView=findViewById(R.id.textView);
        pause=findViewById(R.id.pause);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        previous=findViewById(R.id.previous);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songlist");
        textcontent=intent.getStringExtra("currentsong");
        textView.setText(textcontent);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
                   mediaPlayer.seekTo(seekBar.getProgress());

           }
       });

     updateseek = new Thread(){
     @Override
     public void run() {
         int currentposition=0;
         try{
             while(currentposition<=mediaPlayer.getDuration()){
                 currentposition=mediaPlayer.getCurrentPosition();
                 seekBar.setProgress(currentposition);
                 sleep(800);
             }
         }
         catch(Exception e){
             e.printStackTrace();
         }
     }
            };updateseek.start();


        pause.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view) {
              if(mediaPlayer.isPlaying()) {
                     pause.setImageResource(R.drawable.play);
             mediaPlayer.pause();
         }
         else
         {
             pause.setImageResource(R.drawable.pause);
             mediaPlayer.start();
         }
     }
 });

 previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0) {
                    position=position-1;
                }
                else
                {
                    position=songs.size()-1;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textView.setText(songs.get(position).getName().replace(".mp3",""));

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1) {
                    position=position+1;
                }
                else
                {
                    position=0;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textView.setText(songs.get(position).getName().replace(".mp3",""));

            }
        });


    }

}