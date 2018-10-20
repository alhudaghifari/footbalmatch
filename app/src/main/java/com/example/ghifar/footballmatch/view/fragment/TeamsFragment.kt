package com.example.ghifar.footballmatch.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.R.array.league
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.teams.ListTeamsInterface
import com.example.ghifar.footballmatch.presenter.teams.ListTeamsPresenter
import com.example.ghifar.footballmatch.view.activity.TeamDetailActivity
import com.example.ghifar.footballmatch.view.adapter.TeamsAdapter
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx

/**
 * Created by alhudaghifari on 20 Oktober 2018
 */
class TeamsFragment : Fragment(), ListTeamsInterface {

    private val TAG = TeamsFragment::class.java.simpleName

    private lateinit var spinnerTeams: Spinner
    private lateinit var leagueName: String
    private lateinit var myToolbar: Toolbar
    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: TeamsAdapter
    private lateinit var presenter: ListTeamsPresenter

    private var teams: List<Team> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_teams, container, false)

        myToolbar = v.findViewById(R.id.my_toolbar) as Toolbar
        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        spinnerTeams = v.findViewById(R.id.spinnerTeams)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        presenter = ListTeamsPresenter(this)

        setHasOptionsMenu(true)

        // setup toolbar
        myToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        myToolbar.setTitle(resources.getString(R.string.title_matches))
        (activity as AppCompatActivity).setSupportActionBar(myToolbar)

        val spinnerItems = resources.getStringArray(league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinnerTeams.adapter = spinnerAdapter

        listenerFunction()
        leagueName = "English Premier League"
        presenter.getListTeamByLeague(leagueName)

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.disposeThis()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
        swiperefresh.isRefreshing = true
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
        swiperefresh.isRefreshing = false
    }

    override fun showTeamList(data: List<Team>) {
        Log.d(TAG, "showTeamList")
        swiperefresh.isRefreshing = false
        teams = data
        setRecyclerView()
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError")
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        adapter = TeamsAdapter(context!!, teams) {
            Log.d(TAG, "tes teamsadapter")
            ctx.startActivity<TeamDetailActivity>("id" to "${it.idTeam}")
        }
        recyclerListTeam.layoutManager = LinearLayoutManager(context)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.getListTeamByLeague(leagueName)
        }

        spinnerTeams.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = spinnerTeams.selectedItem.toString()
                Log.d(TAG, "spinner leagueName : $leagueName")
                presenter.getListTeamByLeague(leagueName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
