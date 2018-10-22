package com.example.ghifar.footballmatch.model.database


/**
 * Created by Alhudaghifari on 13:48 22/10/18
 *
 */
data class TeamDb(val id: Long?,
                   val ID_TEAM: String?,
                   val STR_TEAM: String?,
                   val STR_TEAM_BADGE: String?) {

    companion object {
        const val TABLE_FAVORITE_TEAM: String = "TEAM_TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val ID_TEAM: String = "ID_TEAM"
        const val STR_TEAM: String = "STR_TEAM"
        const val STR_TEAM_BADGE: String = "STR_TEAM_BADGE"
    }
}