package com.example.ghifar.footballmatch.model.player


/**
 * Created by Alhudaghifari on 23:26 21/10/18
 *
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlayerModel {

    @SerializedName("player")
    @Expose
    var player: List<Player>? = null

}
