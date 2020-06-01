package com.project.focustime.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.project.focustime.Adapters.CourseCurriculumLessonAdapter;
import com.project.focustime.Adapters.CourseCurriculumSectionAdapter;
import com.project.focustime.Adapters.ViewPagerAdapter;
import com.project.focustime.DBOperations;
import com.project.focustime.Models.Lesson;
import com.project.focustime.Models.MyCourse;
import com.project.focustime.Models.Course;
import com.project.focustime.Models.CourseDetails;
import com.project.focustime.Models.Section;
import com.project.focustime.R;
import com.project.focustime.Utils.BounceInterpolator;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.tabs.TabLayout;
import com.project.focustime.connectServer;

import java.sql.Connection;
import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    private Course mCourse;
    private TextView mcourseTitle;
    private TextView mNumberOfEnrolledStudentNumber;
    private TextView mNumericRating;
    private TextView mTotalNumberOfRatingByUsers;
    private TextView mCoursePrice;
    private ImageView mCourseBanner;
    private RatingBar mStarRating;
    private ImageButton mPlayCoursePreview;
    private ImageButton mWishlistThisCourse;
    private ImageButton mShareThisCourse;
    private ImageButton mBackToCourseList;
    private Button mBuyNowButton;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private ProgressBar progressBar;
    private ArrayList<CourseDetails> mEachCourseDetail = new ArrayList<>();
    private ArrayList<Section> mSections = new ArrayList<>();
    private RecyclerView mSectionRecyclerView;

    ArrayList<Lesson> mLessons = new ArrayList<>();
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    ArrayList<MyCourse> myCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        initElements();

        initProgressBar();
        getCourseObject();
        mCourse.setNumberOfEnrollment(db.getEnrollment(conn,mCourse.getId()));
        mNumberOfEnrolledStudentNumber.setText(Integer.toString(mCourse.getNumberOfEnrollment()));
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId", 0);
        if(db.isAdded(conn,mCourse.getId(),userID)){
            mWishlistThisCourse.setImageResource(R.drawable.wishlist_filled);
        }
        if (db.isMyCourse(conn, mCourse.getId(), userID)) {
            mBuyNowButton.setText("Show Lessons");
        }
    }

    private void initElements() {
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        final int userID = preferences.getInt("userId", 0);

        mBackToCourseList = findViewById(R.id.backToAllCoursesList);
        mcourseTitle = findViewById(R.id.courseTitle);
        mNumberOfEnrolledStudentNumber = findViewById(R.id.numberOfEnrolledStudentNumber);
        mNumericRating = findViewById(R.id.numericRating);
        mTotalNumberOfRatingByUsers = findViewById(R.id.totalNumberOfRatingByUsers);
        mCoursePrice = findViewById(R.id.coursePrice);
        mCourseBanner = findViewById(R.id.courseBannerImage);
        mStarRating = findViewById(R.id.starRating);
        mPlayCoursePreview = findViewById(R.id.playCoursePreview);
        mWishlistThisCourse = findViewById(R.id.wishlistThisCourse);
        mBuyNowButton = findViewById(R.id.buyThisCourseButton);
        mShareThisCourse = findViewById(R.id.shareThisCourse);
        mShareThisCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareThisCourseOnSocialMedia(view);
            }
        });
        mSectionRecyclerView = findViewById(R.id.courseCurriculumSectionRecyclerView);

    }

    private void setupViewPager(CourseDetails courseDetails) {
        mViewPager = findViewById(R.id.courseViewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), courseDetails);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.courseViewPagerTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void initProgressBar() {
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }
    public void handleBackButton(View view) {
        CourseDetailsActivity.super.onBackPressed();
    }

    private void getCourseObject() {
        if (getIntent().hasExtra("courseId")) {
            int courseId = (int) getIntent().getSerializableExtra("courseId");
            getCourseDetails(courseId);
        } else {
            mCourse = (Course) getIntent().getSerializableExtra("Course");
            mcourseTitle.setText(mCourse.getTitle());
            mStarRating.setRating((float)mCourse.getRating());
//            mTotalNumberOfRatingByUsers.setText("( "+mCourse.getTotalNumberRating()+" )");
           // mNumberOfEnrolledStudentNumber.setText(Integer.toString(mCourse.getNumberOfEnrollment()));
            Glide.with(this)
                    .asBitmap()
                    .load(mCourse.getThumbnail())
                    .into(mCourseBanner);

            mPlayCoursePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playPreview();
                }
            });
            if(mCourse.getPrice().equals("0")){
                mCoursePrice.setText("FREE");
            }else{
                mCoursePrice.setText(mCourse.getPrice()+" $");
            }

            mNumericRating.setText(Float.toString((float)mCourse.getRating()));
            getCourseDetails(mCourse.getId());
        }

    }
    
    private void playPreview() {
        Intent intent = new Intent(getApplicationContext(), PopUpActivity.class);
        intent.putExtra("Course", mCourse);
        startActivity(intent);
    }

    private void shareThisCourseOnSocialMedia(View view) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareText = mCourse.getUrl();
        myIntent.putExtra(Intent.EXTRA_TEXT, "https://www.thefocustime.com/course-detail.php?url="+shareText);
        startActivity(Intent.createChooser(myIntent, "Share using"));
    }

    private void getCourseDetails(Integer courseId) {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        final int authToken = preferences.getInt("userValidity", 0);

        try{
            mEachCourseDetail.add(new CourseDetails(mCourse.getId(),mCourse.getDescription(),mCourse.getRequirements(),mCourse.getOutcomes(),false));
            setupViewPager(mEachCourseDetail.get(0));

            mLessons = db.getLesson(conn,courseId);

            mSections.add(new Section(1,mcourseTitle.getText().toString(),mLessons,0,"0",0,0));

            // DAHA SONRA BURAYA WISHLIIST YAP
            progressBar.setVisibility(View.INVISIBLE);

            mCourse.setFirstLesson(mLessons.get(0).getVideoUrl());
            initSectionRecyclerView();

        }catch (Exception e){
            Log.d("HATALI İŞLEM",e.toString());
        }

    }

    private void initSectionRecyclerView() {
        CourseCurriculumSectionAdapter adapter = new CourseCurriculumSectionAdapter(getApplicationContext(),mSections );
        mSectionRecyclerView.setAdapter(adapter);
        mSectionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setHeight(mSections.size(), mSectionRecyclerView);
    }

    private void setHeight(int numberOfItems, RecyclerView mRecyclerView) {
        int pixels = Helpers.convertDpToPixel((numberOfItems * 40) + 10); // numberOfItems is the number of categories and the 90 is each items height with the margin of top and bottom. Extra 10 dp for readability
        ViewGroup.LayoutParams params1 = mRecyclerView.getLayoutParams();
        mRecyclerView.setMinimumHeight(pixels);
        mRecyclerView.requestLayout();
    }

    public void handleWishListButton(View view) {
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        int authToken = preferences.getInt("userValidity", 0);

        if (authToken==0){
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CourseDetailsActivity.this, SignInActivity.class);
            startActivity(intent);
        }else{
            toggleWishListItem();
        }
    }


    private void toggleWishListItem() {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        final int userID = preferences.getInt("userId", 0);

        boolean check = db.isAdded(conn,mCourse.getId(),userID);

        System.out.println(check);

        boolean isMyCourse = db.isMyCourse(conn,mCourse.getId(),userID);

        if(isMyCourse==true){
            Toast.makeText(getApplicationContext(),"You Can't Add This Course to Wishlist\nYou Already Got This Course",Toast.LENGTH_SHORT).show();
        }else{
            if(check==false){
                db.addWishlist(conn,mCourse.getId(),userID);
                mWishlistThisCourse.setImageResource(R.drawable.wishlist_filled);
                Toast.makeText(getApplicationContext(),"Added Wishlist Successfully",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Already Added to Wishlist",Toast.LENGTH_SHORT).show();

            }
        }


        progressBar.setVisibility(View.INVISIBLE);

    }


    public void handleBuyNow(View view) {
        /*Kullanıcı giriş yapmamış ise basınca login sayfasına yönlendir.
        Giriş yapmış ise daha önce bu kursu alıp almadığını kontrol et.
        Almış ise butonda already got this yazsın. Basınca lessonactivitye geçsin.
         Almamış ise basınca veritabanına enrollment kaydet ve kullanıcının kurslarına eklensin.
        MyCourse'a kursun eklendiğinden emin ol.
         */
        SharedPreferences preferences = getSharedPreferences(Helpers.SHARED_PREF, 0);
        final int authToken = preferences.getInt("userValidity", 0);
        final int userID = preferences.getInt("userId", 0);
       // final int statusToken = preferences.getInt("userStatus",0); ONAY MAİLİ YOLLAMADIĞIM İÇİN HENÜZ KULLANMIYORUM.

        if (authToken==0){
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CourseDetailsActivity.this, SignInActivity.class);
            startActivity(intent);
        }else{
            /*if(statusToken==0){  ONAY MAİLİ ATTIKTAN SONRA BU ŞEKİLDE OLACAK KOD. SONRADAN AÇ
                Toast.makeText(this, "You Should Verify Your Account", Toast.LENGTH_SHORT).show();
            }else{

            }
             */
            if(mBuyNowButton.getText().equals("Buy Now")){
                db.enroll(conn,userID,mCourse.getId());
                if(db.isAdded(conn,mCourse.getId(),userID)==true){
                    db.deleteWishlist(conn,mCourse.getId(),userID);
                    Toast.makeText(this, "Enrolled Successfully and Removed from Wishlist", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Enrolled Successfully", Toast.LENGTH_SHORT).show();
                }
                mBuyNowButton.setText("Show Lessons");
                mNumberOfEnrolledStudentNumber.setText((Integer.parseInt(mNumberOfEnrolledStudentNumber.getText().toString())+1)+"");
            }
            else{

                MyCourse myCourse = new MyCourse(mCourse.getId(),mCourse.getTitle(),mCourse.getThumbnail(),mCourse.getPrice(),
                        mCourse.getInstructor(),mCourse.getRating(),mCourse.getTotalNumberRating(),mCourse.getNumberOfEnrollment(),
                        0,0,0,mCourse.getUrl());
                Intent intent = new Intent(CourseDetailsActivity.this, LessonActivity.class);
                intent.putExtra("Course", myCourse);
                startActivity(intent);
            }
        }
    }
}
