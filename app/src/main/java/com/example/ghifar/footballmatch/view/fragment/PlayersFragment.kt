package com.example.ghifar.footballmatch.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ghifar.footballmatch.R

/**
 * A simple [Fragment] subclass created by Alhudaghifari on 21 Oct 2018
 *
 */
class PlayersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false)
    }


}
