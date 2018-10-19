package com.example.ghifar.footballmatch.presenter.nextprev

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
 * Created by Alhudaghifari on 20:50 06/10/18
 */
class PrevMatchPresenterTest {

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
    fun getDataLastMatch() {
        var leagueId = "4328"
        service.getLast15EventsByLeagueId(leagueId).subscribe {
            repos ->
            repos.events.forEach(::println)
            Assert.assertEquals("unlocked", repos.events[0].strLocked)
            Assert.assertNotSame("576541", repos.events[0].idEvent)
        }
    }
}