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
    //private val repository = Repository(RestDataSource(url = "https://apifast-2-r0282118.deta.app"))
    private val repository = Repository(RestDataSource(url = "https://apifast-1-r0282118.deta.app"))

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

    fun uploadImage(name: String, byteArray: ByteArray) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.uploadImage(name, byteArray)
            when(result){
                is ApiSuccess -> Log.d("MainViewModel", "Result: ${result.data}")
                is ApiError -> Log.d("MainViewModel", "Result code: ${result.code} message: ${result.message}")
                is ApiException -> Log.d("MainViewModel", "Result Exc: ${result.e.localizedMessage}")
            }
            Log.d("MainViewModel", "Result: $result")
        }
    }
}
