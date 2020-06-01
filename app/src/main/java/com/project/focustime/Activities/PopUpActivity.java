package com.project.focustime.Activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.project.focustime.Models.Course;
import com.project.focustime.R;
import com.project.focustime.Utils.VideoConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PopUpActivity extends YouTubeBaseActivity {
    private Course mCourse;
    YouTubePlayerView myouTubePlayerView;
    YouTubePlayer.OnInitializedListener  mOnInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width) , (int) (height));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getCourseObject();
    }

    private void getCourseObject() {
        mCourse = (Course) getIntent().getSerializableExtra("Course");
        playCoursePreview();
    }

    private void playCoursePreview() { //getCourseOverviewProvider() ve getCourseOverviewUrl() OLMADAN ÇALIŞMIYOR SONRADAN EKLE
       /* if (mCourse.getCourseOverviewProvider().equals("youtube")){
            playYouTubeVideo();
        }
        if (mCourse.getCourseOverviewProvider().equals("html5")){
            playHTML5Video();
        }

        */
       playYouTubeVideo();
        //playHTML5Video();
    }

    private void playHTML5Video() {
        final VideoView videoView;
        videoView = (VideoView) findViewById(R.id.html5Player);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath(mCourse.getFirstLesson());
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }
    private void playYouTubeVideo() {
        final String youtubeVideoId = VideoConfig.extractYoutubeId(mCourse.getFirstLesson());

        myouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        myouTubePlayerView.setVisibility(View.VISIBLE);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(youtubeVideoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        myouTubePlayerView.initialize(VideoConfig.getApiKey(), mOnInitializedListener);
    }

}
