@file:Suppress("DEPRECATION")
package com.example.skilltracker.skillTests

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class CreateSkillAndEnsureNameProvidedTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun createSkillAndEnsureNameProvidedTest() {
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

        val appCompatButton2 = onView(
            allOf(withId(R.id.create_new_skill_button), withText("Create Skill"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    2),
                isDisplayed()))
        appCompatButton2.perform(click())

        val textView = onView(
            allOf(withId(R.id.new_skill_missing_name), withText("Name can not be blank"),
                childAtPosition(
                    allOf(withId(R.id.card_one_fragment_new_skill),
                        childAtPosition(
                            withId(R.id.new_skill_fragment_constraint_layout),
                            0)),
                    2),
                isDisplayed()))
        textView.check(matches(isDisplayed()))

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

        val appCompatButton3 = onView(
            allOf(withId(R.id.create_new_skill_button), withText("Create Skill"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    2),
                isDisplayed()))
        appCompatButton3.perform(click())

        val frameLayout = onView(
            allOf(childAtPosition(
                childAtPosition(
                    withId(R.id.skill_list),
                    0),
                0),
                isDisplayed()))
        frameLayout.check(matches(isDisplayed()))
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
