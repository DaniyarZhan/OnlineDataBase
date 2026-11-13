package com.example.onlinedatabase;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinedatabase.chats.ChatActivity;
import com.example.onlinedatabase.chats.ContactsActivity;

public class LoadedUserDataActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_user_data);
        Init();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void Init() {
        mPlayer = MediaPlayer.create(this, R.raw.shade);
        mPlayer.setVolume(0.2f, 0.2f);
        mPlayer.setLooping(true);
        mPlayer.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayer != null)
            mPlayer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying())
            mPlayer.pause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }
}