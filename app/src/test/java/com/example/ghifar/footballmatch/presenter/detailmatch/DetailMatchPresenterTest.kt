package com.example.ghifar.footballmatch.presenter.detailmatch

import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.Constant
import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Alhudaghifari on 20:58 06/10/18
 */
class DetailMatchPresenterTest {

    private lateinit var service: ApiService

    @Before
    internal fun setUp() {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(ApiService::class.java)
    }

    @Test
    fun getDetailTeam() {
        var idTeam = "133612" // Manchester United
        service.getDetailTeamById(idTeam).subscribe {
            repos ->
            repos.teams.forEach(::println)
            assertEquals("Old Trafford", repos.teams[0].strStadium)
            assertEquals("Man United", repos.teams[0].strTeam)
            assertNotEquals("Sir Alex Ferguson", repos.teams[0].strManager)
        }
    }

    @Test
    fun getDetailEventById() {
        var idTeam = "576539"
        service.getDetailEventById(idTeam).subscribe {
            repos ->
            repos.events.forEach(::println)
            assertEquals("West Ham", repos.events[0].strHomeTeam)
            assertEquals("Man United", repos.events[0].strAwayTeam)
            assertNotEquals("Serie A", repos.events[0].strLeague)
        }
    }
}