package com.example.ghifar.footballmatch.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.R.array.league
import org.jetbrains.anko.support.v4.ctx

/**
 * Created by alhudaghifari on 20 Oktober 2018
 */
class TeamsFragment : Fragment() {

    private val TAG = TeamsFragment::class.java.simpleName

    private lateinit var spinnerTeams: Spinner
    private lateinit var leagueName: String
    private lateinit var myToolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_teams, container, false)

        myToolbar = v.findViewById(R.id.my_toolbar) as Toolbar

        // setup toolbar
        myToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        myToolbar.setTitle(resources.getString(R.string.title_matches))
        (activity as AppCompatActivity).setSupportActionBar(myToolbar)

        spinnerTeams = v.findViewById(R.id.spinnerTeams)
        val spinnerItems = resources.getStringArray(league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinnerTeams.adapter = spinnerAdapter

        spinnerTeams.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = spinnerTeams.selectedItem.toString()
                Log.d(TAG, "spinner leagueName : $leagueName")
//                presenter.getTeamList(leagueName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return v
    }


}
