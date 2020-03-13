package net.xblacky.animexstream.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.constants.C
import net.xblacky.animexstream.utils.model.ErrorModel
import net.xblacky.animexstream.utils.model.LoadingModel
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class CommonViewModel : ViewModel() {
    private var _isLoading: MutableLiveData<LoadingModel> = MutableLiveData()
    private var _errorModel: MutableLiveData<ErrorModel> = MutableLiveData()
    var isLoading: LiveData<LoadingModel> = _isLoading
    var errorModel: LiveData<ErrorModel> = _errorModel


    protected fun updateErrorModel(show: Boolean,e: Throwable?, isListEmpty: Boolean) {

        var errorCode = C.ERROR_CODE_DEFAULT
        var errorMsgId = R.string.something_went_wrong

        if (e is HttpException) {
            errorCode = when (e.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST -> C.RESPONSE_UNKNOWN
                else -> C.ERROR_CODE_DEFAULT
            }
        } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
            errorCode = C.NO_INTERNET_CONNECTION
            errorMsgId = R.string.no_internet
        } else{
            errorCode = C.ERROR_CODE_DEFAULT
        }

        _errorModel.value = ErrorModel(
            show = show,
            errorCode = errorCode,
            errorMsgId = errorMsgId,
            isListEmpty = isListEmpty
        )

    }

    protected fun isLoading():Boolean{
        return _isLoading.value?.isLoading ?: false
    }

    protected fun updateLoading(loading: Boolean, isListEmpty: Boolean = true){
        _isLoading.value = LoadingModel(
            loading,
            isListEmpty
        )
    }

}