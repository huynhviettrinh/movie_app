@file:Suppress("DEPRECATION")

package com.example.appmovieandroid

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.appmovieandroid.adapters.EpisodeAdapter
import com.example.appmovieandroid.adapters.ViewPagerAdapter
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.databinding.*
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.movie_detail.*
import com.example.appmovieandroid.models.view_model.FragmentViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.collect.ImmutableList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var bindingCustomPlayerBinding: CustomPlayerBinding
    private val fragmentViewModel: FragmentViewModel by viewModels()
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: ExoPlayer

    //    private lateinit var mediaSource: MediaSource
    private lateinit var mFullScreenDialog: Dialog


    private lateinit var textDuration: TextView
    private lateinit var buttonPlayPause: ImageButton
    private lateinit var textMovieName: TextView
    private lateinit var rewindBtn: ImageButton
    private lateinit var forwardBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var seekBarCustom: SeekBar
    private lateinit var blockLockScreen: LinearLayout
    private lateinit var blockRotateScreen: LinearLayout
    private lateinit var blockPlayer: View
    private lateinit var moreBtn: ImageButton
    private lateinit var btnChangeSub: View


    private var isHideLockIcon = true
    private lateinit var youTubePlayerView: YouTubePlayerView

    private lateinit var dialogLoading: AlertDialog

    companion object {
        var nameMovie = ""
        var positionEpisode = -1
        lateinit var loudnessEnhancer: LoudnessEnhancer

        //SPEED CHANGE
        private var speed: Float = 1.0f

        var listSub: Array<Subtitle> = arrayOf()
        private lateinit var listLanguage: Array<String>
        var selectedSubIndex = -1
        var linkM3u8 = ""
        var currentTimeVideo: Long = 0


        fun filterResolution(listResolution: List<Resolution>): Resolution {
            val resolution = Resolution("GROOT_LD","540P")
            for (resolutionItem in listResolution) {
                when (resolutionItem.code) {
                    "GROOT_HD" -> return resolutionItem
                    "GROOT_SD" -> return resolutionItem
                    "GROOT_LD" -> return resolutionItem
                }
            }
            return resolution
        }

        fun filterSubtitle(listSubs: Array<Subtitle>): String {
            listSub = listSubs
            var sub = ""
            for (subItem in listSub) {
                if (subItem.languageAbbr == "en") {
                    sub = subItem.subtitlingUrl
                    return sub
                } else if (subItem.languageAbbr == "vi") {
                    sub = subItem.subtitlingUrl
                    return sub
                }
            }
            return sub
        }

        fun getListLanguageString(listSub: Array<Subtitle>): Array<String> {
            listLanguage = Array(listSub.size) { "" }
            for ((i, element) in listSub.withIndex()) {
                listLanguage[i] = element.language
            }
            return listLanguage
        }

    }

    private fun assignViewValue() {
        buttonPlayPause = findViewById(R.id.videoView_play_pause_btn)
        textDuration = findViewById(R.id.videoView_endtime)
        textMovieName = findViewById(R.id.videoView_title)
        rewindBtn = findViewById(R.id.videoView_rewind)
        forwardBtn = findViewById(R.id.videoView_forward)
        seekBarCustom = findViewById(R.id.videoView_seekbar)
        blockLockScreen = findViewById(R.id.videoView_lock_screen)
        blockRotateScreen = findViewById(R.id.videoView_rotation)
        backBtn = findViewById(R.id.videoView_go_back)
        blockPlayer = findViewById(R.id.blockPlayer)
        moreBtn = findViewById(R.id.videoView_more)
        btnChangeSub = findViewById(R.id.changeSubBtn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = ActivityMovieDetailBinding.inflate(layoutInflater)
            bindingCustomPlayerBinding = CustomPlayerBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.blockMovieDetail.smoothScrollTo(0, 0)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            // Gan gia tri cac view
            assignViewValue()

            initRunnableHandleDuration()



            // Data from adapter
            val movieDetail = intent?.getSerializableExtra("MovieDetail")!! as MovieDetail
            nameMovie = movieDetail.name

            // for youtube
            initPlayerYoutube(movieDetail.trailerId)

            initListEpisode(movieDetail.episodeDetails, movieDetail.episodeCount)

            renderData(
                movieDetail.mainImage,
                movieDetail.bannerImage,
                movieDetail.introduction,
                movieDetail.score,
                movieDetail.name
            )

            setTabLayout(movieDetail.relatedSeries, movieDetail.recommendMovies)

            hideSystemBar()

            // Xu ly cac hanh dong khi click
            handleClickAction(movieDetail.episodeDetails)

            handleEventPlayer()

        } catch (e: Exception) {
            Log.e("ERR", e.toString())
        }
    }

    private fun showConfirmationDialog(listStringLanguage: Array<String>) {

        MaterialAlertDialogBuilder(this)
            .setTitle("Select subtitle")
            .setSingleChoiceItems(listStringLanguage, selectedSubIndex) { _, index ->
                selectedSubIndex = index
                currentTimeVideo = simpleExoPlayer.currentPosition

            }
            .setPositiveButton("OK") { _, _ ->
                if (selectedSubIndex != -1) {
                    simpleExoPlayer.clearMediaItems()
                    createMedia(linkM3u8, listSub[selectedSubIndex].subtitlingUrl)
                    Log.v("VVV", currentTimeVideo.toString())
                    simpleExoPlayer.seekTo(currentTimeVideo)
                    playVideo()
                }
            }
            .show()
        pauseVideo()
    }

    private fun handleClickAction(listEpisode: List<EpisodeDetail>) {

        binding.playVideo.setOnClickListener {

            CompanionObject.definition = filterResolution(listEpisode[0].resolution).code
            Log.e("VVV", filterResolution(listEpisode[0].resolution).code)

            CompanionObject.episodeId = listEpisode[0].episodeId
            try {
                listSub = listEpisode[0].subtitles.toTypedArray()
                callGetMovieMedia(listEpisode[0].subtitles.toTypedArray())
            } catch (e: Exception) {
                Log.e("EEE", e.toString())
            }
        }

        binding.lockBackIcon.setOnClickListener {
            exoPlayerView.useController = true
            exoPlayerView.showController()
            binding.blockHideIconLock.visibility = View.GONE
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.blockHideIconLock.setOnClickListener {
            if (isHideLockIcon) {
                binding.lockBackIcon.visibility = View.INVISIBLE
                isHideLockIcon = false
            } else {
                binding.lockBackIcon.visibility = View.VISIBLE
                isHideLockIcon = true
            }
        }

        btnChangeSub.setOnClickListener {
            showConfirmationDialog(getListLanguageString(listSub))
        }
    }


    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun handleEventPlayer() {

        rewindBtn.setOnClickListener {
            simpleExoPlayer.seekTo(simpleExoPlayer.currentPosition - 10000)
        }

        forwardBtn.setOnClickListener {
            simpleExoPlayer.seekTo(simpleExoPlayer.currentPosition + 10000)
        }

        buttonPlayPause.setOnClickListener {
            if (simpleExoPlayer.isPlaying) {
                handler.removeCallbacks(runnable)
                pauseVideo()
            } else {
                handler.postDelayed(runnable, 1000)
                playVideo()
            }
        }

        seekBarCustom.setOnSeekBarChangeListener((object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    simpleExoPlayer.seekTo((progress * simpleExoPlayer.duration) / 100)
                    textDuration.text =
                        convertTimeMedia((progress * simpleExoPlayer.duration.toInt()) / 100)
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {
                pauseVideo()
                handler.removeCallbacks(runnable)
            }

            override fun onStopTrackingTouch(seek: SeekBar?) {
                playVideo()
                handler.postDelayed(runnable, 1000)
            }
        }))

        blockLockScreen.setOnClickListener {
            exoPlayerView.useController = false
            exoPlayerView.hideController()
            binding.blockHideIconLock.visibility = View.VISIBLE
        }

        blockRotateScreen.setOnClickListener {
            openFullscreenDialog()

        }

        backBtn.setOnClickListener {
            closeFullscreenDialog()
        }

        moreBtn.setOnClickListener {
            pauseVideo()

            val customDialog = LayoutInflater.from(this)
                .inflate(R.layout.more_features, binding.blockPlayer, false)
            val bindingMF = MoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(this).setView(customDialog)
                .setOnCancelListener {
                    playVideo()
                }
                .setBackground(ColorDrawable(0x80000000.toInt()))
                .create()
            dialog.show()

            //TODO audioBooster
            bindingMF.audioBooster.setOnClickListener {
                dialog.dismiss()
                val customDialogB =
                    LayoutInflater.from(this).inflate(R.layout.booster, binding.root, false)
                val bindingB = BoosterBinding.bind(customDialogB)
                val dialogB = MaterialAlertDialogBuilder(this).setView(customDialogB)
                    .setOnCancelListener { playVideo() }
                    .setPositiveButton("OKE") { self, _ ->
                        loudnessEnhancer.setTargetGain(bindingB.seekbar.progress * 100)
                        self.dismiss()
                    }
                    .setBackground(ColorDrawable(0x80000000.toInt()))
                    .create()
                dialogB.show()
                bindingB.seekbar.progress = loudnessEnhancer.targetGain.toInt() / 100
                bindingB.tv.text = "Audio booster \n\n ${loudnessEnhancer.targetGain.toInt() / 10}"
                bindingB.seekbar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        bindingB.tv.text = "Audio booster \n\n${p1 * 10}"
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                })
                playVideo()
            }

            //TODO CHANGE SPEED
            bindingMF.speedBtn.setOnClickListener {
                dialog.dismiss()
                val customDialogS =
                    LayoutInflater.from(this).inflate(R.layout.speed_dialog, binding.root, false)
                val bindingS = SpeedDialogBinding.bind(customDialogS)
                val dialogS = MaterialAlertDialogBuilder(this).setView(customDialogS)
                    .setOnCancelListener { playVideo() }
                    .setPositiveButton("OKE") { self, _ ->
                        self.dismiss()
                    }
                    .setBackground(ColorDrawable(0x80000000.toInt()))
                    .create()
                dialogS.show()
                playVideo()

                bindingS.addBtn.setOnClickListener {
                    changeSpeed(true)
                    bindingS.tvSpeed.text = "${DecimalFormat("#.##").format(speed)}X"
                }

                bindingS.minusBtn.setOnClickListener {
                    changeSpeed(false)
                    bindingS.tvSpeed.text = "${DecimalFormat("#.##").format(speed)}X"
                }
            }

        }
    }


    private fun initPlayerYoutube(videoId: String) {
        val id = videoId.ifEmpty { "o8l6A-cZkY4" }

        youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(id, 0F)
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                super.onVideoId(youTubePlayer, videoId)
                binding.loadingTrailer.visibility = View.GONE
                binding.youtubePlayerView.visibility = View.VISIBLE

            }
        })
    }

    private fun initFullscreenDialog() {
        mFullScreenDialog =
            object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    closeFullscreenDialog()
                    super.onBackPressed()
                }
            }
    }

    private fun openFullscreenDialog() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)
        mFullScreenDialog.addContentView(
            exoPlayerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        mFullScreenDialog.show()
    }

    private fun closeFullscreenDialog() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)
        (blockPlayer as ConstraintLayout).addView(exoPlayerView)
        mFullScreenDialog.dismiss()
    }


    // Dung video
    private fun pauseVideo() {
        buttonPlayPause.setImageResource(R.drawable.ic_play)
        simpleExoPlayer.pause()
    }


    // Phat tiep video
    private fun playVideo() {
        buttonPlayPause.setImageResource(R.drawable.netflix_pause_button)
        simpleExoPlayer.play()

    }

    // handle ham su ly duration
    private fun handleDuration() {
        handler.postDelayed(runnable, 500)
    }

    // khoi tao runnable xu ly duration
    private fun initRunnableHandleDuration() {
        runnable = Runnable {
            textDuration.text = convertTimeMedia(simpleExoPlayer.currentPosition.toInt())
            seekBarCustom.progress =
                ((simpleExoPlayer.currentPosition * 100) / simpleExoPlayer.duration.toInt()).toInt()
            handler.postDelayed(runnable, 500)
        }
    }

    private fun convertTimeMedia(secondTime: Int): String {
        val hours: Int
        var minutes: Int
        val seconds: Int

        val currentSecondsPosition: Int = (secondTime / 1000)

        seconds = currentSecondsPosition % 60
        minutes = currentSecondsPosition / 60

        if (minutes > 59) {
            minutes = (currentSecondsPosition / 60) % 60
        }
        hours = currentSecondsPosition / 3600

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun setInit(numb: Int) {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, numb)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tableLayout.setupWithViewPager(binding.viewPager)
        binding.tableLayout.tag = 1
    }

    //Hide system bar
    @Suppress("DEPRECATION")
    private fun hideSystemBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun renderData(
        mainImage: String,
        bannerImage: String,
        description: String,
        score: Double,
        name: String
    ) {
        Glide.with(binding.root)
            .load(bannerImage)
            .placeholder(R.drawable.animation_loading)
            .into(binding.banner)

        Glide.with(binding.root)
            .load(mainImage)
            .placeholder(R.drawable.animation_loading)
            .into(binding.poster)
        binding.title.text = name
        binding.description.text = description
        binding.score.text = score.toString()

    }

    private fun setTabLayout(
        relatedSeries: List<Movie>,
        recommendMovie: List<Movie>
    ) {
        if (relatedSeries.isEmpty()) {
            fragmentViewModel.setMovieRecommend(recommendMovie)
            setInit(1)
        } else {
            fragmentViewModel.setMovieRelatedSeries(relatedSeries)
            fragmentViewModel.setMovieRecommend(recommendMovie)
            setInit(2)
        }
    }

    private fun initListEpisode(listEpisode: List<EpisodeDetail>, episodeCount: Int) {
        if (episodeCount > 0) {
            binding.playVideo.visibility = View.GONE
            binding.recyclerviewEpisode.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerviewEpisode.adapter = EpisodeAdapter(
                listEpisode,
            ) { callGetMovieMedia(listEpisode[positionEpisode].subtitles.toTypedArray()) }
        }
    }


    private fun initPlayer() {
        exoPlayerView = binding.exoPlayerView
        simpleExoPlayer = ExoPlayer.Builder(this).build()
        exoPlayerView.player = simpleExoPlayer
        loudnessEnhancer = LoudnessEnhancer(simpleExoPlayer.audioSessionId)
        loudnessEnhancer.setTargetGain(0)
    }

    private fun createMedia(m3u8: String, sub: String) {
        val assetSrtUri = Uri.parse((sub))
        val assetVideoUri = Uri.parse((m3u8))


        val subtitle = MediaItem.SubtitleConfiguration.Builder(assetSrtUri)
            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(assetVideoUri)
            .setSubtitleConfigurations(ImmutableList.of(subtitle))
            .build()

        simpleExoPlayer.setMediaItem(mediaItem)

        simpleExoPlayer.prepare()
        simpleExoPlayer.play()


//        simpleExoPlayer.seekTo(0)
//        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
//            this,
//            Util.getUserAgent(this, applicationInfo.name)
//        )
//        mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
//            MediaItem.fromUri(m3u8)
//        )
//        simpleExoPlayer.setMediaSource(mediaSource)
//        simpleExoPlayer.prepare()


    }


    private var callGetMovieMedia: (listSub: Array<Subtitle>) -> Unit = { listSubs ->
        val sub = filterSubtitle(listSubs)

        val scopeMain = CoroutineScope(Dispatchers.Main)
        createDialogLoading()
        dialogLoading.show()
        pauseVideo()

        scopeMain.launch {
            CompanionObject.getMovieMedia {
                linkM3u8 = it.mediaUrl
                binding.blockPlayer.visibility = View.VISIBLE
                binding.blockTrailer.visibility = View.GONE
                textMovieName.text = nameMovie
                youTubePlayerView.release()
                createMedia(linkM3u8, sub)
                handleDuration()
                dialogLoading.dismiss()
            }
        }
        playVideo()
    }

    private fun createDialogLoading() {
        val successDialog = LayoutInflater.from(this)
            .inflate(R.layout.layout_loading_dialog, null, false)
        dialogLoading = MaterialAlertDialogBuilder(this).setView(successDialog)
            .setBackground(
                ColorDrawable(0x00000000)
            ).create()
    }


    //TODO CHANGE SPEED
    private fun changeSpeed(isIncrement: Boolean) {
        if (isIncrement) {
            if (speed <= 2.9f) {
                speed += 0.10f
            }
        } else {
            if (speed > 0.20f) {
                speed -= 0.10f
            }
        }
        simpleExoPlayer.setPlaybackSpeed(speed)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        initPlayer()
        initFullscreenDialog()
        simpleExoPlayer.playWhenReady = true
        playVideo()
    }

    override fun onPause() {
        super.onPause()
        pauseVideo()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        pauseVideo()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()
        selectedSubIndex = -1
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}
