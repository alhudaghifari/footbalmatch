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
import android.widget.Toast
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.playermodel.Player
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.player.PlayersInterface
import com.example.ghifar.footballmatch.presenter.player.PlayersPresenter
import com.example.ghifar.footballmatch.view.activity.PlayerDetailActivity
import com.example.ghifar.footballmatch.view.adapter.PlayerAdapter

/**
 * A simple [Fragment] subclass created by Alhudaghifari on 21 Oct 2018
 *
 */
class PlayersFragment : Fragment(), PlayersInterface {

    private val TAG = PlayersFragment::class.java.simpleName

    private var items: List<Player> = mutableListOf()

    private lateinit var recyclerListTeam: RecyclerView
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var adapter: PlayerAdapter
    private lateinit var onArtikelClickListener: PlayerAdapter.OnArtikelClickListener
    private lateinit var presenter: PlayersPresenter
    private lateinit var teamName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_players, container, false)

        recyclerListTeam = v.findViewById(R.id.recyclerListTeam)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        teamName = getArguments()!!.getString("teamName")

        presenter = PlayersPresenter(this)

        listenerFunction()
        presenter.getListPlayerByTeamName(teamName)

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

    override fun showTeamList(data: List<Player>) {
        Log.d(TAG, "showTeamList")
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
        adapter = PlayerAdapter(items, context, Constant.PREVMATCH)
        adapter.setOnArtikelClickListener(onArtikelClickListener)
        recyclerListTeam.adapter = adapter
    }

    private fun listenerFunction() {
        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.getListPlayerByTeamName(teamName)
        }

        onArtikelClickListener = object : PlayerAdapter.OnArtikelClickListener {
            override fun onClick(posisi: Int, model: Player) {
                Log.d(TAG, "Player clicked on position : " + posisi)
                val intent = Intent(context, PlayerDetailActivity::class.java)
                intent.putExtra(Constant.ID, items[posisi].idPlayer)
                startActivity(intent)
            }
        }
    }
}
