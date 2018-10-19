package com.example.ghifar.footballmatch.presenter.detailmatch

import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event


/**
 * Created by Alhudaghifari on 6:36 25/09/18
 *
 */
interface DetailMatchInterface {
    fun showLoading()
    fun hideLoading()
    fun showTeamListHome(dataTeam: List<Team>)
    fun showTeamListAway(dataTeam: List<Team>)
    fun showDetailEvent(data: Event)
    fun showError(msg: String)
}