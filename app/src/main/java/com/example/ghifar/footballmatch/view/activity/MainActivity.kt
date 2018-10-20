package com.example.ghifar.footballmatch.view.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.view.fragment.FavoriteMatchFragment
import com.example.ghifar.footballmatch.view.fragment.MatchFragment
import com.example.ghifar.footballmatch.view.fragment.NextMatchFragment
import com.example.ghifar.footballmatch.view.fragment.TeamsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_matches -> {
                supportActionBar?.title = resources.getString(R.string.title_matches)
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.root_layout, MatchFragment(), resources.getString(R.string.title_matches))
                        .commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_teams -> {
                supportActionBar?.title = resources.getString(R.string.title_teams)
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.root_layout, TeamsFragment(), resources.getString(R.string.title_teams))
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites_match -> {
                supportActionBar?.title = resources.getString(R.string.favoritematch)
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.root_layout, FavoriteMatchFragment(), resources.getString(R.string.favoritematch))
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = resources.getString(R.string.title_matches)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, MatchFragment(), resources.getString(R.string.title_matches))
                .commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}
