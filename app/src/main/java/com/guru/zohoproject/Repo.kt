package com.guru.zohoproject

/**Created by Guru kathir.J on 27,October,2021 **/
class Repo constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllCountries() = retrofitService.getAllCountries()

}