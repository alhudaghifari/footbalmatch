package com.example.ghifar.footballmatch.model.database

/**
 * Created by Alhudaghifari on 8:13 27/09/18
 *
 */
data class EventDb(val id: Long?,
                val EVENT_ID: String?,
                val ID_HOME_TEAM: String?,
                val ID_AWAY_TEAM: String?,
                val STR_HOME_TEAM: String?,
                val STR_AWAY_TEAM: String?,
                val INT_HOME_SCORE: String?,
                val INT_AWAY_SCORE: String?,
                val DATE_EVENT: String?,
                val STR_HOME_BADGE: String?,
                val STR_AWAY_BADGE: String?) {

    companion object {
        const val TABLE_FAVORITE_MATCH: String = "MATCH_TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val ID_HOME_TEAM: String = "ID_HOME_TEAM"
        const val ID_AWAY_TEAM: String = "ID_AWAY_TEAM"
        const val STR_HOME_TEAM: String = "STRHOMETEAM"
        const val STR_AWAY_TEAM: String = "STRAWAYTEAM"
        const val INT_HOME_SCORE: String = "INTHOMESCORE"
        const val INT_AWAY_SCORE: String = "INTAWAYSCORE"
        const val DATE_EVENT: String = "DATEEVENT"
        const val STR_HOME_BADGE: String = "STRHOMEBADGE"
        const val STR_AWAY_BADGE: String = "STRAWAYBADGE"
    }
}