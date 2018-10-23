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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.Utils
import com.example.ghifar.footballmatch.presenter.nextprev.DataMatchInterface
import com.example.ghifar.footballmatch.presenter.nextprev.PrevMatchPresenter
import com.example.ghifar.footballmatch.view.activity.DetailMatchActivity
import com.example.ghifar.footballmatch.view.adapter.MatchAdapter
import org.jetbrains.anko.support.v4.ctx


/**
 * A simple [Fragment] subclass.
 *
 */
class PrevMatchFragment : Fragment(), DataMatchInterface {

    private val TAG = PrevMatchFragment::class.java.simpleName

    private var items: List<Event> = mutableListOf()

    private lateinit var spinnerTeams: Spinner
    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: MatchAdapter
    private lateinit var onArtikelClickListener: MatchAdapter.OnArtikelClickListener
    private lateinit var presenter: PrevMatchPresenter

    private var leagueId = "4328"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_prev_match, container,
                false)

        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        swiperefresh = v.findViewById(R.id.swiperefresh)
        spinnerTeams = v.findViewById(R.id.spinnerTeams)

        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinnerTeams.adapter = spinnerAdapter

        presenter = PrevMatchPresenter(this)

        listenerFunction()
        presenter.getDataLastMatch(leagueId)

        return v
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
        items = data
        setRecyclerView()
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError")
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        swiperefresh.isRefreshing = false
        recyclerListTeam.layoutManager = LinearLayoutManager(context)
        adapter = MatchAdapter(items, context, Constant.PREVMATCH)
        adapter.setOnArtikelClickListener(onArtikelClickListener)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.getDataLastMatch(leagueId)
        }

        onArtikelClickListener = object : MatchAdapter.OnArtikelClickListener {
            override fun onClick(posisi: Int, model: Event) {
                Log.d(TAG, "PrevMatch clicked on position : " + posisi)
                val intent = Intent(context, DetailMatchActivity::class.java)
                intent.putExtra(Constant.ID, items.get(posisi).idEvent)
                Log.d(TAG, "id : " + items.get(posisi).idEvent)
                intent.putExtra(Constant.POSITION, posisi)
                intent.putExtra(Constant.HOMETEAMNAME, items.get(posisi).idHomeTeam)
                intent.putExtra(Constant.AWAYTEAMNAME, items.get(posisi).idAwayTeam)
                intent.putExtra(Constant.TIPEMATCH, Constant.PREVMATCH)
                startActivity(intent)
            }
        }

        spinnerTeams.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var leagueName = spinnerTeams.selectedItem.toString()
                Log.d(TAG, "spinner leagueName : $leagueName")
                var utils = Utils()
                leagueId = utils.getIdLeague(leagueName)
                presenter.getDataLastMatch(leagueId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
