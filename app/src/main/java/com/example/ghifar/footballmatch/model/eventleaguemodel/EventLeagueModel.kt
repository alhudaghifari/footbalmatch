package com.example.ghifar.footballmatch.model.eventleaguemodel

/**
 * Created by Alhudaghifari on 1:07 17/09/18
 */
import com.google.gson.annotations.SerializedName

class EventLeagueModel {

    @SerializedName("events")
    var events: List<Event> = mutableListOf()

}