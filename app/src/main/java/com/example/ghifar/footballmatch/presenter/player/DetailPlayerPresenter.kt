package com.example.ghifar.footballmatch.presenter.player

import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 7:57 22/10/18
 *
 */
class DetailPlayerPresenter (private val view: PlayersInterface) {

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getDetailPlayerById(id: String) {
        view.showLoading()
        disposable = footballApiService.getDetailPlayerById(id)
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