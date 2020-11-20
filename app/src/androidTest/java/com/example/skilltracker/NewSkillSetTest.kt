package com.example.skilltracker


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test class to test creating a new skill set. Tests successfully creating a new skill set,
 *  trying to create a skill set without a name and/or description, tests that the FAB
 *  is not visible while viewing the NewSkillSet Fragment, and tests that we are brought
 *  back to the SkillSetFragment with the FAB visible after creating a new skill set
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NewSkillSetTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun newSkillSetTest() {
        // Navigate to the NewSkillSet Fragment
        val floatingActionButton = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        floatingActionButton.perform(click())

        val floatingActionButton2 = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        floatingActionButton2.perform(click())

        // Ensure the FAB is invisible
        onView(withId(R.id.fab)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))

        // Fill out the new skill set information
        val appCompatEditText = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText.perform(replaceText("Skill Set"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.new_skillSet_description_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    3),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("Desc"), closeSoftKeyboard())

        // Successfully create a new skill set
        val appCompatButton = onView(
            allOf(withId(R.id.create_new_skillSet_button), withText("Create Skill Set"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    4),
                isDisplayed()))
        appCompatButton.perform(click())

        // Navigate back to the NewSkillSet Fragment
        val floatingActionButton3 = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        floatingActionButton3.perform(click())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText3.perform(replaceText("S Set"), closeSoftKeyboard())

        // Try creating new skill set with only a name, no desription
        val appCompatButton2 = onView(
            allOf(withId(R.id.create_new_skillSet_button), withText("Create Skill Set"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    4),
                isDisplayed()))
        appCompatButton2.perform(click())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.new_skillSet_name_input), withText("S Set"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText4.perform(replaceText(""))

        val appCompatEditText5 = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText5.perform(closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(withId(R.id.new_skillSet_description_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    3),
                isDisplayed()))
        appCompatEditText6.perform(replaceText("Desc"), closeSoftKeyboard())

        // Try creating a new skill set with just a description, no name
        val appCompatButton3 = onView(
            allOf(withId(R.id.create_new_skillSet_button), withText("Create Skill Set"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    4),
                isDisplayed()))
        appCompatButton3.perform(click())

        val appCompatEditText7 = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    1),
                isDisplayed()))
        appCompatEditText7.perform(replaceText("S Set"), closeSoftKeyboard())

        // Successfully create a new skill set
        val appCompatButton4 = onView(
            allOf(withId(R.id.create_new_skillSet_button), withText("Create Skill Set"),
                childAtPosition(
                    allOf(withId(R.id.new_skill_set_fragment_constraint_layout),
                        childAtPosition(
                            withId(R.id.myNavHostFragment),
                            0)),
                    4),
                isDisplayed()))
        appCompatButton4.perform(click())

        // Ensure we are brought back to the SkillSet Fragment and the FAB is visible
        onView(withId(R.id.fab)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.skill_set_list)).check(matches(isDisplayed()))
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
