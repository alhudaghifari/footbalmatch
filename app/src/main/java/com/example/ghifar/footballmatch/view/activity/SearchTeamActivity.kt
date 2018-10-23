package com.example.ghifar.footballmatch.view.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.teamsearcher.TeamSearchInterface
import com.example.ghifar.footballmatch.presenter.teamsearcher.TeamSearchPresenter
import com.example.ghifar.footballmatch.view.adapter.TeamsAdapter
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class SearchTeamActivity : AppCompatActivity(), TeamSearchInterface {

    private val TAG = SearchTeamActivity::class.java.simpleName
    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: TeamsAdapter
    private lateinit var presenter: TeamSearchPresenter
    private lateinit var etSearch: EditText

    private var items: MutableList<Team> = mutableListOf()
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_team)

        val myToolbar = findViewById(R.id.my_toolbar) as Toolbar
        recyclerListTeam = findViewById(R.id.recyclerListTeam)
        swiperefresh = findViewById(R.id.swiperefresh)
        etSearch = findViewById(R.id.etSearch)

        // setup toolbar
        myToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        myToolbar.setTitle(resources.getString(R.string.title_matches))
        setSupportActionBar(myToolbar)

        listenerFunction()
        presenter = TeamSearchPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                finish()
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
        Log.d(TAG, "showEventList")
        items.clear()
        for (index in data) {
            if (index.strSport.equals("Soccer")) {
                items.add(index)
            }
        }
        setRecyclerView()
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        adapter = TeamsAdapter(this, items) {
            Log.d(TAG, "tes teamsadapter")
            ctx.startActivity<DetailTeamActivity>("id" to "${it.idTeam}", "teamName" to "${it.strTeam}")
        }
        recyclerListTeam.layoutManager = LinearLayoutManager(this)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.searchTeamByName(searchText)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "afterTextChanged")
                searchText = etSearch.text.toString().trim()
                Log.d(TAG, "searchText = $searchText")
                presenter.disposeThis()
                presenter.searchTeamByName(searchText)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

}
