package com.n1rocket.truefaces

import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.datasources.RestDataSource
import com.n1rocket.truefaces.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository(RestDataSource(url = ""))

    fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>? {
        var result :ApiResult<String>? = null
        CoroutineScope(Dispatchers.IO).launch {
             result = repository.uploadImage(name, byteArray)
        }
        return result
    }
}
