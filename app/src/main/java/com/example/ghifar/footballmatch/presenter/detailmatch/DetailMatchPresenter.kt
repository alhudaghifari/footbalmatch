package com.example.ghifar.footballmatch.presenter.detailmatch

import android.util.Log
import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 6:37 25/09/18
 *
 */
class DetailMatchPresenter(private val view: DetailMatchInterface) {

    private val TAG = DetailMatchPresenter::class.java.simpleName

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getDetailHomeTeam(teamName: String) {
        Log.d(TAG, "getDetailHomeTeam")
        view.showLoading()
        disposable = footballApiService.getDetailTeamById(teamName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d(TAG, "getDetailHomeTeam result")
                            view.showTeamListHome(result.teams)
                        },
                        { error ->
                            view.showError(error.message + " 1")
                            view.hideLoading()
                        }
                )
    }

    fun getDetailAwayTeam(teamName: String) {
        Log.d(TAG, "getDetailAwayTeam")
        view.showLoading()
        disposable = footballApiService.getDetailTeamById(teamName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d(TAG, "getDetailAwayTeam result")
                            view.showTeamListAway(result.teams)
                        },
                        { error ->
                            view.showError(error.message + " 2")
                            view.hideLoading()
                        }
                )
    }

    fun getDetailEventById(leagueId: String) {
        Log.d(TAG, "getDataLastMatch")
        view.showLoading()
        disposable = footballApiService.getDetailEventById(leagueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d(TAG, "getDataLastMatch result. size : " + result.events.size)
                            view.showDetailEvent(result.events[0])
                            view.hideLoading()
                        },
                        { error ->
                            view.showError(error.message + " 5")
                            view.hideLoading()
                        }
                )
    }
}