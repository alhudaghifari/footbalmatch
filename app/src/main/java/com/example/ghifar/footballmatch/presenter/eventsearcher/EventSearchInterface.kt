package com.example.ghifar.footballmatch.presenter.eventsearcher

import com.example.ghifar.footballmatch.model.eventleaguemodel.Event


/**
 * Created by Alhudaghifari on 20:05 23/10/18
 *
 */
interface EventSearchInterface {
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<Event>)
    fun showError(msg: String)
}