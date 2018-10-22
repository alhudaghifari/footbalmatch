package com.example.ghifar.footballmatch.model.playermodel

import com.google.gson.annotations.SerializedName


/**
 * Created by Alhudaghifari on 7:54 22/10/18
 *
 */

class OnePlayerModel {

    @SerializedName("players")
    var player: List<Player> = mutableListOf()

}
