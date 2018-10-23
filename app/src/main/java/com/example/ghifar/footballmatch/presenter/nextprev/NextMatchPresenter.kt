package com.example.ghifar.footballmatch.presenter.nextprev

import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 6:23 25/09/18
 *
 */
class NextMatchPresenter(private val view: DataMatchInterface) {

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getDataNextMatch(leagueId: String) {
        view.showLoading()
        disposable = footballApiService.getNext15EventsByLeagueId(leagueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.showEventList(result.events)
                            view.hideLoading()
                        },
                        { error ->
                            view.showError(error.message + "")
                            view.hideLoading()
                        }
                )
    }
}