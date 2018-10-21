package com.example.ghifar.footballmatch.presenter.player

import com.example.ghifar.footballmatch.model.playermodel.Player


/**
 * Created by Alhudaghifari on 23:34 21/10/18
 *
 */
interface PlayersInterface {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Player>)
    fun showError(msg: String)
}