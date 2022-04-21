package com.sunandsandsports.assignment.api

import com.sunandsandsports.assignment.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Shaheer cs on 20/04/2022.
 */
interface RandomUserApi {

    @GET("api/")
    suspend fun getRandomUserList(
        @Query("page") pageNo: Int,
        @Query("results") noOfResults: Int
    ): RandomUserResponse

    companion object {
        const val BASE_URL = "https://randomuser.me"
    }
}