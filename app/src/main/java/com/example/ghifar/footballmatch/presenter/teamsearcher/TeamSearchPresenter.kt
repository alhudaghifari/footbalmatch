package com.example.ghifar.footballmatch.presenter.teamsearcher

import android.util.Log
import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.eventsearcher.EventSearchInterface
import com.example.ghifar.footballmatch.presenter.nextprev.PrevMatchPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 20:06 23/10/18
 *
 */
class TeamSearchPresenter(private val view: TeamSearchInterface) {

    private val TAG = PrevMatchPresenter::class.java.simpleName

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun searchEventByName(teamName: String) {
        Log.d(TAG, "getDataLastMatch")
        view.showLoading()
        disposable = footballApiService.searchTeamsByName(teamName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.showTeamList(result.teams)
                            view.hideLoading()
                        },
                        { error ->
                            view.showError(error.message + "")
                            view.hideLoading()
                        }
                )
    }
}