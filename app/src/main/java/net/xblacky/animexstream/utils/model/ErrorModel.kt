package net.xblacky.animexstream.utils.model

data class ErrorModel(
    var show:Boolean = false,
    var isListEmpty: Boolean = true,
    var errorMsgId: Int,
    var errorCode: Int
)

data class LoadingModel(
    var isLoading: Boolean,
    var isListEmpty: Boolean
)