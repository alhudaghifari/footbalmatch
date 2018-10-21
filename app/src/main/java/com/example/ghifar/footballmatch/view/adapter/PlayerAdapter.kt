package com.example.ghifar.footballmatch.view.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ghifar.footballmatch.R
import com.example.ghifar.footballmatch.model.playermodel.Player


/**
 * Created by Alhudaghifari on 5:52 22/10/18
 *
 */
class PlayerAdapter(internal var data: List<Player>, internal var context: Context?,
                    internal var type: Int?)
    : RecyclerView.Adapter<PlayerAdapter.ArtikelViewHolder>() {

    private val TAG = PlayerAdapter::class.java.simpleName

    private var mOnArtikelClickListener: OnArtikelClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_player, parent,
                        false)
        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder,
                                  position: Int) {
        val artikel = data[position]
        Glide.with(context!!)
                .load(artikel.strThumb)
                .into(holder.ivPhotoPlayer)
        holder.tvPlayerName.text = artikel.strPlayer
        holder.tvPosition.text = artikel.strPosition

        holder.rellayHolder.setOnClickListener {
            val handler = Handler()
            handler.postDelayed({
                Log.d(TAG, "position clicked : $position")
                if (mOnArtikelClickListener != null) {
                    mOnArtikelClickListener!!.onClick(position, artikel)
                }
            }, 250)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ArtikelViewHolder(mViewContainer: View) : RecyclerView.ViewHolder(mViewContainer) {
        internal var rellayHolder : RelativeLayout

        internal var tvPosition: TextView
        internal var tvPlayerName: TextView

        internal var ivPhotoPlayer: ImageView

        init {
            rellayHolder = mViewContainer.findViewById(R.id.rellayHolder)

            tvPosition = mViewContainer.findViewById(R.id.tvPosition)
            tvPlayerName = mViewContainer.findViewById(R.id.tvPlayerName)

            ivPhotoPlayer = mViewContainer.findViewById(R.id.ivPhotoPlayer)
        }
    }

    /**
     * interface ketika container di click
     */
    interface OnArtikelClickListener {
        fun onClick(posisi: Int, model: Player)
    }

    /**
     * listener artikel di klik
     * @param onArtikelClickListener onartikelclicklistener
     */
    fun setOnArtikelClickListener(onArtikelClickListener: OnArtikelClickListener) {
        mOnArtikelClickListener = onArtikelClickListener
    }
}