package com.example.ghifar.footballmatch.presenter.favorite

import com.example.ghifar.footballmatch.model.datateammodel.Team


/**
 * Created by Alhudaghifari on 17:12 26/09/18
 *
 */
interface TeamDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamDetail(data: List<Team>)
    fun showError(msg: String)
}