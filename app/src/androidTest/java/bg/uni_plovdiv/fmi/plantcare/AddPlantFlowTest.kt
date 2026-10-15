package bg.uni_plovdiv.fmi.plantcare

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddPlantFlowTest {

    @Test
    fun addPlant_appearsInList() {
        // Името започва с "AAA", за да е първо в азбучно подредения списък.
        val name = "AAA Test ${System.currentTimeMillis()}"
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.fabAdd)).perform(click())
            onView(withId(R.id.etName)).perform(scrollTo(), replaceText(name))
            closeSoftKeyboard()
            onView(withId(R.id.btnSave)).perform(scrollTo(), click())
            onView(withText(name)).check(matches(isDisplayed()))
        }
    }
}