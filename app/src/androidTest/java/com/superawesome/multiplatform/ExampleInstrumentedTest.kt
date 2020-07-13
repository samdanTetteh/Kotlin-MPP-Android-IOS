package com.superawesome.multiplatform

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.superawesome.multiplatform.ui.TodoAdapter
import com.superawesome.sharedcode.model.Todo
import org.hamcrest.CoreMatchers

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
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().getTargetContext()
        assertEquals("com.superawesome.multiplatform", appContext.packageName)
    }

    @Test
    fun clickListItem() {
        Espresso.onView(withId(R.id.todo_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<TodoAdapter.ViewHolder>(
                    5,
                    ViewActions.click()
                )
            )
    }

    @Test
    fun recyclerViewDisplayedCheck(){
        //this test will fail until the exact item count of the list is known and can be asserted
        Espresso.onView(withId(R.id.todo_list)).check(CustomAssertions.hasItemCount(202))
    }

    // Custome assertions class to check recycler list item count
    class CustomAssertions {
        companion object {
            fun hasItemCount(count: Int): ViewAssertion {
                return RecyclerViewItemCountAssertion(count)
            }
        }

        private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

            override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
                if (noViewFoundException != null) {
                    throw noViewFoundException
                }

                if (view !is RecyclerView) {
                    throw IllegalStateException("The asserted view is not RecyclerView")
                }

                if (view.adapter == null) {
                    throw IllegalStateException("No adapter is assigned to RecyclerView")
                }

                ViewMatchers.assertThat("RecyclerView item count", view.adapter?.itemCount, CoreMatchers.equalTo(count))
            }
        }
    }
}
