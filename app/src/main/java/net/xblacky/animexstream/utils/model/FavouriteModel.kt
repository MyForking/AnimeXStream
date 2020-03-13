package net.xblacky.animexstream.utils.model

import io.realm.RealmObject

open class FavouriteModel(
    var ID: String? = "",
    var animeName: String? = "",
    var categoryUrl: String? ="",
    var imageUrl: String? ="",
    var releasedDate: String? = null
): RealmObject()