package com.olyaz.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackHistoryAdapter(
    private val onTrackClick: (Track) -> Unit
): RecyclerView.Adapter<TrackViewHolder>() {

    var tracksHistory = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracksHistory.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksHistory[position])

        holder.itemView.setOnClickListener {
            onTrackClick(tracksHistory[position])
        }
    }

}