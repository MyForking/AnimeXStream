package net.xblacky.animexstream.utils.model

import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

public open class AnimeMetaModel(
    @PrimaryKey
    var ID: Int = 0,
    var typeValue: Int? = null,
    var imageUrl: String = "",
    var categoryUrl: String? = null,
    var episodeUrl: String? = null,
    var title: String = "",
    var episodeNumber: String? = null,
    var genreList: RealmList<GenreModel>? =null,
    var timestamp: Long = System.currentTimeMillis(),
    var insertionOrder: Int = -1,
    var releasedDate: String? =null
): RealmObject()
