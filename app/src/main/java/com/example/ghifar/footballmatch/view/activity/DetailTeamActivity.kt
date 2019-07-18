package com.example.ghifar.footballmatch.view.activity

import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.database.TeamDb
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.database
import com.example.ghifar.footballmatch.presenter.teams.TeamInterface
import com.example.ghifar.footballmatch.presenter.teams.TeamPresenter
import com.example.ghifar.footballmatch.view.fragment.OverviewTeamFragment
import com.example.ghifar.footballmatch.view.fragment.PlayersFragment
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import java.nio.file.Files.delete

class DetailTeamActivity : AppCompatActivity(), TeamInterface {

    private val TAG = DetailTeamActivity::class.java.simpleName

    private lateinit var ivPhotoProfil: ImageView
    private lateinit var tvTeamName: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvCity: TextView
    private lateinit var presenter: TeamPresenter
    private lateinit var id: String
    private var teamName: String = ""

    private var items: List<Team> = mutableListOf()

    private var menuItem: Menu? = null
    private var isDataLoaded = false
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        val myToolbar = findViewById(R.id.my_toolbar) as Toolbar
        val tabLayout = findViewById(R.id.tabs) as TabLayout
        val viewPager = findViewById(R.id.viewpager) as ViewPager
        ivPhotoProfil = findViewById(R.id.ivPhotoProfil)
        tvTeamName = findViewById(R.id.tvTeamName)
        tvYear = findViewById(R.id.tvYear)
        tvCity = findViewById(R.id.tvCity)
        val mLayoutManager = LinearLayoutManager(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat
                    .getColor(this@DetailTeamActivity, R.color.blackandblue2)
        }

        val intent = intent
        id = intent.getStringExtra("id")
        teamName = intent.getStringExtra("teamName")
        favoriteState()

        myToolbar.setTitle("")
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        presenter = TeamPresenter(this)
        presenter.getDetailTeam(id)

        // add tabLayout
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())

        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        viewPager.adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setTabMode(TabLayout.MODE_FIXED)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
        tabLayout.getTabAt(0)?.setText(resources.getString(R.string.title_overview))
        tabLayout.getTabAt(1)?.setText(resources.getString(R.string.title_players))
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.silveryoung)
        )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "position fragment : ${tab.position}")
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        ivRefresh.setOnClickListener {
            presenter.getDetailTeam(id)
        }
    }

    inner class PagerAdapter(fm: FragmentManager, internal var mNumOfTabs: Int)
        : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            val bundle = Bundle()
            if (position == 0) {
                bundle.putString("id", id)
                var overviewTeamFragment = OverviewTeamFragment()
                overviewTeamFragment.arguments = bundle
                return overviewTeamFragment
            } else if (position == 1) {
                Log.d(TAG, "teamName : $teamName")
                bundle.putString("teamName", teamName)
                var playerFragment = PlayersFragment()
                playerFragment.arguments = bundle
                return playerFragment
            } else return null
        }

        override fun getCount(): Int {
            return mNumOfTabs
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.disposeThis()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
        ivRefresh.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
        progressBar.visibility = View.GONE
    }

    override fun showTeamList(data: List<Team>) {
        Glide.with(this)
                .load(data[0].strTeamBadge)
                .into(ivPhotoProfil)
        tvTeamName.text = data[0].strTeam
        tvYear.text = data[0].intFormedYear
        tvCity.text = data[0].strCountry
        isDataLoaded = true
        items = data
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError : " + msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        ivRefresh.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        } else if (item?.itemId == R.id.add_to_favorite) {
            if (!isDataLoaded) {
                Toast.makeText(this, "loading..", Toast.LENGTH_SHORT).show()
            } else {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
            }
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    // bagian db

    private fun favoriteState() {
        Log.d(TAG, "favoriteState | id : " + id)
        database.use {
            val result = select(TeamDb.TABLE_FAVORITE_TEAM)
                    .whereArgs("(ID_TEAM = {id})",
                            "id" to id)
            val favorite = result.parseList(classParser<TeamDb>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(TeamDb.TABLE_FAVORITE_TEAM,
                        TeamDb.ID_TEAM to items[0].idTeam,
                        TeamDb.STR_TEAM to items[0].strTeam,
                        TeamDb.STR_TEAM_BADGE to items[0].strTeamBadge)
            }
            snackbar(coordinatorLayout, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(TeamDb.TABLE_FAVORITE_TEAM, "(ID_TEAM = {id})",
                        "id" to id)
            }
            snackbar(coordinatorLayout, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }
}
