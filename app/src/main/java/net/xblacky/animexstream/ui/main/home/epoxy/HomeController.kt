package net.xblacky.animexstream.ui.main.home.epoxy

import android.content.Intent
import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.Carousel.setDefaultGlobalSnapHelperFactory
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import net.xblacky.animexstream.R
import net.xblacky.animexstream.ui.main.animeinfo.AnimeInfoActivity
import net.xblacky.animexstream.ui.main.player.VideoPlayerActivity
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.epoxy.AnimeCommonModel_
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import net.xblacky.animexstream.utils.model.HomeScreenModel


class HomeController : TypedEpoxyController<ArrayList<HomeScreenModel>>() {


    override fun buildModels(data: ArrayList<HomeScreenModel>) {

        //TODO Change Header to AutoModel
//        HomeHeaderModel_()
//            .id("header")
//            .addTo(this)

        data.forEach { homeScreenModel ->

            AnimeMiniHeaderModel_()
                .id(homeScreenModel.typeValue)
                .typeName(homeScreenModel.type)
                .addIf(!homeScreenModel.animeList.isNullOrEmpty(),this)



            when (homeScreenModel.typeValue) {

                C.TYPE_MOVIE, C.TYPE_NEW_SEASON -> {
                    val movieModelList: ArrayList<AnimeCommonModel_> = ArrayList()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it

                        movieModelList.add(
                            AnimeCommonModel_()
                                .id(animeMetaModel.ID)
                                .clickListener { model, _, clickedView, _ ->
                                    clicked(model = model.animeMetaModel(), clickedView = clickedView)
                                }
                                .animeMetaModel(animeMetaModel)
                        )
                    }
                    setDefaultGlobalSnapHelperFactory(null)

                        CarouselModel_()
                            .id(homeScreenModel.hashCode())
                            .models(movieModelList)
                            .padding(Carousel.Padding.dp(30,0,40,0,20))
                            .addTo(this)

                }
                C.TYPE_POPULAR_ANIME -> {
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it


                        AnimePopularModel_()
                            .id(animeMetaModel.ID)
                            .clickListener { model, _, clickedView, _ ->
                                clicked(model = model.animeMetaModel(), clickedView = clickedView)
                            }
                            .animeMetaModel(animeMetaModel)
                            .addTo(this)
                    }

                }
                else ->{
                    val recentModelList: ArrayList<AnimeSubDubModel2_> = ArrayList()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it
                        recentModelList.add(
                        AnimeSubDubModel2_()
                            .id(animeMetaModel.ID)
                            .clickListener { model, _, clickedView, _ ->
                                recentSubDubClick(model,clickedView)
                            }
                            .animeMetaModel(animeMetaModel)
                        )
                    }
                    CarouselModel_()
                        .id(homeScreenModel.hashCode())
                        .models(recentModelList)
                        .padding(Carousel.Padding.dp(30,0,40,0,20))
                        .addTo(this)
                }
            }

        }

    }

    private fun recentSubDubClick(model: AnimeSubDubModel2_, clickedView: View){
        when(clickedView.id){
            R.id.backgroundImage->{
                val intent = Intent(clickedView.context, VideoPlayerActivity::class.java)
                intent.putExtra("episodeUrl", model.animeMetaModel().episodeUrl)
                intent.putExtra("episodeNumber",model.animeMetaModel().episodeNumber)
                intent.putExtra("animeName",model.animeMetaModel().title)
                clickedView.context.startActivity(intent)
            }
            R.id.animeTitle->{
                val intent = Intent(clickedView.context, AnimeInfoActivity::class.java)
                intent.putExtra("categoryUrl", model.animeMetaModel().categoryUrl)
                clickedView.context.startActivity(intent)
            }
        }

    }

    private fun clicked(model: AnimeMetaModel, clickedView: View){
        val intent = Intent(clickedView.context, AnimeInfoActivity::class.java)
        intent.putExtra("categoryUrl", model.categoryUrl)
        clickedView.context.startActivity(intent)
    }

}