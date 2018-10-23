package com.example.ghifar.footballmatch.presenter.teamsearcher

import com.example.ghifar.footballmatch.model.datateammodel.Team


/**
 * Created by Alhudaghifari on 20:07 23/10/18
 *
 */
interface TeamSearchInterface {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
    fun showError(msg: String)
}