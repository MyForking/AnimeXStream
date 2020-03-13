package net.xblacky.animexstream.ui.main.search.epoxy

import android.content.Intent
import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import net.xblacky.animexstream.ui.main.animeinfo.AnimeInfoActivity
import net.xblacky.animexstream.utils.epoxy.AnimeCommonModel_
import net.xblacky.animexstream.utils.epoxy.LoadingModel_
import net.xblacky.animexstream.utils.model.AnimeMetaModel

class SearchController : Typed2EpoxyController<ArrayList<AnimeMetaModel>, Boolean>() {


    override fun buildModels(data: ArrayList<AnimeMetaModel>?, isLoading: Boolean) {
        data?.forEach { animeMetaModel ->
            AnimeCommonModel_()
                .id(animeMetaModel.ID)
                .animeMetaModel(animeMetaModel)
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount / totalSpanCount }
                .clickListener { model, _, clickedView, _ ->
                    startInfoActivity(model, clickedView)
                }
                .addTo(this)
        }
        if(!data.isNullOrEmpty()){
            LoadingModel_()
                .id("loading")
                .spanSizeOverride { totalSpanCount, _, _ ->
                    totalSpanCount
                }
                .addIf(isLoading,this)
        }
    }

    private fun startInfoActivity(model: AnimeCommonModel_, clickedView: View) {
        val intent = Intent(clickedView.context, AnimeInfoActivity::class.java)
        intent.putExtra("categoryUrl", model.animeMetaModel().categoryUrl)
        clickedView.context.startActivity(intent)
    }

}