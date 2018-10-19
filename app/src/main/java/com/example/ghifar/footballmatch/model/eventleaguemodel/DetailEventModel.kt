package com.example.ghifar.footballmatch.model.eventleaguemodel


/**
 * Created by Alhudaghifari on 11:32 30/09/18
 *
 */
import com.google.gson.annotations.SerializedName

class DetailEventModel {

    @SerializedName("events")
    var events: List<Event> = mutableListOf()

}