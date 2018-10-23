package com.example.ghifar.footballmatch.model.search

import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.google.gson.annotations.SerializedName


/**
 * Created by Alhudaghifari on 19:54 23/10/18
 *
 */

class EventSearchModel {

    @SerializedName("event")
    var events: List<Event> = mutableListOf()

}