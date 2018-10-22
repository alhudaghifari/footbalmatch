package com.example.ghifar.footballmatch.view.activity

import android.os.Build
import android.os.Bundle
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
import com.example.ghifar.footballmatch.model.datateammodel.Team
import com.example.ghifar.footballmatch.presenter.teams.TeamInterface
import com.example.ghifar.footballmatch.presenter.teams.TeamPresenter
import com.example.ghifar.footballmatch.view.fragment.OverviewTeamFragment
import com.example.ghifar.footballmatch.view.fragment.PlayersFragment
import kotlinx.android.synthetic.main.activity_team_detail.*

class DetailTeamActivity : AppCompatActivity(), TeamInterface {

    private val TAG = DetailTeamActivity::class.java.simpleName

    private lateinit var ivPhotoProfil: ImageView
    private lateinit var tvTeamName: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvCity: TextView
    private lateinit var presenter: TeamPresenter
    private lateinit var id: String
    private var teamName: String = ""

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

        myToolbar.setTitle("")
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        id = intent.getStringExtra("id")
        teamName = intent.getStringExtra("teamName")

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
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError : " + msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        ivRefresh.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        } else if (item?.itemId == R.id.add_to_favorite) {

            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}
