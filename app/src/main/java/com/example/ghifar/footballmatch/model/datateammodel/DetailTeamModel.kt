package com.example.ghifar.footballmatch.model.datateammodel

/**
 * Created by Alhudaghifari on 16:12 17/09/18
 */

import com.google.gson.annotations.SerializedName

class DetailTeamModel {

    @SerializedName("teams")
    var teams: List<Team> = mutableListOf()

}