package com.example.ghifar.footballmatch.presenter.teams

import com.example.ghifar.footballmatch.model.datateammodel.Team

/**
 * Created by Alhudaghifari on 19:47 20/10/18
 *
 */
interface TeamInterface {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
    fun showError(msg: String)
}