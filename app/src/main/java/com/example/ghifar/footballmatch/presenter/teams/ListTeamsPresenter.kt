package com.example.ghifar.footballmatch.presenter.teams

import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 20:09 20/10/18
 *
 */
class ListTeamsPresenter(private val view: ListTeamsInterface) {

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getListTeamByLeague(leagueName: String) {
        view.showLoading()
        disposable = footballApiService.getListTeamByLeagueName(leagueName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.showTeamList(result.teams)
                        },
                        { error ->
                            view.showError(error.message + " 1")
                            view.hideLoading()
                        }
                )
    }
}