package com.bhirschberger.a20210909_bretthirschberger_nycschools.Activities;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import com.bhirschberger.a20210909_bretthirschberger_nycschools.R;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.models.School;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.models.SchoolData;


import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest extends TestCase {
    @Rule
    public ActivityScenarioRule activityRule = new ActivityScenarioRule(MainActivity.class);

    final int ITEM_IN_TEST = 4;
    final School SCHOOL_IN_TEST = SchoolData.getInstance().getSchool(ITEM_IN_TEST);

    @Test
    public void testDetailViewOverviewParagraph() {
        onView(withId(R.id.schools_list)).perform(actionOnItemAtPosition(ITEM_IN_TEST,click()));
        onView(withIndex(withId(R.id.infoButton),ITEM_IN_TEST)).perform(click());
        onView(withId(R.id.overviewText)).check(matches(withText(SCHOOL_IN_TEST.getOverviewParagraph())));
    }

    @Test
    public void testDetailViewTitle() {
        onView(withId(R.id.schools_list)).perform(actionOnItemAtPosition(ITEM_IN_TEST,click()));
        onView(withIndex(withId(R.id.infoButton),ITEM_IN_TEST)).perform(click());
        onView(withId(R.id.schoolNameView)).check(matches(withText(SCHOOL_IN_TEST.getName())));
    }

    // assures proper index is always clicked and eliminates AmbiguousViewMatcherExceptions
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
}