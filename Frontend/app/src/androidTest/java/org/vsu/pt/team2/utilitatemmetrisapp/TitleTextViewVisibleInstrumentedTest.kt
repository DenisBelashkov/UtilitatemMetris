package org.vsu.pt.team2.utilitatemmetrisapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.vsu.pt.team2.utilitatemmetrisapp.ui.main.MainActivity


@RunWith(AndroidJUnit4::class)
class TitleTextViewVisibleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun titleTvVisible() {
        onView(withId(R.id.toolbar_title_textview)).check(matches(isDisplayed()))
    }
}