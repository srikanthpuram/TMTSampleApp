package com.srikanthpuram.tmtsampleapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.srikanthpuram.tmtsampleapp.ui.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.srikanthpuram.tmtsampleapp", appContext.packageName)
    }

    @Test
    fun isListViewDisplayed() {
        onView(withId(R.id.progressBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rvTmtCards)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}
