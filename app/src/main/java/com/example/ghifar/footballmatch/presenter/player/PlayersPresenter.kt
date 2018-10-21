package com.example.ghifar.footballmatch.presenter.player

import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 23:33 21/10/18
 *
 */

class PlayersPresenter (private val view: PlayersInterface) {

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getListPlayerByTeamName(teamName: String) {
        view.showLoading()
        disposable = footballApiService.getListPlayerByTeamName(teamName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.showTeamList(result.player)
                            view.hideLoading()
                        },
                        { error ->
                            view.showError(error.message + " 1")
                            view.hideLoading()
                        }
                )
    }
}