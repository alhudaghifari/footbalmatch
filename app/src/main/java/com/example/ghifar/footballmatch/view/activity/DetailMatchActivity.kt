package com.example.ghifar.footballmatch.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.database.EventDb
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.*
import com.example.ghifar.footballmatch.presenter.detailmatch.DetailMatchInterface
import com.example.ghifar.footballmatch.presenter.detailmatch.DetailMatchPresenter
import kotlinx.android.synthetic.main.activity_detail_match.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh

class DetailMatchActivity : AppCompatActivity(), DetailMatchInterface {

    private val TAG = DetailMatchActivity::class.java.simpleName

    private var homeTeams: List<Team> = mutableListOf()
    private var awayTeams: List<Team> = mutableListOf()
    private var position = 0
    private var tipeMatch = 0
    private var homeTeamName = "Man United"
    private var awayTeamName = "Chelsea"
    private var isHomeBadgeLoaded = false
    private var isAwayBadgeLoaded = false
    private var isDataLoaded = false

    private lateinit var presenter: DetailMatchPresenter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var dataDetailEvent: Event

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)

        swipeRefresh = findViewById(R.id.swiperefresh)

        val intent = intent
        id = intent.getStringExtra(Constant.ID).toLong()
        position = intent.getIntExtra(Constant.POSITION, 0)
        homeTeamName = intent.getStringExtra(Constant.HOMETEAMNAME)
        awayTeamName = intent.getStringExtra(Constant.AWAYTEAMNAME)
        tipeMatch = intent.getIntExtra(Constant.TIPEMATCH, 0)

        favoriteState()

        presenter = DetailMatchPresenter(this)

        presenter.getDetailEventById(id.toString())
        presenter.getDetailHomeTeam(homeTeamName)
        presenter.getDetailAwayTeam(awayTeamName)

        swipeRefresh.onRefresh {
            presenter.getDetailEventById(id.toString())
            presenter.getDetailHomeTeam(homeTeamName)
            presenter.getDetailAwayTeam(awayTeamName)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.disposeThis()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
        setLoading(true)
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
        setLoading(false)
    }

    override fun showTeamListHome(dataTeam: List<Team>) {
        Log.d(TAG, "showTeamListHome")
        homeTeams = dataTeam
        Glide.with(this)
                .load(homeTeams[0].strTeamBadge)
                .into(ivLogoTimKiri)
        if (homeTeams.size != 0) isHomeBadgeLoaded = true
    }

    override fun showTeamListAway(dataTeam: List<Team>) {
        Log.d(TAG, "showTeamListAway")
        awayTeams = dataTeam
        Glide.with(this)
                .load(awayTeams[0].strTeamBadge)
                .into(ivLogoTimKanan)
        if (awayTeams.size != 0) isAwayBadgeLoaded = true
    }

    override fun showDetailEvent(data: Event) {
        Log.d(TAG, "showDetailEvent")
        dataDetailEvent = data
        isDataLoaded = true
        initiateViewData()
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError : " + msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initiateViewData() {
        setLoading(false)

        var utils = Utils()

        tvTanggalMain.text = utils.getDate(dataDetailEvent.dateEvent)
        tvJamMain.text = utils.getClock(dataDetailEvent.strTime?.substring(0, 8))

        tvNamaTimKiri.text = dataDetailEvent.strHomeTeam
        tvNamaTimKanan.text = dataDetailEvent.strAwayTeam
        tvScoreTimKiri.text = dataDetailEvent.intHomeScore
        tvScoreTimKanan.text = dataDetailEvent.intAwayScore
        tvHomeGoals.text = dataDetailEvent.strHomeGoalDetails
        tvAwayGoals.text = dataDetailEvent.strAwayGoalDetails
        tvHomeShots.text = dataDetailEvent.intHomeShots
        tvAwayShots.text = dataDetailEvent.intAwayShots
        tvHomeGoalKeepers.text = dataDetailEvent.strHomeLineupGoalkeeper
        tvAwayGoalKeeper.text = dataDetailEvent.strAwayLineupGoalkeeper
        tvHomeDefense.text = dataDetailEvent.strHomeLineupDefense
        tvAwayDefense.text = dataDetailEvent.strAwayLineupDefense
        tvHomeMidfield.text = dataDetailEvent.strHomeLineupMidfield
        tvAwayMidfield.text = dataDetailEvent.strAwayLineupMidfield
        tvHomeForward.text = dataDetailEvent.strHomeLineupForward
        tvAwayForward.text = dataDetailEvent.strAwayLineupForward
        tvHomeSubstitutes.text = dataDetailEvent.strHomeLineupSubstitutes
        tvAwaySubstitutes.text = dataDetailEvent.strAwayLineupSubstitutes
        tvHomeRedCards.text = dataDetailEvent.strHomeRedCards
        tvAwayRedCards.text = dataDetailEvent.strAwayRedCards
        tvHomeYellowCards.text = dataDetailEvent.strHomeYellowCards
        tvAwayYellowCards.text = dataDetailEvent.strAwayYellowCards
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            swipeRefresh.isRefreshing = true
            linlayPapanAtas.visibility = GONE
            linlayPapanBawah.visibility = GONE
        } else {
            swipeRefresh.isRefreshing = false
            linlayPapanAtas.visibility = VISIBLE
            linlayPapanBawah.visibility = VISIBLE
        }
    }

    // bagian db

    private fun favoriteState(){
        Log.d(TAG, "favoriteState | id : " + id)
        database.use {
            val result = select(EventDb.TABLE_FAVORITE_MATCH)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to id)
            val favorite = result.parseList(classParser<EventDb>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (!isHomeBadgeLoaded || !isAwayBadgeLoaded || !isDataLoaded) {
                    Toast.makeText(this, "loading..", Toast.LENGTH_SHORT).show()
                } else {
                    if (isFavorite) removeFromFavorite() else addToFavorite()

                    isFavorite = !isFavorite
                    setFavorite()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        var homescore = dataDetailEvent.intHomeScore
        var awayscore = dataDetailEvent.intAwayScore
        if (tipeMatch == Constant.NEXTMATCH) {
            homescore = "-"
            awayscore = "-"
        }
        try {
            database.use {
                insert(EventDb.TABLE_FAVORITE_MATCH,
                        EventDb.EVENT_ID to dataDetailEvent.idEvent,
                        EventDb.ID_HOME_TEAM to dataDetailEvent.idHomeTeam,
                        EventDb.ID_AWAY_TEAM to dataDetailEvent.idAwayTeam,
                        EventDb.STR_HOME_TEAM to dataDetailEvent.strHomeTeam,
                        EventDb.STR_AWAY_TEAM to dataDetailEvent.strAwayTeam,
                        EventDb.INT_HOME_SCORE to homescore,
                        EventDb.INT_AWAY_SCORE to awayscore,
                        EventDb.DATE_EVENT to dataDetailEvent.dateEvent,
                        EventDb.STR_HOME_BADGE to homeTeams[0].strTeamBadge,
                        EventDb.STR_AWAY_BADGE to awayTeams[0].strTeamBadge)
            }
            snackbar(swipeRefresh, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(EventDb.TABLE_FAVORITE_MATCH, "(EVENT_ID = {id})",
                        "id" to id)
            }
            snackbar(swipeRefresh, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }
}