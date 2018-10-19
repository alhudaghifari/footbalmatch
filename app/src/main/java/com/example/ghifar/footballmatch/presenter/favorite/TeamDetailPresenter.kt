package com.example.ghifar.footballmatch.presenter.favorite

import android.util.Log
import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 17:12 26/09/18
 *
 */
class TeamDetailPresenter(private val view: TeamDetailView) {

    private val TAG = TeamDetailPresenter::class.java.simpleName

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
                            view.showTeamDetail(result.teams)
                            view.hideLoading()
                        },
                        { error ->
                            view.showError(error.message + "")
                            view.hideLoading()
                        }
                )
    }

}