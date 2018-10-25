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
import com.example.ghifar.footballmatch.model.datateammodel.Team
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
 * Created by Alhudaghifari on 8:03 25/10/18
 */
@RunWith(AndroidJUnit4::class)
class TeamsFragmentTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var service: ApiService

    private var items: MutableList<Team> = mutableListOf()

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

    fun searchTeamsByName(teamName: String) {
        setUp()
        service.searchTeamsByName(teamName).subscribe {
            repos ->
            for (index in repos.teams) {
                if (index.strSport.equals("Soccer")) {
                    items.add(index)
                }
            }
        }
    }

    @Test
    fun testSearchTeams() {
        Thread.sleep(2000)
        val team = "Barcelona"
        val teamThatWeWantToAddToFav = "Barcelona B"
        // masuk ke page teams
        Espresso.onView(ViewMatchers.withId(R.id.navigation_teams))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.navigation_teams)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.menu_search))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_search)).perform(ViewActions.click())

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etSearch))
                .perform(ViewActions.typeText(team))

        Thread.sleep(2000)
        searchTeamsByName(team)
        Assert.assertNotEquals("0", items.size)
        Espresso.onView(ViewMatchers.withText(teamThatWeWantToAddToFav))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(teamThatWeWantToAddToFav)).perform(ViewActions.click())

        // tambahkan team ke favorit
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.add_to_favorite))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_to_favorite)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Added to favorite"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(2000)

        Espresso.pressBack()
        Espresso.pressBack()
    }

}