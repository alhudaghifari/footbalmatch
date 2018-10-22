package com.example.ghifar.footballmatch.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.playermodel.Player
import com.example.ghifar.footballmatch.presenter.Utils
import com.example.ghifar.footballmatch.presenter.player.DetailPlayerPresenter
import com.example.ghifar.footballmatch.presenter.player.PlayersInterface
import kotlinx.android.synthetic.main.activity_player_detail.*
import kotlinx.android.synthetic.main.layout_detail_player.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.example.ghifar.footballmatch.presenter.Constant


class PlayerDetailActivity : AppCompatActivity(), PlayersInterface {

    private val TAG = PlayerDetailActivity::class.java.simpleName

    private lateinit var presenter: DetailPlayerPresenter
    private lateinit var myToolbar: Toolbar
    private var items: List<Player> = mutableListOf()
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        myToolbar = findViewById(R.id.my_toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat
                    .getColor(this@PlayerDetailActivity, R.color.blackandblue2)
        }

        myToolbar.setTitle("")
        setSupportActionBar(myToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        id = intent.getStringExtra(Constant.ID)

        presenter = DetailPlayerPresenter(this)
        presenter.getDetailPlayerById(id)

        listenerFunction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_refresh -> {
                presenter.getDetailPlayerById(id)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.disposeThis()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
        progressBar.visibility = View.GONE
    }

    override fun showTeamList(data: List<Player>) {
        var utils = Utils()
        items = data
        Glide.with(this)
                .load(data[0].strThumb)
                .into(ivPhotoBanner)

        var weight = data[0].strWeight!!.split(" ")[0]        //.substring(0, data[0].strWeight!!.indexOf(" "))
        var height = data[0].strHeight!!.split(" ")[0] //.substring(0, data[0].strHeight!!.indexOf(" "))

        tvWeight.text = weight
        tvHeight.text = height
        tvPosition.text = data[0].strPosition
        myToolbar.setTitle(data[0].strPlayer)
        myToolbar.setTitleTextColor(resources.getColor(R.color.white))

        wvDescription.loadData(utils.getHtmlStringFormat(data[0].strDescriptionEN),
                "text/html", "utf-8")

        if (!data[0].strTwitter.equals("")) {
            ivTwitter.visibility = View.VISIBLE
        }

        if (!data[0].strInstagram.equals("")) {
            ivInstagram.visibility = View.VISIBLE
        }

        if (!data[0].strFacebook.equals("")) {
            ivFacebook.visibility = View.VISIBLE
        }

        if (!data[0].strYoutube.equals("")) {
            ivYoutube.visibility = View.VISIBLE
        }
    }

    override fun showError(msg: String) {
        Log.d(TAG, "showError : " + msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun listenerFunction() {
        ivTwitter.setOnClickListener {
            val url = "https://" + items[0].strTwitter
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        ivInstagram.setOnClickListener {
            val url = "https://" + items[0].strInstagram
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        ivFacebook.setOnClickListener {
            val url = "https://" + items[0].strFacebook
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        ivYoutube.setOnClickListener {
            val url = "https://" + items[0].strYoutube
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}
