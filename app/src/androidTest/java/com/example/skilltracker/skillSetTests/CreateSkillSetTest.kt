@file:Suppress("DEPRECATION")

package com.example.skilltracker.skillSetTests


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
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

@Suppress("DEPRECATION")
@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateSkillSetTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addNewSkillSetTest() {
        val floatingActionButton = onView(
            allOf(withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        1),
                    0),
                isDisplayed()))
        floatingActionButton.perform(click())

        onView(withId(R.id.card_four)).check(ViewAssertions.matches(withEffectiveVisibility(Visibility.INVISIBLE)))

        val appCompatEditText = onView(
            allOf(withId(R.id.new_skillSet_name_input),
                childAtPosition(
                    allOf(withId(R.id.card_one),
                        childAtPosition(
                            withId(R.id.new_skill_set_fragment_constraint_layout),
                            0)),
                    2),
                isDisplayed()))
        appCompatEditText.perform(replaceText("n"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.new_skillSet_description_input),
                childAtPosition(
                    allOf(withId(R.id.card_two),
                        childAtPosition(
                            withId(R.id.new_skill_set_fragment_constraint_layout),
                            1)),
                    2),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("n"), closeSoftKeyboard())

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

        // Ensure we are brought back to the SkillSet Fragment
        onView(withId(R.id.skill_set_list)).check(ViewAssertions.matches(isDisplayed()))
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
