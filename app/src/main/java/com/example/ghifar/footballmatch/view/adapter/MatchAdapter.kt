package com.example.ghifar.footballmatch.view.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.eventleaguemodel.Event
import com.example.ghifar.footballmatch.presenter.Constant
import com.example.ghifar.footballmatch.presenter.Utils

/**
 * Created by Alhudaghifari on 1:34 17/09/18
 */

class MatchAdapter(internal var data: List<Event>, internal var context: Context?,
                   internal var type: Int?)
    : RecyclerView.Adapter<MatchAdapter.ArtikelViewHolder>() {

    private val TAG = MatchAdapter::class.java.simpleName

    private var mOnArtikelClickListener: OnArtikelClickListener? = null
    private var mOnNotificationClickListener: OnNotificationClickListener? = null

    private var utils = Utils()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_schedule_match, parent,
                        false)
        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder,
                                  position: Int) {
        val artikel = data[position]
        var strTime = artikel.strTime
        var formattedClock = ""
        var dateIndo = ""

        if (strTime != null) {
            Log.d(TAG, "strTime : ${artikel.strTime}")
            if (strTime.length > 6) {
                strTime = artikel.strTime?.substring(0, 8)
                formattedClock = utils.getClock(strTime)
            } else {
                formattedClock = strTime
            }
        }

        if (artikel.dateEvent != null)
            dateIndo = utils.getDate(artikel.dateEvent)

        Log.d(TAG, "dateIndo : $dateIndo, $formattedClock")

        holder.tvTanggalMain.text = dateIndo
        holder.tvJamMain.text = formattedClock
        holder.tvNamaTimKiri.text = artikel.strHomeTeam
        holder.tvNamaTimKanan.text = artikel.strAwayTeam
        holder.tvScoreTimKiri.text = artikel.intHomeScore
        holder.tvScoreTimKanan.text = artikel.intAwayScore

        Log.d(TAG, "position now : $position")

        if (type == Constant.FAVMATCH) {
            holder.ivLogoTimKanan.visibility = View.GONE
            holder.ivLogoTimKiri.visibility = View.GONE
        } else if (type == Constant.NEXTMATCH) {
            holder.ivNotification.visibility = View.VISIBLE
        } else if (type == Constant.SEARCHMATCH) {
            if (artikel.intHomeScore == null)
                holder.ivNotification.visibility = View.VISIBLE
        }

        holder.linlayItemScheduleMatch.setOnClickListener {
            val handler = Handler()
            handler.postDelayed({
                Log.d(TAG, "position clicked : $position")
                if (mOnArtikelClickListener != null) {
                    mOnArtikelClickListener!!.onClick(position, artikel)
                }
            }, 250)
        }

        holder.ivNotification.setOnClickListener {
            val handler = Handler()
            handler.postDelayed({
                Log.d(TAG, "position clicked : $position")
                if (mOnNotificationClickListener != null) {
                    mOnNotificationClickListener!!.onClick(position, artikel)
                }
            }, 250)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ArtikelViewHolder(mViewContainer: View) : RecyclerView.ViewHolder(mViewContainer) {
        internal var linlayItemScheduleMatch : LinearLayout

        internal var tvTanggalMain: TextView
        internal var tvJamMain: TextView
        internal var tvNamaTimKiri: TextView
        internal var tvNamaTimKanan: TextView
        internal var tvScoreTimKiri: TextView
        internal var tvScoreTimKanan: TextView

        internal var ivLogoTimKanan: ImageView
        internal var ivLogoTimKiri: ImageView
        internal var ivNotification: ImageView

        init {
            linlayItemScheduleMatch = mViewContainer.findViewById(R.id.linlayItemScheduleMatch)

            tvTanggalMain = mViewContainer.findViewById(R.id.tvTanggalMain)
            tvJamMain = mViewContainer.findViewById(R.id.tvJamMain)
            tvNamaTimKiri = mViewContainer.findViewById(R.id.tvNamaTimKiri)
            tvNamaTimKanan = mViewContainer.findViewById(R.id.tvNamaTimKanan)
            tvScoreTimKanan = mViewContainer.findViewById(R.id.tvScoreTimKanan)
            tvScoreTimKiri = mViewContainer.findViewById(R.id.tvScoreTimKiri)

            ivLogoTimKanan = mViewContainer.findViewById(R.id.ivLogoTimKanan)
            ivLogoTimKiri = mViewContainer.findViewById(R.id.ivLogoTimKiri)
            ivNotification = mViewContainer.findViewById(R.id.ivNotification)
        }
    }

    /**
     * interface ketika container di click
     */
    interface OnArtikelClickListener {
        fun onClick(posisi: Int, model: Event)
    }

    /**
     * listener artikel di klik
     * @param onArtikelClickListener onartikelclicklistener
     */
    fun setOnArtikelClickListener(onArtikelClickListener: OnArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener
    }

    interface OnNotificationClickListener {
        fun onClick(posisi: Int, model: Event)
    }

    fun setOnNotificationClickListener(onNotificationClickListener: OnNotificationClickListener) {
        mOnNotificationClickListener = onNotificationClickListener
    }
}
