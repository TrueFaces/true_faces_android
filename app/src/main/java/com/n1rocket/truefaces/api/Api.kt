package com.n1rocket.truefaces.api

import com.n1rocket.truefaces.datasources.RestDataSource
import com.n1rocket.truefaces.repository.Repository

internal object Api {
    fun getRepository(platformUrl: String): Repository {
        return Repository(RestDataSource(platformUrl))
    }
}
