@file:Suppress("DEPRECATION")
package com.example.skilltracker.taskTests

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.skilltracker.MainActivity
import com.example.skilltracker.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MakeCompletedTaskUncompletedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun makeCompletedTaskUncompletedTest() {
        val floatingActionButton = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        1),
                    0),
                isDisplayed()))
        floatingActionButton.perform(click())

        val appCompatEditText = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.card_one),
                        childAtPosition(
                            withId(R.id.new_skill_set_fragment_constraint_layout),
                            0)),
                    2),
                isDisplayed()))
        appCompatEditText.perform(replaceText("s"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.new_skillSet_description_input),
                childAtPosition(
                    allOf(withId(R.id.card_two),
                        childAtPosition(
                            withId(R.id.new_skill_set_fragment_constraint_layout),
                            1)),
                    2),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("s"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(withId(R.id.add_new_skills_button), withText("Add New Skills"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    5),
                isDisplayed()))
        appCompatButton.perform(click())

        val floatingActionButton2 = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        1),
                    0),
                isDisplayed()))
        floatingActionButton2.perform(click())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.new_skill_name_input),
                childAtPosition(
                    allOf(withId(R.id.card_one_fragment_new_skill),
                        childAtPosition(
                            withId(R.id.new_skill_fragment_constraint_layout),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText3.perform(replaceText("a"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(withId(R.id.add_new_tasks_button), withText("Add New Tasks"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    4),
                isDisplayed()))
        appCompatButton2.perform(click())

        val floatingActionButton3 = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        1),
                    0),
                isDisplayed()))
        floatingActionButton3.perform(click())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.new_task_name_input),
                childAtPosition(
                    allOf(withId(R.id.cardone),
                        childAtPosition(
                            withId(R.id.new_task_fragment_constraint_layout),
                            1)),
                    1),
                isDisplayed()))
        appCompatEditText4.perform(replaceText("t"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(withId(R.id.create_new_task_button), withText("Create Task"),
                childAtPosition(
                    allOf(withId(R.id.new_task_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    5),
                isDisplayed()))
        appCompatButton3.perform(click())

        val recyclerView = onView(
            allOf(withId(R.id.task_list),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    0)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, longClick()))

        val appCompatButton4 = onView(
            allOf(withId(R.id.create_new_task_button),
                childAtPosition(
                    allOf(withId(R.id.new_task_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    5),
                isDisplayed()))
        appCompatButton4.perform(click())

        val recyclerView2 = onView(
            allOf(withId(R.id.task_list),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    0)))
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, longClick()))

        val appCompatCheckBox3 = onView(
            allOf(withId(R.id.task_completed_checkbox),
                childAtPosition(
                    allOf(withId(R.id.cardfour),
                        childAtPosition(
                            withId(R.id.new_task_fragment_constraint_layout),
                            0)),
                    0),
                isDisplayed()))
        appCompatCheckBox3.perform(click())

        val appCompatButton5 = onView(
            allOf(withId(R.id.create_new_task_button),
                childAtPosition(
                    allOf(withId(R.id.new_task_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    5),
                isDisplayed()))
        appCompatButton5.perform(click())

        val recyclerView3 = onView(
            allOf(withId(R.id.task_list),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    0)))
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, longClick()))

        val appCompatCheckBox6 = onView(
            allOf(withId(R.id.task_completed_checkbox),
                childAtPosition(
                    allOf(withId(R.id.cardfour),
                        childAtPosition(
                            withId(R.id.new_task_fragment_constraint_layout),
                            0)),
                    0),
                isDisplayed()))
        appCompatCheckBox6.perform(click())

        val appCompatButton6 = onView(
            allOf(withId(R.id.create_new_task_button),
                childAtPosition(
                    allOf(withId(R.id.new_task_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    5),
                isDisplayed()))
        appCompatButton6.perform(click())

        val recyclerView4 = onView(
            allOf(withId(R.id.task_list),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    0)))
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(0, longClick()))

        onView(withId(R.id.new_task_name_input)).check(ViewAssertions.matches(withSubstring("t")))
        onView(withId(R.id.new_task_name_input)).check(ViewAssertions.matches(withSubstring("t")))
        onView(withId(R.id.task_completed_checkbox)).check(ViewAssertions.matches(isNotChecked()))
        onView(withId(R.id.cardfive)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
