package com.example.ghifar.footballmatch.presenter.nextprev

import android.util.Log
import com.example.ghifar.footballmatch.presenter.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 14:36 24/09/18
 *
 */
class PrevMatchPresenter(private val view: DataMatchInterface) {

    private val TAG = PrevMatchPresenter::class.java.simpleName

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun getDataLastMatch(leagueId: String) {
        Log.d(TAG, "getDataLastMatch")
        view.showLoading()
        disposable = footballApiService.getLast15EventsByLeagueId(leagueId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d(TAG, "getDataLastMatch result. size : " + result.events.size)
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