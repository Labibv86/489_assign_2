package com.example.assignment_2;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        VideoView videoView = view.findViewById(R.id.video_view);

        if (videoView != null) {
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            // Build path pointing directly to your res/raw/sample_video.mp4 file
            String videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.sample_video;
            videoView.setVideoURI(Uri.parse(videoPath));

            // Starts playing immediately from local storage without buffering lag
            videoView.setOnPreparedListener(mp -> videoView.start());
        }

        return view;
    }
}