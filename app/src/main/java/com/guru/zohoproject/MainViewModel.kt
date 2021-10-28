package com.guru.zohoproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

/**Created by Guru kathir.J on 27,October,2021 **/
class MainViewModel(app:Application):AndroidViewModel(app) {
   private var job: Job? = null
    val isLoading:MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessage:MutableLiveData<String?> = MutableLiveData(null)

    val countryList:MutableLiveData<List<Region>> = MutableLiveData(emptyList())
    private var mainList:List<Country.CountryItem> = emptyList()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val retrofitService = RetrofitService.getInstance()
    private val mainRepository = Repo(retrofitService)
    fun getAllCountries() {
        isLoading.value = true
       var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getAllCountries()
                if (response.isSuccessful) {
                     response.body().let {
                         if (it != null) {
                             mainList =it
                         }
                     }
                    val list = ArrayList<Region>()
                    response.body()?.distinctBy { it.region }?.forEach{
                        list.add(Region(false,it.region,null))
                        response.body()?.filter { it1-> it1.region==it.region }?.forEach{
                            list.add(Region(true,it.region,it))
                        }

                    }

                    countryList.postValue(list)

                    isLoading.postValue(false)
                } else {
                    onError("Error : ${response.message()} ")
                }

        }


    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        isLoading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun performSearch(name:String) {
        val results =  mainList.filter {
            val cName = it.name?.lowercase()
            cName==name|| cName?.contains(name) == true|| cName?.startsWith(name) == true|| cName?.endsWith(name) == true
        }
        val list = ArrayList<Region>()
        results.distinctBy { it.region }.forEach{
            list.add(Region(false,it.region,null))
            results.filter { it1-> it1.region==it.region }.forEach{
                list.add(Region(true,it.region,it))
            }

        }
        countryList.value = list
    }

    fun seachCleared()
    {
        val list = ArrayList<Region>()
        mainList.distinctBy { it.region }.forEach{
            list.add(Region(false,it.region,null))
            mainList.filter { it1-> it1.region==it.region }.forEach{
                list.add(Region(true,it.region,it))
            }

        }
        countryList.value = list
    }
}