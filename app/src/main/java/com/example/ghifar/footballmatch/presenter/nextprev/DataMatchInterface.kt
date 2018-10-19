package com.example.ghifar.footballmatch.presenter.nextprev

import com.example.ghifar.footballmatch.model.eventleaguemodel.Event


/**
 * Created by Alhudaghifari on 6:26 25/09/18
 *
 */
interface DataMatchInterface {
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<Event>)
    fun showError(msg: String)
}