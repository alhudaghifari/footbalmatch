package com.example.ghifar.footballmatch.view.fragment


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
import com.example.ghifar.footballmatch.model.database.TeamDb
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.database
import com.example.ghifar.footballmatch.view.activity.DetailTeamActivity
import com.example.ghifar.footballmatch.view.adapter.TeamsAdapter
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

/**
 * A simple [Fragment] subclass by alhudaghifari created on 22 Oct 2018.
 *
 */
class FavoriteTeamFragment : Fragment() {

    private val TAG = FavoriteTeamFragment::class.java.simpleName

    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: TeamsAdapter
    
    private var favorites: MutableList<TeamDb> = mutableListOf()
    private var teams: MutableList<Team> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favorite_team, container, false)

        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        showFavorite()

        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            showFavorite()
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        // Check which request we're responding to
        if (requestCode == Constant.PICK_FAV_TEAM) {
            Log.d(TAG, "onActivityResult PICK_FAV_TEAM")
            showFavorite()
        }
    }

    private fun setRecyclerView() {
        adapter = TeamsAdapter(context!!, teams) {
            val intent = Intent(context, DetailTeamActivity::class.java)
            intent.putExtra("id", it.idTeam)
            intent.putExtra("teamName", it.strTeam)
            startActivityForResult(intent, Constant.PICK_FAV_TEAM)
        }
        recyclerListTeam.layoutManager = LinearLayoutManager(context)
        recyclerListTeam.adapter = adapter
    }


    private fun showFavorite(){
        Log.d(TAG, "showFavorite")
        favorites.clear()
        teams.clear()
        context?.database?.use {
            swiperefresh.isRefreshing = false
            val result = select(TeamDb.TABLE_FAVORITE_TEAM)
            val favorite = result.parseList(classParser<TeamDb>())
            favorites.addAll(favorite)
            for (index in favorites) {
                var team = Team()
                team.idTeam = index.ID_TEAM
                team.strTeam = index.STR_TEAM
                team.strTeamBadge = index.STR_TEAM_BADGE
                teams.add(team)
            }
            Log.d(TAG, "showFavorite size : " + favorites.size)
            setRecyclerView()
        }
    }

}
