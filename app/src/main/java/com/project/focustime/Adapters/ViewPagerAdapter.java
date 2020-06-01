package com.project.focustime.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.focustime.Fragments.CourseDescriptionFragment;
import com.project.focustime.Fragments.CourseOutcomeFragment;
import com.project.focustime.Fragments.CourseRequirementFragment;
import com.project.focustime.Models.CourseDetails;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private CourseDetails mCourseDetails;
    public ViewPagerAdapter(FragmentManager fm, CourseDetails courseDetails) {
        super(fm);
        this.mCourseDetails = courseDetails;
    }
    private int mCurrentPosition;
    @Override
    public Fragment getItem(int position) {
        mCurrentPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CourseDescriptionFragment(mCourseDetails);
                break;
            case 1:
                fragment = new CourseOutcomeFragment(mCourseDetails);
                break;
            case 2:
                fragment = new CourseRequirementFragment(mCourseDetails);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = null;
        switch (position){
            case 0:
                tabTitle = "Description";
                break;
            case 1:
                tabTitle = "Outcomes";
                break;
            case 2:
                tabTitle = "Requirements";
                break;
        }
        return tabTitle;
    }
}
