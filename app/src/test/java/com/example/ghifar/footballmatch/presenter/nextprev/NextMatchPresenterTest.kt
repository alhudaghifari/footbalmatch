package com.example.ghifar.footballmatch.presenter.nextprev

import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.Constant
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Alhudaghifari on 20:10 06/10/18
 *
 */
class NextMatchPresenterTest {

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
    fun getNextMatch() {
        var leagueId = "4328"
        service.getNext15EventsByLeagueId(leagueId).subscribe {
            repos ->
            repos.events.forEach(::println)
            assertEquals("unlocked",repos.events[0].strLocked)
            assertEquals("576550", repos.events[0].idEvent)
            assertNotSame("12345", repos.events[1].idAwayTeam)
        }
    }
}