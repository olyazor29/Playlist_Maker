package com.olyaz.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val trackInfo: TextView = itemView.findViewById(R.id.trackInfoTextView)
    private val trackCover: ImageView = itemView.findViewById(R.id.trackCoverImageView)

    fun bind(model: Track) {
        trackName.text = model.trackName
        val trackTimeAsString = convertTimeToRightFormat(model.trackTimeMillis)
        trackInfo.text = "${model.artistName} • $trackTimeAsString"

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .error(R.drawable.placeholder_track)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(trackCover)

    }
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}

fun convertTimeToRightFormat(millis: Long): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}

