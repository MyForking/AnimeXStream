package net.xblacky.animexstream.utils.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WatchedEpisode(
    @PrimaryKey
    var id: Int = 0,
    var watchedDuration: Long? = 0,
    var watchedPercentage: Long? = 0,
    var animeName: String = ""
): RealmObject()