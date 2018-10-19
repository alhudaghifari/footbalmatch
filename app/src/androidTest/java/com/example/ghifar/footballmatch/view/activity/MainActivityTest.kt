package com.example.ghifar.footballmatch.view.activity

import android.os.SystemClock
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.R.id.add_to_favorite
import com.example.ghifar.footballmatch.R.string.favorites
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import android.support.test.espresso.UiController
import android.support.test.orchestrator.junit.BundleJUnitUtils.getDescription
import android.support.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matcher
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.model.eventleaguemodel.EventLeagueModel
import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.view.fragment.PrevMatchFragment
import junit.framework.Assert
import org.hamcrest.Matchers.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Alhudaghifari on 22:09 06/10/18
 */

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var service: ApiService

    private lateinit var eventLeagueModel: EventLeagueModel

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

    fun getDataLastMatch() {
        var leagueId = "4328"
        setUp()
        service.getLast15EventsByLeagueId(leagueId).subscribe {
            repos ->
            eventLeagueModel = repos
        }
    }

    fun getDataNextMatch() {
        var leagueId = "4328"
        setUp()
        service.getNext15EventsByLeagueId(leagueId).subscribe {
            repos ->
            eventLeagueModel = repos
        }
    }

    @Test
    @Throws(Exception::class)
    fun testRecyclerViewBehaviour() {
        SystemClock.sleep(2000)
        onView(ViewMatchers.withId(R.id.recyclerListTeam))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.recyclerListTeam))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(11))
        onView(ViewMatchers.withId(R.id.recyclerListTeam)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(11, ViewActions.click()))

        sleep(2000)
        pressBack()

        onView(ViewMatchers.withId(R.id.navigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.navigation_next_match)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.recyclerListTeam))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.recyclerListTeam))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9))

        sleep(2000)
        onView(withId(R.id.swiperefresh))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))
    }

    @Test
    fun testFavoriteMatch() {
        // cek apakah ada list dengan daftar pertandingan Man United
        sleep(2000)
        getDataLastMatch()
        val team = eventLeagueModel.events[0].strHomeTeam
        onView(ViewMatchers.withText(team))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(team)).perform(ViewActions.click())

        // tambahkan pertandingan Man United ke favorit
        sleep(1000)
        onView(ViewMatchers.withId(R.id.add_to_favorite))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.add_to_favorite)).perform(ViewActions.click())
        onView(ViewMatchers.withText("Added to favorite"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()

        // masuk ke halaman next match
        onView(ViewMatchers.withId(R.id.navigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.navigation_next_match)).perform(ViewActions.click())

        // cek apakah ada pertandingan Tottenham
        sleep(2000)
        getDataNextMatch()
        val teamNext = eventLeagueModel.events[0].strHomeTeam
        onView(ViewMatchers.withText(teamNext))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(teamNext)).perform(ViewActions.click())

        // tambahkan pertandingan Tottenham ke favorit
        sleep(1000)
        onView(ViewMatchers.withId(R.id.add_to_favorite))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.add_to_favorite)).perform(ViewActions.click())
        onView(ViewMatchers.withText("Added to favorite"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()

        // masuk ke halaman favorit match
        onView(ViewMatchers.withId(R.id.navigation))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.navigation_favorites_match)).perform(ViewActions.click())

        // cek apakah ada pertandingan Tottenham di favorit
        sleep(2000)
        onView(ViewMatchers.withText(teamNext))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(teamNext)).perform(ViewActions.click())

        // hapus pertandingan Tottenham dari dari daftar favorit
        sleep(1000)
        onView(ViewMatchers.withId(R.id.add_to_favorite))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.add_to_favorite)).perform(ViewActions.click())
        onView(ViewMatchers.withText("Removed to favorite"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()
    }
}