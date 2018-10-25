package com.example.ghifar.footballmatch.view.fragment

import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.search.EventSearchModel
import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.view.activity.MainActivity
import org.hamcrest.Matcher
import org.junit.Assert

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Alhudaghifari on 7:26 25/10/18
 */
@RunWith(AndroidJUnit4::class)
class MatchFragmentTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var service: ApiService

    private lateinit var eventSearchModel: EventSearchModel

    fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }

    fun setUp() {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(ApiService::class.java)
    }

    fun searchEventByEventName(eventName: String) {
        setUp()
        service.searchEventByEventName(eventName).subscribe {
            repos ->
            eventSearchModel = repos
        }
    }

    @Test
    fun testSearch() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.menu_search))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_search)).perform(ViewActions.click())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
                .perform(ViewActions.typeText("Chelsea"))


        Thread.sleep(2000)
        searchEventByEventName("Chelsea")
        Assert.assertNotEquals("2", eventSearchModel.events.size)

        Thread.sleep(2000)
        Espresso.pressBack()
        Espresso.pressBack()

    }
}