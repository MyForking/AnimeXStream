package net.xblacky.animexstream.ui.main.animeinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import net.xblacky.animexstream.utils.CommonViewModel
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.model.AnimeInfoModel
import net.xblacky.animexstream.utils.model.EpisodeModel
import net.xblacky.animexstream.utils.model.FavouriteModel
import net.xblacky.animexstream.utils.parser.HtmlParser
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber

class AnimeInfoViewModel : CommonViewModel() {

    private var categoryUrl: String? = null
    private var _animeInfoModel: MutableLiveData<AnimeInfoModel> = MutableLiveData()
    private var _episodeList: MutableLiveData<ArrayList<EpisodeModel>> = MutableLiveData()
    var episodeList: LiveData<ArrayList<EpisodeModel>> = _episodeList
    var animeInfoModel: LiveData<AnimeInfoModel> = _animeInfoModel
    private val animeInfoRepository = AnimeInfoRepository()
    private var compositeDisposable = CompositeDisposable()
    private var _isFavourite: MutableLiveData<Boolean> = MutableLiveData(false)
    var isFavourite: LiveData<Boolean> = _isFavourite

    private var _animeInfoLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var animeInfoLoading: LiveData<Boolean> = _animeInfoLoading
    private var _episodeLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var episodeLoading: LiveData<Boolean> = _episodeLoading


    fun fetchAnimeInfo() {
        _episodeLoading.value = true
        _animeInfoLoading.value = true
        updateErrorModel(false, null, false)
        categoryUrl?.let {
            compositeDisposable.add(
                animeInfoRepository.fetchAnimeInfo(it)
                    .subscribeWith(getAnimeInfoObserver(C.TYPE_ANIME_INFO))
            )
        }
    }

    private fun getAnimeInfoObserver(typeValue: Int): DisposableObserver<ResponseBody> {
        return object : DisposableObserver<ResponseBody>() {
            override fun onNext(response: ResponseBody) {
                if (typeValue == C.TYPE_ANIME_INFO) {
                    _animeInfoLoading.value = false
                    val animeInfoModel = HtmlParser.parseAnimeInfo(response = response.string())
                    _animeInfoModel.value = animeInfoModel
                    compositeDisposable.add(
                        animeInfoRepository.fetchEpisodeList(
                                id = animeInfoModel.id,
                                endEpisode = animeInfoModel.endEpisode,
                                alias = animeInfoModel.alias
                            )
                            .subscribeWith(getAnimeInfoObserver(C.TYPE_EPISODE_LIST))
                    )
                    _isFavourite.value = animeInfoRepository.isFavourite(animeInfoModel.id)


                } else if (typeValue == C.TYPE_EPISODE_LIST) {
                    _episodeLoading.value = false
                    _episodeList.value = HtmlParser.fetchEpisodeList(response = response.string())

                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

                if (typeValue == C.TYPE_ANIME_INFO) {
                    _animeInfoLoading.value = false
                    updateErrorModel(show = true, e = e, isListEmpty = false)
                } else {
                    updateErrorModel(show = true, e = e, isListEmpty = true)
                }

            }

        }
    }


    fun toggleFavourite() {
        if (_isFavourite.value!!) {
            animeInfoModel.value?.id?.let { animeInfoRepository.removeFromFavourite(it) }
            _isFavourite.value = false
        } else {
            saveFavourite()

        }
    }

    private fun saveFavourite() {
        val model = animeInfoModel.value
        animeInfoRepository.addToFavourite(
            FavouriteModel(
                ID = model?.id,
                categoryUrl = categoryUrl,
                animeName = model?.animeTitle,
                releasedDate = model?.releasedTime,
                imageUrl = model?.imageUrl
            )
        )
        _isFavourite.value = true
    }

    fun setUrl(url: String) {
        this.categoryUrl = url
    }

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        if(isFavourite.value!!){
            saveFavourite()
        }
        super.onCleared()
    }
}