package com.example.ghifar.footballmatch.view.activity

import android.content.Intent
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
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.eventsearcher.EventSearchInterface
import com.example.ghifar.footballmatch.presenter.eventsearcher.EventSearchPresenter
import com.example.ghifar.footballmatch.view.adapter.MatchAdapter

class EventSearchActivity : AppCompatActivity(), EventSearchInterface {

    private val TAG = EventSearchActivity::class.java.simpleName
    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: MatchAdapter
    private lateinit var onArtikelClickListener: MatchAdapter.OnArtikelClickListener
    private lateinit var presenter: EventSearchPresenter
    private lateinit var etSearch: EditText

    private var items: MutableList<Event> = mutableListOf()
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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
        presenter = EventSearchPresenter(this)

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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, " onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, " onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, " onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, " onDestroy")
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

    override fun showEventList(data: List<Event>) {
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
        swiperefresh.isRefreshing = false
        recyclerListTeam.layoutManager = LinearLayoutManager(this)
        adapter = MatchAdapter(items, this, Constant.SEARCHMATCH)
        adapter.setOnArtikelClickListener(onArtikelClickListener)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener {
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
           presenter.searchEventByName(searchText)
        }

        onArtikelClickListener = object : MatchAdapter.OnArtikelClickListener {
            override fun onClick(posisi: Int, model: Event) {
                Log.d(TAG, "clicked on position : $posisi")
                val intent = Intent(this@EventSearchActivity, DetailMatchActivity::class.java)
                intent.putExtra(Constant.ID, items.get(posisi).idEvent)
                intent.putExtra(Constant.POSITION, posisi)
                intent.putExtra(Constant.HOMETEAMNAME, items.get(posisi).idHomeTeam)
                intent.putExtra(Constant.AWAYTEAMNAME, items.get(posisi).idAwayTeam)
                intent.putExtra(Constant.TIPEMATCH, Constant.SEARCHMATCH)
                startActivity(intent)
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "afterTextChanged")
                searchText = etSearch.text.toString().trim()
                Log.d(TAG, "searchText = $searchText")
                presenter.disposeThis()
                presenter.searchEventByName(searchText)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}
