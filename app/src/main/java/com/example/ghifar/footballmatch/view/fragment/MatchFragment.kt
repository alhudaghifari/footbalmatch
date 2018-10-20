package com.example.ghifar.footballmatch.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.example.ghifar.footballmatch.R
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.MenuInflater

/**
 * Created by alhudaghifari
 */
class MatchFragment : Fragment() {

    private val TAG = MatchFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, containerViewGroup: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_match, containerViewGroup,
                false)
        val myToolbar = v.findViewById(R.id.my_toolbar) as Toolbar
        val tabLayout = v.findViewById(R.id.tabs) as TabLayout
        val viewPager = v.findViewById(R.id.viewpager) as ViewPager
        val mLayoutManager = LinearLayoutManager(activity)

        setHasOptionsMenu(true)

        // setup toolbar
        myToolbar.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))
        myToolbar.setTitle(resources.getString(R.string.title_matches))
        (activity as AppCompatActivity).setSupportActionBar(myToolbar)

        // add tabLayout
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())

        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        viewPager.adapter = PagerAdapter(fragmentManager!!, tabLayout.tabCount)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setTabMode(TabLayout.MODE_FIXED)
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL)
        tabLayout.getTabAt(0)?.setText(resources.getString(R.string.title_next_match))
        tabLayout.getTabAt(1)?.setText(resources.getString(R.string.title_prev_match))
        tabLayout.setTabTextColors(
                ContextCompat.getColor(context!!, R.color.white),
                ContextCompat.getColor(context!!, R.color.silveryoung)
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

        return v
    }

    inner class PagerAdapter(fm: FragmentManager, internal var mNumOfTabs: Int)
        : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> return NextMatchFragment()
                1 -> return PrevMatchFragment()

                else -> return null
            }
        }

        override fun getCount(): Int {
            return mNumOfTabs
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
