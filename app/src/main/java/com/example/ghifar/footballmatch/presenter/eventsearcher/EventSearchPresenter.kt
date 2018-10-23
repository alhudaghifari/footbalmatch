package com.example.ghifar.footballmatch.presenter.eventsearcher

import android.util.Log
import com.example.ghifar.footballmatch.presenter.ApiService
import com.example.ghifar.footballmatch.presenter.nextprev.DataMatchInterface
import com.example.ghifar.footballmatch.presenter.nextprev.PrevMatchPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Alhudaghifari on 20:03 23/10/18
 *
 */
class EventSearchPresenter(private val view: EventSearchInterface) {

    private val TAG = PrevMatchPresenter::class.java.simpleName

    private var disposable: Disposable? = null

    private val footballApiService by lazy {
        ApiService.create()
    }

    fun disposeThis() {
        disposable?.dispose()
    }

    fun searchEventByName(eventName: String) {
        Log.d(TAG, "getDataLastMatch")
        view.showLoading()
        disposable = footballApiService.searchEventByEventName(eventName)
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