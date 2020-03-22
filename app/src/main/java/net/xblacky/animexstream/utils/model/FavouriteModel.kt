package net.xblacky.animexstream.utils.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavouriteModel(
    @PrimaryKey
    var ID: String? = "",
    var animeName: String? = "",
    var categoryUrl: String? ="",
    var imageUrl: String? ="",
    var releasedDate: String? = null,
    var insertionTime: Long = System.currentTimeMillis()
): RealmObject()