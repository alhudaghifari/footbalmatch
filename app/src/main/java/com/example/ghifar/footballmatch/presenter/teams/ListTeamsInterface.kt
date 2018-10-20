package com.example.ghifar.footballmatch.presenter.teams

import com.example.ghifar.footballmatch.model.datateammodel.Team


/**
 * Created by Alhudaghifari on 20:26 20/10/18
 *
 */
interface ListTeamsInterface {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
    fun showError(msg: String)
}