package net.xblacky.animexstream.ui.main.animeinfo.epoxy

import android.content.Intent
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import net.xblacky.animexstream.ui.main.player.VideoPlayerActivity
import net.xblacky.animexstream.utils.model.EpisodeModel
import net.xblacky.animexstream.utils.model.WatchedEpisode

class AnimeInfoController : TypedEpoxyController<ArrayList<EpisodeModel>>(){
    var animeName: String = ""
    private lateinit var isWatchedHelper: net.xblacky.animexstream.utils.helper.WatchedEpisode
    override fun buildModels(data: ArrayList<EpisodeModel>?) {
        data?.forEach {
            EpisodeModel_()
                .id(it.episodeurl)
                .episodeModel(it)
                .clickListener { model, _, clickedView, _ ->
                    startVideoActivity(model.episodeModel(),clickedView)
                }
                .spanSizeOverride { totalSpanCount, _, _ ->
                    totalSpanCount/totalSpanCount
                }
                .watched(isWatchedHelper.isWatched(it.episodeurl.hashCode()))
                .watchedProgress(isWatchedHelper.getWatchedDuration(it.episodeurl.hashCode()))
                .addTo(this)
        }
    }

    fun setAnime(animeName: String){
        this.animeName = animeName
        isWatchedHelper = net.xblacky.animexstream.utils.helper.WatchedEpisode(animeName)
    }

    fun isWatchedHelperUpdated():Boolean{
        return ::isWatchedHelper.isInitialized
    }

    private fun startVideoActivity(episodeModel: EpisodeModel, clickedView: View){
        val intent = Intent(clickedView.context, VideoPlayerActivity::class.java)
        intent.putExtra("episodeUrl",episodeModel.episodeurl)
        intent.putExtra("episodeNumber",episodeModel.episodeNumber)
        intent.putExtra("animeName",animeName)
        clickedView.context.startActivity(intent)
    }

}