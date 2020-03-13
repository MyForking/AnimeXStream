package net.xblacky.animexstream.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import net.xblacky.animexstream.utils.CommonViewModel
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import net.xblacky.animexstream.utils.parser.HtmlParser
import okhttp3.ResponseBody

class SearchViewModel : CommonViewModel() {

    private val searchRepository = SearchRepository()
    private var _searchList: MutableLiveData<ArrayList<AnimeMetaModel>> = MutableLiveData()
    private var pageNumber: Int = 1
    private lateinit var keyword: String
    private var _canNextPageLoaded = true
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var searchList: LiveData<ArrayList<AnimeMetaModel>> = _searchList

    fun fetchSearchList(keyword: String) {
        pageNumber = 1
        this.keyword = keyword
        val list = _searchList.value
        list?.clear()
        _searchList.value = list
        if (!super.isLoading()) {
            compositeDisposable.add(
                searchRepository.fetchSearchList(
                    keyword,
                    pageNumber
                ).subscribeWith(getSearchObserver(C.TYPE_SEARCH_NEW))
            )

            updateError(false, null)
            this.updateLoading(true)
        }
    }

    fun fetchNextPage() {
        updateError(false, null)
        if (_canNextPageLoaded && !super.isLoading()) {
            compositeDisposable.add(
                searchRepository.fetchSearchList(
                    keyword,
                    pageNumber
                ).subscribeWith(getSearchObserver(C.TYPE_SEARCH_UPDATE))
            )
            this.updateLoading(true)
        }


    }

    private fun getSearchObserver(searchType: Int): DisposableObserver<ResponseBody> {
        return object : DisposableObserver<ResponseBody>() {
            override fun onComplete() {
            }

            override fun onNext(response: ResponseBody) {
                val list =
                    HtmlParser.parseMovie(response = response.string(), typeValue = C.TYPE_DEFAULT)
                if (list.isNullOrEmpty() || list.size < 20) {
                    _canNextPageLoaded = false
                }
                if (searchType == C.TYPE_SEARCH_NEW) {
                    _searchList.value = list
                } else if (searchType == C.TYPE_SEARCH_UPDATE) {
                    val updatedList = _searchList.value
                    updatedList?.addAll(list)
                    _searchList.value = updatedList
                }
                updateLoading(false)
                pageNumber++
            }

            override fun onError(e: Throwable) {
                updateError(true, e)
            }

        }
    }

    private fun updateLoading(isLoading: Boolean) {
        super.updateLoading(loading = isLoading, isListEmpty = _searchList.value.isNullOrEmpty())
    }

    private fun updateError(show: Boolean, e: Throwable?) {
        super.updateErrorModel(show = show, e = e, isListEmpty = _searchList.value.isNullOrEmpty())
    }

}