package com.example.ghifar.footballmatch.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.database.EventDb
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.database
import com.example.ghifar.footballmatch.view.activity.DetailMatchActivity
import com.example.ghifar.footballmatch.view.adapter.MatchAdapter
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteMatchFragment : Fragment() {

    private val TAG = FavoriteMatchFragment::class.java.simpleName

    private var favorites: MutableList<EventDb> = mutableListOf()
    private var items: MutableList<Event> = mutableListOf()

    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: MatchAdapter
    private lateinit var onArtikelClickListener: MatchAdapter.OnArtikelClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favorite_match, container,
                false)

        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        listenerFunction()
        showFavorite()

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        // Check which request we're responding to
        if (requestCode == Constant.PICK_FAV_MATCH) {
            Log.d(TAG, "onActivityResult PICK_FAV_MATCH")
            showFavorite()
        }
    }

    private fun showFavorite(){
        Log.d(TAG, "showFavorite")
        favorites.clear()
        items.clear()
        context?.database?.use {
            swiperefresh.isRefreshing = false
            val result = select(EventDb.TABLE_FAVORITE_MATCH)
            val favorite = result.parseList(classParser<EventDb>())
            favorites.addAll(favorite)
            for (index in favorites) {
                var event = Event()
                event.idEvent = index.EVENT_ID
                event.idHomeTeam = index.ID_HOME_TEAM
                event.idAwayTeam = index.ID_AWAY_TEAM
                event.dateEvent = index.DATE_EVENT
                event.strTime = index.STR_TIME
                event.strHomeTeam = index.STR_HOME_TEAM
                event.strAwayTeam = index.STR_AWAY_TEAM
                event.intHomeScore = index.INT_HOME_SCORE
                event.intAwayScore = index.INT_AWAY_SCORE
                if (event.intHomeScore == null)
                    event.intHomeScore = "-"
                if (event.intAwayScore == null)
                    event.intAwayScore = "-"
                items.add(event)
            }
            Log.d(TAG, "showFavorite size : " + favorites.size)
            Log.d(TAG, "2showFavorite size items : " + items.size)
            setRecyclerView()
        }
    }

    private fun setRecyclerView() {
        swiperefresh.isRefreshing = false
        recyclerListTeam.layoutManager = LinearLayoutManager(context)
        adapter = MatchAdapter(items, context, Constant.FAVMATCH)
        adapter.setOnArtikelClickListener(onArtikelClickListener)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            showFavorite()
        }

        onArtikelClickListener = object : MatchAdapter.OnArtikelClickListener {
            override fun onClick(posisi: Int, model: Event) {
                Log.d(TAG, "PrevMatch clicked on position : " + posisi)
                val intent = Intent(context, DetailMatchActivity::class.java)
                intent.putExtra(Constant.ID, model.idEvent)
                intent.putExtra(Constant.POSITION, posisi)
                intent.putExtra(Constant.HOMETEAMNAME, model.idHomeTeam)
                intent.putExtra(Constant.AWAYTEAMNAME, model.idAwayTeam)
                intent.putExtra(Constant.TIPEMATCH, Constant.FAVMATCH)
                startActivityForResult(intent, Constant.PICK_FAV_MATCH)
            }
        }
    }
}
