package com.n1rocket.truefaces

import android.util.Log
import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.datasources.RestDataSource
import com.n1rocket.truefaces.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository(RestDataSource(url = ""))

    fun testRepo() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.testRepo()
            when(result){
                is ApiSuccess -> Log.d("MainViewModel", "Result: ${result.data}")
                is ApiError -> Log.d("MainViewModel", "Result code: ${result.code} message: ${result.message}")
                is ApiException -> Log.d("MainViewModel", "Result Exc: ${result.e.localizedMessage}")
            }
            Log.d("MainViewModel", "Result: $result")
        }
    }

    fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>? {
        var result: ApiResult<String>? = null
        CoroutineScope(Dispatchers.IO).launch {
            result = repository.uploadImage(name, byteArray)
        }
        return result
    }
}
