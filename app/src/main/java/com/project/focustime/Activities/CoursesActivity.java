package com.project.focustime.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.focustime.DBOperations;
import com.project.focustime.Models.*;
import com.project.focustime.R;
import com.project.focustime.Adapters.CoursesAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.focustime.connectServer;

import java.sql.Connection;
import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity{
    //vars
    private static final String TAG = "Course-List";
    private RecyclerView recyclerViewForFilteredCourses = null;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private TextView numberOfFilteredCourses;
    private Spinner categorySpinner;
    private Spinner priceSpinner;
    private Spinner difficultyLevelSpinner;
    private Spinner languageSpinner;
    private Spinner ratingSpinner;

    private String selectedCategory;
    private String selectedPrice;
    private String selectedDifficultyLevel;
    private String selectedLanguage;
    private int selectedRating;

    Button showSearchBoxButton;
    Button hideSearchBoxButton;
    EditText searchStringInputField;
    Button backButton;
    ImageView applicationLogo;
    FloatingActionButton filterButton;
    View bottomSheetView;
    ImageButton closeFilterViewButton;
    Button filterApplyButton;
    Button filterResetButton;
    ArrayAdapter<Category> categoryAdapter;
    ArrayAdapter<Price> priceAdapter;
    ArrayAdapter<DifficultyLevel> difficultyLevelArrayAdapter;
    ArrayAdapter<Language> languageArrayAdapter;
    ArrayAdapter<Rating> ratingArrayAdapter;
    Editable searchString;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Course> caCourses=new ArrayList<>();
    ArrayList<Course> searchedCourse =new ArrayList<>();
    private BottomSheetBehavior bottomSheetBehavior;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        init();
        initProgressBar();
        getCourseObject();

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:

                    case BottomSheetBehavior.STATE_EXPANDED:
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        getCategories();
        getPrice();
        getDifficultyLevel();
        //getLanguage();
        getRating();

        closeFilterViewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        filterApplyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                filterCourse();
            }
        });

        filterResetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                resetFilter();
            }
        });
    }

    private void init() {
        showSearchBoxButton = findViewById(R.id.showSearchBarButton);
        hideSearchBoxButton = findViewById(R.id.hideSearchBarButton);
        applicationLogo = findViewById(R.id.applicationLogo);
        backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);

        searchStringInputField = findViewById(R.id.searchStringInputField);
        filterButton = findViewById(R.id.floatingFilterButton);
        bottomSheetView = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        closeFilterViewButton = findViewById(R.id.filterViewCloseButton);
        filterApplyButton = findViewById(R.id.filterApplyButton);
        filterResetButton = findViewById(R.id.filterResetButton);

        searchStringInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchCourse();
                    return true;
                }
                return false;
            }
        });
    }
    public void handleBackButton(View view) {
        CoursesActivity.super.onBackPressed();
    }
    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }
    private void getCourseObject() {
        recyclerViewForFilteredCourses  = findViewById(R.id.recyclerViewForFilteredCourses);
        mCourses = (ArrayList<Course>) getIntent().getSerializableExtra("Course");

        numberOfFilteredCourses = findViewById(R.id.filterResultTitle);
        numberOfFilteredCourses.setText("Showing "+mCourses.size()+" Courses");

        reloadCourses(mCourses);

    }

    private void reloadCourses(ArrayList<Course> mCourses) {
        numberOfFilteredCourses = findViewById(R.id.filterResultTitle);
        numberOfFilteredCourses.setText("Showing "+mCourses.size()+" Courses");

        initFilteredCourseRecyclerView(mCourses);
    }

    private void initFilteredCourseRecyclerView(ArrayList<Course> mCourses) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        CoursesAdapter adapter = new CoursesAdapter(CoursesActivity.this, mCourses);
        recyclerViewForFilteredCourses.setAdapter(adapter);
        recyclerViewForFilteredCourses.setLayoutManager(layoutManager);

    }


    public void searchCourse() {
        searchString = searchStringInputField.getText();
        //filterCourse();
        getCourseBySearchString(searchString);
    }

    private void getCourseBySearchString(Editable searchString) {

        String s = searchString.toString();
        searchedCourse=db.getCourses(conn);
        ArrayList<Course> sCourse = new ArrayList<>();
        for(Course c: searchedCourse){
            if(c.getTitle().toLowerCase().trim().contains(s)){
                sCourse.add(c);
            }
        }
        Intent intent = new Intent(CoursesActivity.this, CoursesActivity.class);
        intent.putExtra("Course", sCourse);
        startActivity(intent);


        searchedCourse.clear();
        sCourse.clear();

        searchStringInputField.setVisibility(View.GONE);
        hideSearchBoxButton.setVisibility(View.GONE);
        showSearchBoxButton.setVisibility(View.VISIBLE);
        applicationLogo.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchStringInputField.getWindowToken(), 0);
    }
    public void showSearchBox(View view) {
        searchStringInputField.setVisibility(View.VISIBLE);
        searchStringInputField.setFocusableInTouchMode(true);
        searchStringInputField.requestFocus();
        applicationLogo.setVisibility(View.GONE);
        showSearchBoxButton.setVisibility(View.GONE);
        hideSearchBoxButton.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchStringInputField, InputMethodManager.SHOW_IMPLICIT);
    }
    public void hideSearchBox(View view) {
        searchStringInputField.setVisibility(View.GONE);
        applicationLogo.setVisibility(View.VISIBLE);
        showSearchBoxButton.setVisibility(View.VISIBLE);
        hideSearchBoxButton.setVisibility(View.GONE);
        searchStringInputField.getText().clear();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchStringInputField.getWindowToken(),0);
    }

    public void handleFilterButton(View view) {
        switch (bottomSheetBehavior.getState()){
            case BottomSheetBehavior.STATE_COLLAPSED:
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
                bottomSheetView.bringToFront();
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    private void initializeCategorySpinner(final ArrayList<Category> mCategory) {
        categorySpinner = findViewById(R.id.filterCategorySpinner);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedCategory(mCategory.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializePriceSpinner(final ArrayList<Price> mPrice){
        priceSpinner = findViewById(R.id.filterPriceSpinner);
        priceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPrice);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);
        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedPrice(mPrice.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void initializeDifficultySpinner(final ArrayList<DifficultyLevel> mDifficultyLevel){
        difficultyLevelSpinner = findViewById(R.id.filterLevelSpinner);
        difficultyLevelArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDifficultyLevel);
        difficultyLevelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyLevelSpinner.setAdapter(difficultyLevelArrayAdapter);
        difficultyLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedDifficultyLevel(mDifficultyLevel.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*private void initializeLanguageSpinner(final ArrayList<Language> mLanguage){
        //languageSpinner = findViewById(R.id.filterLanguageSpinner);
        languageArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLanguage);
        languageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageArrayAdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedLanguage(mLanguage.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

     */

    private void initializeRatingSpinner(final ArrayList<Rating> mRating){
        ratingSpinner = findViewById(R.id.filterRatingSpinner);
        ratingArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mRating);
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(ratingArrayAdapter);
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSelectedRating(mRating.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCategories() {
        try {
            categories = db.getCategory(conn);

            categories.add(0,new Category(0,"All","",0));
            initializeCategorySpinner(categories);

        }catch (Exception e){

            Log.e("HATA",e.toString());
        }
    }
    private void getPrice() {
        final ArrayList<Price> mPrice = new ArrayList<>();
        mPrice.add(new Price(0, "all", "All"));
        mPrice.add(new Price(1, "free", "Free"));
        mPrice.add(new Price(2, "paid", "Paid"));
        initializePriceSpinner(mPrice);
    }

    private void getDifficultyLevel() {
        final ArrayList<DifficultyLevel> mDifficultyLevel = new ArrayList<>();
        mDifficultyLevel.add(new DifficultyLevel(0, "all", "All"));
        mDifficultyLevel.add(new DifficultyLevel(1, "beginner", "Beginner"));
        mDifficultyLevel.add(new DifficultyLevel(2, "intermediate", "Intermediate"));
        mDifficultyLevel.add(new DifficultyLevel(3, "expert", "Expert"));
        initializeDifficultySpinner(mDifficultyLevel);
    }
    private void getLanguage() {
        final ArrayList<Language> language = new ArrayList<>();
        language.add(new Language(0,"All","All"));
        language.add(new Language(1,"Turkish","Turkish"));
        language.add((new Language(2,"English","English")));
        //initializeLanguageSpinner(language);
    }

    private void getRating() {
        final ArrayList<Rating> mRating = new ArrayList<>();

        mRating.add(new Rating(0, 0, "All"));
        mRating.add(new Rating(1, 1, "⭐"));
        mRating.add(new Rating(2, 2, "⭐⭐"));
        mRating.add(new Rating(3, 3, "⭐⭐⭐"));
        mRating.add(new Rating(4, 4, "⭐⭐⭐⭐"));
        mRating.add(new Rating(5, 5, "⭐⭐⭐⭐⭐"));
        initializeRatingSpinner(mRating);
    }

    public void getSelectedCategory(Category category) {
        if (category.getId() == 0){
            this.selectedCategory = "all";
        }else{
            this.selectedCategory = category.getTitle();
        }
    }
    public void getSelectedPrice(Price price) {

        this.selectedPrice = price.getValue();
    }
    public void getSelectedDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.selectedDifficultyLevel = difficultyLevel.getValue();
    }
    public void getSelectedLanguage(Language language) {
        this.selectedLanguage = language.getValue();
    }
    public void getSelectedRating(Rating rating) {
        if (rating.getValue() == 0){
            this.selectedRating = 0;
        }else{
            this.selectedRating = rating.getValue();
        }
    }


    private void filterCourse() {
        String searchedString = searchString+"";
        caCourses.clear();
        caCourses = db.getCourses(conn);

        ArrayList<Course> remove = new ArrayList<>();

        if (selectedCategory.equals("all") && selectedPrice.equals("all") && selectedDifficultyLevel.equals("all")
                && selectedRating == 0) {

            reloadCourses(caCourses);
        }

        if (!selectedCategory.equals("all")) {
            for (Course c : caCourses) {
                if (!selectedCategory.equals(c.getCategory())) {
                    remove.add(c);
                }
            }
            caCourses.removeAll(remove);
            remove.clear();

        }

        if (!selectedPrice.equals("all")) {

            if (selectedPrice.equals("free")) {

                for (Course c : caCourses) {
                    if (Double.parseDouble(c.getPrice()) != 0) {
                        remove.add(c);
                    }
                }
                caCourses.removeAll(remove);
                remove.clear();
            } else {
                for (Course c : caCourses) {
                    if (Double.parseDouble(c.getPrice()) == 0) {
                        remove.add(c);
                    }
                }
                caCourses.removeAll(remove);
                remove.clear();
            }

        }

        if(!selectedDifficultyLevel.equals("all")){
            if(selectedDifficultyLevel.equals("beginner")){
                selectedDifficultyLevel="1";
                for (Course c : caCourses) {
                    if (!selectedDifficultyLevel.equals(c.getLevel())) {
                        remove.add(c);
                    }
                }
                caCourses.removeAll(remove);
                remove.clear();
                selectedDifficultyLevel="beginner";
            }
            else if(selectedDifficultyLevel.equals("intermediate")){
                selectedDifficultyLevel="2";
                for (Course c : caCourses) {
                    if (!selectedDifficultyLevel.equals(c.getLevel())) {
                        remove.add(c);
                    }
                }
                caCourses.removeAll(remove);
                remove.clear();
                selectedDifficultyLevel="intermediate";
            }
            else{
                selectedDifficultyLevel="3";
                for (Course c : caCourses) {
                    if (!selectedDifficultyLevel.equals(c.getLevel())) {
                        remove.add(c);
                    }
                }
                caCourses.removeAll(remove);
                remove.clear();
                selectedDifficultyLevel="advanced";
            }

        }

        if (selectedRating!=0) {
            for (Course c : caCourses) {
                if (selectedRating!=c.getRating()) {
                    remove.add(c);
                }
            }
            caCourses.removeAll(remove);
            remove.clear();
        }

        reloadCourses(caCourses);
    }

    private void resetFilter(){
        categorySpinner.setSelection(0, true);
        priceSpinner.setSelection(0, true);
        difficultyLevelSpinner.setSelection(0, true);
        //languageSpinner.setSelection(0, true);
        ratingSpinner.setSelection(0, true);
    }
}
