package com.project.focustime.Activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.Adapters.LessonAdapter;
import com.project.focustime.Adapters.SectionAdapter;
import com.project.focustime.DBOperations;
import com.project.focustime.Models.Lesson;
import com.project.focustime.Models.Course;
import com.project.focustime.Models.CourseDetails;
import com.project.focustime.Models.MyCourse;
import com.project.focustime.Models.Section;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.project.focustime.Utils.VideoConfig;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.project.focustime.connectServer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends YouTubeBaseActivity implements SectionAdapter.PassLessonToActivity {

    private TextView courseTitle;
    private TextView courseCompletionNumberOutOfTotal;
    private ProgressBar courseCompletionProgressBar;
    private MyCourse mCourse;
    private ArrayList<Section> mSections = new ArrayList<>();
    private ProgressBar progressBar;
    private ProgressBar progressBarForVideoPlayer;
    private ArrayList<CourseDetails> mEachCourseDetail = new ArrayList<>();
    private ArrayList<Lesson> mLessons = new ArrayList<>();

    RecyclerView sectionRecyclerView;
    VideoView html5VideoPlayer;
    YouTubePlayerView youtubePlayer;
    ImageButton fullScreenLessonBtn;
    Lesson mLesson;
    ImageView emptyVideoScreen;
    TextView attachmentTitle;
    Button downloadAttachmentButton;
    RelativeLayout downloadAttachmentArea;
    DownloadManager downloadManager;
    Spinner moreButton;
    RelativeLayout quizStuffs;



    TextView quizTitle;
    Button startQuiz;

    YouTubePlayer.OnInitializedListener  mOnInitializedListener;

    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    YouTubePlayer youTubePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        init();
        youtubePlayer.initialize(VideoConfig.getApiKey(),new YouTubePlayer.OnInitializedListener(){
            //YouTubePlayer youTubePlasyer=null;
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {

                youTubePlayer=player;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


            }
        });
        getCourseObject();

        mCourse.setTotalNumberOfLessons(db.getLessonNumber(conn,mCourse.getId()));
        courseCompletionNumberOutOfTotal.setText(mCourse.getTotalNumberOfCompletedLessons()+"/"+mCourse.getTotalNumberOfLessons()+" Lessons are completed");

        getAllSections();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width) , (int) (height));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);


        initMoreOptionButton();


    }

    private void init() {
        courseTitle = findViewById(R.id.courseTitle);
        courseCompletionNumberOutOfTotal = findViewById(R.id.courseCompletionNumberOutOfTotal);
        courseCompletionProgressBar = findViewById(R.id.courseCompletionProgressBar);
        sectionRecyclerView = findViewById(R.id.sectionRecyclerView);
        html5VideoPlayer = findViewById(R.id.html5Player);
        youtubePlayer =  findViewById(R.id.youtubePlayer);
        fullScreenLessonBtn = findViewById(R.id.fullScreenLesson);
        emptyVideoScreen = findViewById(R.id.emptyVideoScreen);
        downloadAttachmentArea = findViewById(R.id.downloadAttachmentArea);
        attachmentTitle = findViewById(R.id.attachmentTitle);
        downloadAttachmentButton = findViewById(R.id.downloadAttachmentButton);
        moreButton = findViewById(R.id.moreButton);
        quizTitle = findViewById(R.id.quizTitle);
        startQuiz = findViewById(R.id.startQuiz);
        quizStuffs = findViewById(R.id.quizStuffs);
        initProgressBar();
        initViewElement();
       //initBroadcastReceiver();
    }

    private void initViewElement() {
        fullScreenLessonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreenTheLesson(mLesson);
            }
        });
        downloadAttachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAttachemt();
            }
        });


    }

    private void initMoreOptionButton() {
        ArrayAdapter<CharSequence> optionAdapter = ArrayAdapter.createFromResource(this, R.array.lesson_options, android.R.layout.simple_spinner_item);
        optionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreButton.setAdapter(optionAdapter);
        moreButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemIdAtPosition(i) == 1){
                    shareThisCourse(mCourse.getShareableLink());
                }
                /*else if(adapterView.getItemIdAtPosition(i) == 2) {
                    getCourseObjectById("share");
                }

                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void shareThisCourse(String shareableLink) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_TEXT, "https://www.thefocustime.com/course-detail.php?url="+shareableLink);
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }
    private void switchToCourseDetailsActivity(Course mCourse) {
        Intent intent = new Intent(LessonActivity.this, CourseDetailsActivity.class);
        intent.putExtra("Course", mCourse);
        this.startActivity(intent);
    }

    private void getAllSections() {
        progressBar.setVisibility(View.VISIBLE);
        // Making empty array of category
        mSections = new ArrayList<>();
        // Auth Token
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        String authToken = preferences.getString("userToken", "loggedOut");

        mLessons = db.getLesson(conn,mCourse.getId());

        mSections.add(new Section(1,mCourse.getTitle(),mLessons,0,"10",1,10));
        initSectionRecyclerView();

        progressBar.setVisibility(View.GONE);

    }
    // Initialize the progress bar
    private void initProgressBar() {
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);

        progressBarForVideoPlayer = findViewById(R.id.loadingVideoPlayer);
        Sprite waveLoading = new Wave();
        progressBarForVideoPlayer.setIndeterminateDrawable(waveLoading);
    }

    private void initSectionRecyclerView() {
        SectionAdapter adapter = new SectionAdapter(getApplicationContext(), mSections, this, true);
        sectionRecyclerView.setAdapter(adapter);
        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    private void getCourseObject() {
        mCourse = (MyCourse) getIntent().getSerializableExtra("Course");
        courseTitle.setText(mCourse.getTitle());
        //courseCompletionNumberOutOfTotal.setText(mCourse.getTotalNumberOfCompletedLessons()+"/"+mCourse.getTotalNumberOfLessons()+" Lessons are completed");
        courseCompletionProgressBar.setProgress(mCourse.getCourseCompletion());
        Log.d("CourseId", mCourse.getId()+"");
    }
    public void handleBackButton(View view) {
        LessonActivity.super.onBackPressed();
    }

    @Override
    public void PassLesson(final Lesson eachLesson, LessonAdapter.ViewHolder viewHolder) {
        //viewHolder.itemView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_custom_blue_button));
        this.mLesson = eachLesson;
        courseTitle.setText(eachLesson.getTitle());
        if (eachLesson.getLessonType().equals("video")){
            System.out.println(eachLesson.getVideoUrl());
            //fullScreenLessonBtn.setVisibility(View.VISIBLE);
            if (eachLesson.getVideoType().equals("youtube") && !eachLesson.getVideoUrl().equals("")){

                playYouTubeVideo(eachLesson.getVideoUrl());

            }else{
                fullScreenLessonBtn.setVisibility(View.GONE);
                Toast.makeText(this, "Proper Video URL is Missing", Toast.LENGTH_SHORT).show();
                html5VideoPlayer.setVisibility(View.GONE);
                quizStuffs.setVisibility(View.GONE);
                emptyVideoScreen.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void ToggleLessonCompletionStatus(Lesson eachLesson, final boolean lessonCompletionStatus) {
        int courseProgress = 0;
        if (lessonCompletionStatus){
            courseProgress = 1;
        }else{
            courseProgress = 0;
        }

        //progressBar.setVisibility(View.VISIBLE);
        // Auth Token
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        String authToken = preferences.getString("userToken", "loggedOut");

        if(lessonCompletionStatus){
            //mCourse.setTotalNumberOfCompletedLessons(mCourse.getTotalNumberOfCompletedLessons()+1);
            Toast.makeText(this, "Prssssssing", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(this, "Prssssssing", Toast.LENGTH_SHORT).show();
    }

    private void playHTML5Video(final String videoUrl) {
        downloadAttachmentArea.setVisibility(View.GONE);
        progressBarForVideoPlayer.setVisibility(View.VISIBLE);
        emptyVideoScreen.setVisibility(View.GONE);
        html5VideoPlayer.setVisibility(View.VISIBLE);
        quizStuffs.setVisibility(View.GONE);
        html5VideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {

                        MediaController mc = new MediaController(LessonActivity.this);
                        html5VideoPlayer.setMediaController(mc);

                        mc.setAnchorView(html5VideoPlayer);
                        progressBarForVideoPlayer.setVisibility(View.GONE);
                    }
                });
            }
        });
        html5VideoPlayer.setVideoPath(videoUrl);
        MediaController mediaController = new MediaController(this);
        html5VideoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(html5VideoPlayer);
        html5VideoPlayer.start();
        final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        progressBarForVideoPlayer.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        progressBarForVideoPlayer.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        progressBarForVideoPlayer.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }

        };

        html5VideoPlayer.setOnInfoListener(onInfoToPlayStateListener);
    }


    private void playYouTubeVideo(final String videoUrl) {
        html5VideoPlayer.setVisibility(View.GONE);
        final String youtubeVideoId = VideoConfig.extractYoutubeId(videoUrl);
        youtubePlayer.setVisibility(View.VISIBLE);

        if(youTubePlayer!=null){
            youTubePlayer.loadVideo(youtubeVideoId);
        }else{
            System.out.println("İçi boş");
        }



    }

    public void fullScreenTheLesson(Lesson eachLesson) {
        Intent intent = new Intent(LessonActivity.this, FullScreenLessonPlayerActivity.class);
        intent.putExtra("videoType", eachLesson.getVideoType());
        intent.putExtra("videoUrl", eachLesson.getVideoUrl());
        startActivity(intent);
    }

    public void downloadAttachemt() {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mLesson.getAttachmentUrl()));
       // downloadManager.enqueue(request);
    }


}
