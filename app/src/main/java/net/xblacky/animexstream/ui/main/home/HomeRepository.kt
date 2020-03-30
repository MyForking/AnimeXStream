package net.xblacky.animexstream.ui.main.home

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.Sort
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.rertofit.NetworkInterface
import net.xblacky.animexstream.utils.rertofit.RetrofitHelper
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import net.xblacky.animexstream.utils.realm.InitalizeRealm
import okhttp3.ResponseBody
import retrofit2.Retrofit

class HomeRepository {
    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance()!!


    fun fetchRecentSubOrDub(page: Int, type: Int): Observable<ResponseBody> {
        val fetchHomeListService = retrofit.create(NetworkInterface.FetchRecentSubOrDub::class.java)
        return fetchHomeListService.get(page, type).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchPopularFromAjax(page: Int): Observable<ResponseBody> {
        val fetchPopularListService =
            retrofit.create(NetworkInterface.FetchPopularFromAjax::class.java)
        return fetchPopularListService.get(page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchMovies(page: Int): Observable<ResponseBody> {
        val fetchMoviesListService = retrofit.create(NetworkInterface.FetchMovies::class.java)
        return fetchMoviesListService.get(page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchNewestAnime(page: Int): Observable<ResponseBody> {
        val fetchNewestSeasonService =
            retrofit.create(NetworkInterface.FetchNewestSeason::class.java)
        return fetchNewestSeasonService.get(page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addDataInRealm(animeList: ArrayList<AnimeMetaModel>) {
        val realm: Realm = Realm.getInstance(InitalizeRealm.getConfig())

        try {
            realm.executeTransaction { realm1: Realm ->
                realm1.insertOrUpdate(animeList)
            }
        } catch (ignored: Exception) {
        }
    }

    fun removeFromRealm(){
        val realm: Realm = Realm.getInstance(InitalizeRealm.getConfig())

        realm.executeTransaction{
            val results = it.where(AnimeMetaModel::class.java).lessThanOrEqualTo("timestamp", System.currentTimeMillis() - C.MAX_TIME_FOR_ANIME).findAll()
            results.deleteAllFromRealm()
        }
    }

    fun fetchFromRealm(typeValue: Int): ArrayList<AnimeMetaModel> {
        val realm: Realm = Realm.getInstance(InitalizeRealm.getConfig())


        val list: ArrayList<AnimeMetaModel> = ArrayList()
        try {
            val results =
                realm.where(AnimeMetaModel::class.java)?.equalTo("typeValue", typeValue)?.sort("insertionOrder", Sort.ASCENDING)?.findAll()
            results?.let {
                list.addAll(it)
            }


        } catch (ignored: Exception) {
        }
        return list
    }



}