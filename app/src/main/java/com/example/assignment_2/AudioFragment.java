package com.example.assignment_2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AudioFragment extends Fragment {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        Button btnPlay = view.findViewById(R.id.btn_play_audio);
        Button btnPause = view.findViewById(R.id.btn_pause_audio);

        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"));

        if (btnPlay != null) {
            btnPlay.setOnClickListener(v -> {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            });
        }

        if (btnPause != null) {
            btnPause.setOnClickListener(v -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            });
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}