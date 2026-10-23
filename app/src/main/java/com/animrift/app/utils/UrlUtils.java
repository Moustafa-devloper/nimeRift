package com.animrift.app.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.animrift.app.R;
import com.animrift.app.network.SourceValidator;

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_ANIME_TITLE = "anime_title";
    public static final String EXTRA_ANIME_GENRE = "anime_genre";
    public static final String EXTRA_VIDEO_URL = "video_url";
    public static final String EXTRA_SOURCE_URL = "source_url";

    private VideoView videoView;
    private SeekBar seekBar;
    private TextView playbackText;
    private TextView titleText;
    private Button playPauseButton;
    private Button openFileButton;
    private Button fullscreenButton;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoView != null && videoView.isPlaying()) {
                int current = videoView.getCurrentPosition();
                seekBar.setProgress(current);
                playbackText.setText(formatTime(current) + " / " + formatTime(videoView.getDuration()));
                uiHandler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        seekBar = findViewById(R.id.seekBar);
        playbackText = findViewById(R.id.playbackText);
        titleText = findViewById(R.id.playerTitleText);
        playPauseButton = findViewById(R.id.playPauseButton);
        openFileButton = findViewById(R.id.openFileButton);
        fullscreenButton = findViewById(R.id.fullscreenButton);

        String title = getIntent().getStringExtra(EXTRA_ANIME_TITLE);
        if (title != null) {
            titleText.setText(title);
        }

        openFileButton.setOnClickListener(v -> openLocalVideo());
        playPauseButton.setOnClickListener(v -> togglePlayback());
        fullscreenButton.setOnClickListener(v -> toggleFullscreen());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        String videoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        if (SourceValidator.isValidHttpUrl(videoUrl)) {
            openRemoteVideo(videoUrl);
        } else {
            playbackText.setText(R.string.no_valid_url);
        }
    }

    private void openRemoteVideo(String url) {
        try {
            Uri uri = Uri.parse(url);
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> {
                int duration = mp.getDuration();
                seekBar.setMax(duration);
                playbackText.setText("0:00 / " + formatTime(duration));
                playPauseButton.setText(R.string.pause);
                videoView.start();
                uiHandler.post(progressRunnable);
            });
            videoView.setOnErrorListener((mp, what, extra) -> {
                playbackText.setText(R.string.video_error);
                return true;
            });
        } catch (Exception e) {
            playbackText.setText(R.string.video_error);
        }
    }

    private void openLocalVideo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();
            videoView.start();
            playPauseButton.setText(R.string.pause);
        }
    }

    private void togglePlayback() {
        if (videoView.isPlaying()) {
            videoView.pause();
            playPauseButton.setText(R.string.play);
        } else {
            if (videoView.getDuration() > 0) {
                videoView.start();
                playPauseButton.setText(R.string.pause);
                uiHandler.post(progressRunnable);
            }
        }
    }

    private void toggleFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private String formatTime(int milliseconds) {
        int totalSeconds = milliseconds / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(java.util.Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }
}
