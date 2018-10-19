package com.example.ghifar.footballmatch.presenter

import com.example.ghifar.footballmatch.model.datateammodel.DetailTeamModel
import com.example.ghifar.footballmatch.model.eventleaguemodel.DetailEventModel
import com.example.ghifar.footballmatch.model.eventleaguemodel.EventLeagueModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Alhudaghifari on 11:37 17/09/18
 *
 */
interface ApiService {

    @GET("lookupevent.php")
    fun getDetailEventById(@Query("id") id: String): Observable<DetailEventModel>

    @GET("eventspastleague.php")
    fun getLast15EventsByLeagueId(@Query("id") id: String): Observable<EventLeagueModel>

    @GET("eventsnextleague.php")
    fun getNext15EventsByLeagueId(@Query("id") id: String): Observable<EventLeagueModel>

    @GET("lookupteam.php")
    fun getDetailTeamById(@Query("id") id: String): Observable<DetailTeamModel>

    companion object {
        fun create(): ApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.BASE_URL)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}