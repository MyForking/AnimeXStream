package net.xblacky.animexstream.ui.main.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import net.xblacky.animexstream.utils.CommonViewModel
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.model.Content
import net.xblacky.animexstream.utils.parser.HtmlParser
import okhttp3.ResponseBody

class VideoPlayerViewModel : CommonViewModel() {

    private val episodeRepository = EpisodeRepository()
    private var compositeDisposable = CompositeDisposable()
    private var _content = MutableLiveData<Content>(Content())
    var liveContent: LiveData<Content> = _content

    init {
        episodeRepository.clearContent()
    }

    fun fetchEpisodeMediaUrl(fetchFromDb: Boolean = true) {
        liveContent.value?.episodeUrl?.let {
            updateErrorModel(show = false, e = null, isListEmpty = false)
            updateLoading(loading = true)
            val result = episodeRepository.fetchContent(it)
            val animeName = _content.value?.animeName
            if (fetchFromDb) {
                result?.let {
                    result.animeName = animeName ?: ""
                    _content.value = result
                    updateLoading(false)
                } ?: kotlin.run {
                    fetchFromInternet(it)
                }
            } else {
               fetchFromInternet(it)
            }
        }
    }

    private fun fetchFromInternet(url: String){
        compositeDisposable.add(
            episodeRepository.fetchEpisodeMediaUrl(url = url).subscribeWith(
                getEpisodeUrlObserver(
                    C.TYPE_MEDIA_URL
                )
            )
        )
    }

    fun updateEpisodeContent(content: Content) {
        _content.value = content
    }

    private fun getEpisodeUrlObserver(type: Int): DisposableObserver<ResponseBody> {
        return object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {
                updateLoading(false)
                updateErrorModel(show = false, e = null, isListEmpty = false)
            }

            override fun onNext(response: ResponseBody) {
                if (type == C.TYPE_MEDIA_URL) {
                    val episodeInfo = HtmlParser.parseMediaUrl(response = response.string())
                    episodeInfo.vidcdnUrl?.let {
                        compositeDisposable.add(
                            episodeRepository.fetchM3u8Url(episodeInfo.vidcdnUrl!!).subscribeWith(
                                getEpisodeUrlObserver(C.TYPE_M3U8_URL)
                            )
                        )
                    }
                    val watchedEpisode =
                        episodeRepository.fetchWatchedDuration(_content.value?.episodeUrl.hashCode())
                    _content.value?.watchedDuration = watchedEpisode?.watchedDuration ?: 0
                    _content.value?.previousEpisodeUrl = episodeInfo.previousEpisodeUrl
                    _content.value?.nextEpisodeUrl = episodeInfo.nextEpisodeUrl
                } else if (type == C.TYPE_M3U8_URL) {
                    val m3u8Url = HtmlParser.parseM3U8Url(response = response.string())
                    m3u8Url?.let {
                        val content = _content.value
                        content?.url = it
                        _content.value = content
                        saveContent(content!!)
                    }
                }

            }

            override fun onError(e: Throwable) {
                updateLoading(false)
                updateErrorModel(true, e, false)
            }

        }
    }

    fun saveContent(content: Content) {
        if (!content.url.isNullOrEmpty()) {
            episodeRepository.saveContent(content)
        }
    }


    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }
}