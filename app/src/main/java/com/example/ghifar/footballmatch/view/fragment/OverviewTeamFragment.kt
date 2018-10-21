package com.example.ghifar.footballmatch.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.Utils
import com.example.ghifar.footballmatch.presenter.teams.TeamInterface
import com.example.ghifar.footballmatch.presenter.teams.TeamPresenter


/**
 * A simple [Fragment] subclass created by alhudaghifari on 21 October 2018
 *
 */
class OverviewTeamFragment : Fragment(), TeamInterface {

    private val TAG = OverviewTeamFragment::class.java.simpleName

    private lateinit var wvDescription: WebView
    private lateinit var wvStadium: WebView
    private lateinit var tvStadium: TextView
    private lateinit var ivStadium: ImageView
    private lateinit var presenter: TeamPresenter
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var id: String
    private var utils = Utils()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_overview_team, container, false)

        wvDescription = v.findViewById(R.id.wvDescription)
        wvStadium = v.findViewById(R.id.wvStadium)
        tvStadium = v.findViewById(R.id.tvStadium)
        ivStadium = v.findViewById(R.id.ivStadium)
        swiperefresh = v.findViewById(R.id.swiperefresh)

        id = getArguments()!!.getString("id")

        Log.d(TAG, "id : $id")

        presenter = TeamPresenter(this)
        presenter.getDetailTeam(id)

        swiperefresh.setOnRefreshListener{
            Log.d(TAG, "onRefresh called from SwipeRefreshLayout")
            presenter.getDetailTeam(id)
        }

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

    override fun showTeamList(data: List<Team>) {
        wvDescription.loadData(utils.getHtmlStringFormat(data[0].strDescriptionEN),
                "text/html", "utf-8")

        Glide.with(this)
                .load(data[0].strStadiumThumb).into(ivStadium)
        tvStadium.text = data[0].strStadium


        wvStadium.loadData(utils.getHtmlStringFormat(data[0].strStadiumDescription),
                "text/html", "utf-8")
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError")
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


}
