package net.xblacky.animexstream.ui.main.player

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_video_player.*
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.model.Content

class VideoPlayerActivity : AppCompatActivity(), VideoPlayerListener {
    private var url =
        "https://hls11xx.cdnfile.info/videos/hls/g62z5V-FaVOCLJV13hPCwg/1583017051/136872/c751bab1939a2b83020565e1ac242896/sub.923.m38"
    private lateinit var viewModel: VideoPlayerViewModel
    private var episodeNumber: String? = ""
    private var animeName: String? = ""
    private lateinit var content: Content
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)
        getExtra()
//        (playerFragment as VideoPlayerFragment).updateContent(Content(
//            url = url,
//            episodeNumber = "153"
//        ))
        setObserver()
        goFullScreen()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            goFullScreen()
        }
    }

    private fun getExtra() {
        val url = intent.extras?.getString("episodeUrl")
        episodeNumber = intent.extras?.getString("episodeNumber")
        animeName = intent.extras?.getString("animeName")
        viewModel.updateEpisodeContent(
            Content(
                animeName = animeName ?: "",
                episodeUrl = url,
                episodeName = animeName!! + " (" + episodeNumber!! + ")",
                url = ""
                )
        )
        viewModel.fetchEpisodeMediaUrl()
    }

    private fun setObserver() {
        viewModel.liveContent.observe(this, Observer {
            this.content = it
            it?.let {
                if(!it.url.isNullOrEmpty()){
                    (playerFragment as VideoPlayerFragment).updateContent(it)
                }

            }
        })
    }

    private fun goFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun updateWatchedValue(content: Content) {
       viewModel.saveContent(content)
    }

    override fun playNextEpisode() {
        viewModel.updateEpisodeContent(
            Content(
                episodeUrl = content.nextEpisodeUrl,
                episodeName = "$animeName (EP ${incrimentEpisodeNumber(content.episodeName!!)})",
                url="",
                animeName = content.animeName
            )
        )
        viewModel.fetchEpisodeMediaUrl()

    }

    override fun playPreviousEpisode(){

        viewModel.updateEpisodeContent(
            Content(
                episodeUrl = content.previousEpisodeUrl,
                episodeName = "$animeName (EP ${decrimentEpisodeNumber(content.episodeName!!)})",
                url="",
                animeName = content.animeName
            )
        )
        viewModel.fetchEpisodeMediaUrl()
    }

    private fun incrimentEpisodeNumber(episodeName: String): String{
        return try{
            val episodeString = episodeName.substring(episodeName.lastIndexOf(' ')+1, episodeName.lastIndexOf(')'))
            var episodeNumber = Integer.parseInt(episodeString)
            episodeNumber++
            episodeNumber.toString()

        }catch (obe: ArrayIndexOutOfBoundsException){
            ""
        }
    }
    private fun decrimentEpisodeNumber(episodeName: String): String{
        return try{
            val episodeString = episodeName.substring(episodeName.lastIndexOf(' ')+1, episodeName.lastIndexOf(')'))
            var episodeNumber = Integer.parseInt(episodeString)
            episodeNumber--
            episodeNumber.toString()

        }catch (obe: ArrayIndexOutOfBoundsException){
            ""
        }
    }

}