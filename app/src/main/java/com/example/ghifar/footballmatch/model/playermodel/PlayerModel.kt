package com.example.ghifar.footballmatch.model.playermodel


/**
 * Created by Alhudaghifari on 23:26 21/10/18
 *
 */

import com.google.gson.annotations.SerializedName

class PlayerModel {

    @SerializedName("player")
    var player: List<Player> = mutableListOf()

}
