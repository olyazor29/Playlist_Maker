package com.olyaz.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.olyaz.playlistmaker.databinding.ActivityAudioplayerBinding

const val TRACK_KEY = "CURRENT_TRACK_VALUE"

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private var currentTrack: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playerToolbar.setNavigationOnClickListener {
            finish()
        }

        val trackJson = intent.getStringExtra(TRACK_KEY)
        currentTrack = Gson().fromJson(trackJson, Track::class.java)

        if (currentTrack != null) {
            showCurrentTrackData(currentTrack!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TRACK_KEY, Gson().toJson(currentTrack))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val trackJson = savedInstanceState.getString(TRACK_KEY)
        if (trackJson != null) {
            currentTrack = Gson().fromJson(trackJson, Track::class.java)
        }
    }

    private fun showCurrentTrackData(track: Track) {
        binding.tvTrackName.text = currentTrack!!.trackName
        binding.tvTrackArtist.text = currentTrack!!.artistName
        binding.tvTrackYear.text = currentTrack!!.releaseDate.substring(0,4)
        binding.tvTrackGenre.text = currentTrack!!.primaryGenreName
        binding.tvTrackCountry.text = currentTrack!!.country
        binding.tvTrackDuration.text = convertTimeToRightFormat(currentTrack!!.trackTimeMillis)

        if (currentTrack!!.collectionName.isNotEmpty()) {
            binding.collectioGroup.show()
            binding.tvTrackCollection.text = currentTrack!!.collectionName
        } else {
            binding.collectioGroup.gone()
        }

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_cover)
            .error(R.drawable.placeholder_cover)
            .fitCenter()
            .into(binding.ivTrackCover)
    }
}